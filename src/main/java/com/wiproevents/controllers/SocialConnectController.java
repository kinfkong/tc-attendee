package com.wiproevents.controllers;

/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.wiproevents.entities.SocialUser;
import com.wiproevents.entities.User;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.support.OAuth1ConnectionFactory;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.connect.web.*;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Generic UI controller for managing the account-to-service-provider connection flow.
 * <ul>
 * <li>GET /connect/{providerId}  - Get a web page showing connection status to {providerId}.</li>
 * <li>POST /connect/{providerId} - Initiate an connection with {providerId}.</li>
 * <li>GET /connect/{providerId}?oauth_verifier||code - Receive {providerId} authorization callback and establish the connection.</li>
 * <li>DELETE /connect/{providerId} - Disconnect from {providerId}.</li>
 * </ul>
 * @author Keith Donald
 * @author Craig Walls
 * @author Roy Clarkson
 */
@Controller
@RequestMapping("/signup")
public class SocialConnectController {

    private final static Log logger = LogFactory.getLog(org.springframework.social.connect.web.ConnectController.class);

    @Autowired
    private ConnectionFactoryLocator connectionFactoryLocator;

    @Value("${social.oauth.result.url}")
    private String resultUrl;

    private final MultiValueMap<Class<?>, ConnectInterceptor<?>> connectInterceptors = new LinkedMultiValueMap<Class<?>, ConnectInterceptor<?>>();

    private final MultiValueMap<Class<?>, DisconnectInterceptor<?>> disconnectInterceptors = new LinkedMultiValueMap<Class<?>, DisconnectInterceptor<?>>();

    private ConnectSupport connectSupport;

    private final UrlPathHelper urlPathHelper = new UrlPathHelper();

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    private String applicationUrl = null;

    @Autowired
    private UserService userService;

    /**
     * Sets a strategy to use when persisting information that is to survive past the boundaries of a request.
     * The default strategy is to set the data as attributes in the HTTP Session.
     * @param sessionStrategy the session strategy.
     */
    public void setSessionStrategy(SessionStrategy sessionStrategy) {
        this.sessionStrategy = sessionStrategy;
    }



    /**
     * Process a connect form submission by commencing the process of establishing a connection to the provider on behalf of the member.
     * For OAuth1, fetches a new request token from the provider, temporarily stores it in the session, then redirects the member to the provider's site for authorization.
     * For OAuth2, redirects the user to the provider's site for authorization.
     * @param providerId the provider ID to connect to
     * @param request the request
     * @return a RedirectView to the provider's authorization page or to the connection status page if there is an error
     */
    @RequestMapping(value="/{providerId}", method=RequestMethod.POST)
    public RedirectView connect(@PathVariable String providerId, NativeWebRequest request) {
        ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(providerId);
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        try {
            return new RedirectView(connectSupport.buildOAuthUrl(connectionFactory, request, parameters));
        } catch (Exception e) {
            sessionStrategy.setAttribute(request, PROVIDER_ERROR_ATTRIBUTE, e);
            return connectionStatusRedirect(providerId, request, null, e);
        }
    }


    /**
     * Process the authorization callback from an OAuth 2 service provider.
     * Called after the user authorizes the connection, generally done by having he or she click "Allow" in their web browser at the provider's site.
     * On authorization verification, connects the user's local account to the account they hold at the service provider.
     * @param providerId the provider ID to connect to
     * @param request the request
     * @return a RedirectView to the connection status page
     */
    @RequestMapping(value="/{providerId}", method=RequestMethod.GET, params="code")
    public RedirectView oauth2Callback(@PathVariable String providerId, NativeWebRequest request) {
        User user = null;
        try {
            OAuth2ConnectionFactory<?> connectionFactory = (OAuth2ConnectionFactory<?>) connectionFactoryLocator.getConnectionFactory(providerId);
            Connection<?> connection = connectSupport.completeConnection(connectionFactory, request);
            if (!providerId.equals(connection.getKey().getProviderId())) {
                throw new IllegalArgumentException("invalid providerId" + providerId + " in url.");
            }

            User innerUser = processConnection(providerId, connection);
            return connectionStatusRedirect(providerId, request, innerUser, null);
        } catch (Exception e) {
            sessionStrategy.setAttribute(request, PROVIDER_ERROR_ATTRIBUTE, e);
            logger.warn("Exception while handling OAuth2 callback (" + e.getMessage() + "). Redirecting to " + providerId +" connection status page.");
            return connectionStatusRedirect(providerId, request, null, e);
        }

    }

    private User processConnection(String providerId, Connection<?> connection) throws AttendeeException {
        User user;
        String providerUserId = connection.getKey().getProviderUserId();
        user = userService.getUserBySocial(providerId, providerUserId);

        if (user == null) {
            user = new User();

            // fetch the profile
            if (providerId.equals("facebook")) {
                // for facebook, to workaround for this bug:
                // https://stackoverflow.com/questions/39890885/error-message-is-12-bio-field-is-deprecated-for-versions-v2-8-and-higher
                Facebook facebook = (Facebook) connection.getApi();
                String [] fields = { "id", "email"};
                org.springframework.social.facebook.api.User profile =
                        facebook.fetchObject("me", org.springframework.social.facebook.api.User.class, fields);
                user.setEmail(profile.getEmail());
            } else {
                UserProfile profile = connection.fetchUserProfile();
                user.setEmail(profile.getEmail());
            }

            user.setFullName(connection.getDisplayName());
            user.setProfilePictureURL(connection.getImageUrl());

            SocialUser socialUser = new SocialUser();

            socialUser.setProviderId(providerId);
            socialUser.setProviderUserId(providerUserId);

            // create the user
            user = userService.createSocialUser(socialUser, user);
        }
        return user;
    }

    /**
     * Process the authorization callback from an OAuth 1 service provider.
     * Called after the user authorizes the connection, generally done by having he or she click "Allow" in their web browser at the provider's site.
     * On authorization verification, connects the user's local account to the account they hold at the service provider
     * Removes the request token from the session since it is no longer valid after the connection is established.
     * @param providerId the provider ID to connect to
     * @param request the request
     * @return a RedirectView to the connection status page
     */
    @RequestMapping(value="/{providerId}", method=RequestMethod.GET, params="oauth_token")
    public RedirectView oauth1Callback(@PathVariable String providerId, NativeWebRequest request) {
        try {
            OAuth1ConnectionFactory<?> connectionFactory = (OAuth1ConnectionFactory<?>) connectionFactoryLocator.getConnectionFactory(providerId);
            Connection<?> connection = connectSupport.completeConnection(connectionFactory, request);
            User innerUser = processConnection(providerId, connection);
            return connectionStatusRedirect(providerId, request, innerUser, null);
        } catch (Exception e) {
            sessionStrategy.setAttribute(request, PROVIDER_ERROR_ATTRIBUTE, e);
            logger.warn("Exception while handling OAuth1 callback (" + e.getMessage() + "). Redirecting to " + providerId +" connection status page.");
            return connectionStatusRedirect(providerId, request, null, e);
        }
    }


    /**
     * Process an error callback from an OAuth 2 authorization as described at http://tools.ietf.org/html/rfc6749#section-4.1.2.1.
     * Called after upon redirect from an OAuth 2 provider when there is some sort of error during authorization, typically because the user denied authorization.
     * @param providerId the provider ID that the connection was attempted for
     * @param error the error parameter sent from the provider
     * @param errorDescription the error_description parameter sent from the provider
     * @param errorUri the error_uri parameter sent from the provider
     * @param request the request
     * @return a RedirectView to the connection status page
     */
    @RequestMapping(value="/{providerId}", method=RequestMethod.GET, params="error")
    public RedirectView oauth2ErrorCallback(@PathVariable String providerId,
                                            @RequestParam("error") String error,
                                            @RequestParam(value="error_description", required=false) String errorDescription,
                                            @RequestParam(value="error_uri", required=false) String errorUri,
                                            NativeWebRequest request) {
        Map<String, String> errorMap = new HashMap<String, String>();
        errorMap.put("error", error);
        if (errorDescription != null) { errorMap.put("errorDescription", errorDescription); }
        if (errorUri != null) { errorMap.put("errorUri", errorUri); }
        sessionStrategy.setAttribute(request, AUTHORIZATION_ERROR_ATTRIBUTE, errorMap);
        return connectionStatusRedirect(providerId, request, null, new IllegalStateException("Failed to authorized, " + error));
    }


    /**
     * Returns a RedirectView with the URL to redirect to after a connection is created or deleted.
     * Defaults to "/connect/{providerId}" relative to DispatcherServlet's path.
     * May be overridden to handle custom redirection needs.
     * @param providerId the ID of the provider for which a connection was created or deleted.
     * @param request the NativeWebRequest used to access the servlet path when constructing the redirect path.
     * @return a RedirectView to the page to be displayed after a connection is created or deleted
     */
    protected RedirectView connectionStatusRedirect(String providerId, NativeWebRequest request, User user, Exception e) {
        String path = resultUrl;
        try {
            if (e != null) {
                String msg = e.getMessage();
                if (msg == null) {
                    msg = "unknown reason.";
                }
                path += "?err=" + URLEncoder.encode(msg, "UTF-8");
            } else if (user == null) {
                path += "?err=" + URLEncoder.encode("error in fetch the inner user", "UTF-8");
            } else {
                String token = userService.createTokenForUser(user);
                path += "?accessToken=" + token;
            }

        } catch (UnsupportedEncodingException e1) {
            // ignore
        }
        return new RedirectView(path, true);
    }

    // internal helpers

    private boolean prependServletPath(HttpServletRequest request) {
        return !this.urlPathHelper.getPathWithinServletMapping(request).equals("");
    }

    /*
     * Determines the path extension, if any.
     * Returns the extension, including the period at the beginning, or an empty string if there is no extension.
     * This makes it possible to append the returned value to a path even if there is no extension.
     */
    private String getPathExtension(HttpServletRequest request) {
        String fileName = extractFullFilenameFromUrlPath(request.getRequestURI());
        String extension = StringUtils.getFilenameExtension(fileName);
        return extension != null ? "." + extension : "";
    }

    private String extractFullFilenameFromUrlPath(String urlPath) {
        int end = urlPath.indexOf('?');
        if (end == -1) {
            end = urlPath.indexOf('#');
            if (end == -1) {
                end = urlPath.length();
            }
        }
        int begin = urlPath.lastIndexOf('/', end) + 1;
        int paramIndex = urlPath.indexOf(';', begin);
        end = (paramIndex != -1 && paramIndex < end ? paramIndex : end);
        return urlPath.substring(begin, end);
    }


    private void processFlash(WebRequest request, Model model) {
        convertSessionAttributeToModelAttribute(DUPLICATE_CONNECTION_ATTRIBUTE, request, model);
        convertSessionAttributeToModelAttribute(PROVIDER_ERROR_ATTRIBUTE, request, model);
        model.addAttribute(AUTHORIZATION_ERROR_ATTRIBUTE, sessionStrategy.getAttribute(request, AUTHORIZATION_ERROR_ATTRIBUTE));
        sessionStrategy.removeAttribute(request, AUTHORIZATION_ERROR_ATTRIBUTE);
    }

    private void convertSessionAttributeToModelAttribute(String attributeName, WebRequest request, Model model) {
        if (sessionStrategy.getAttribute(request, attributeName) != null) {
            model.addAttribute(attributeName, Boolean.TRUE);
            sessionStrategy.removeAttribute(request, attributeName);
        }
    }

    private void setNoCache(NativeWebRequest request) {
        HttpServletResponse response = request.getNativeResponse(HttpServletResponse.class);
        if (response != null) {
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 1L);
            response.setHeader("Cache-Control", "no-cache");
            response.addHeader("Cache-Control", "no-store");
        }
    }

    protected static final String DUPLICATE_CONNECTION_ATTRIBUTE = "social_addConnection_duplicate";

    protected static final String PROVIDER_ERROR_ATTRIBUTE = "social_provider_error";

    protected static final String AUTHORIZATION_ERROR_ATTRIBUTE = "social_authorization_error";

}
