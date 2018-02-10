package com.wiproevents.security;

import com.wiproevents.entities.User;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.exceptions.ConfigurationException;
import com.wiproevents.services.UserService;
import com.wiproevents.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * The token auth service.
 */
@Service
public class TokenAuthenticationService {
    /**
     * The token auth header fullName.
     */
    private static final String AUTH_HEADER_NAME = "Authorization";

    /**
     * The auth cookie fullName.
     */
    private static final String AUTH_COOKIE_NAME = "AUTH-ACCESS-TOKEN";

    /**
     * The token expires in milliseconds for 10 days.
     */
    @Value("${token.expirationTimeInMillis}")
    private long tokenExpirationTimeInMillis;

    /**
     * The token handler.
     */
    @Autowired
    private UserService userService;

    /**
     * The token auth service constructor.
     */
    public TokenAuthenticationService() {

    }

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(userService, "tokenHandler");
        Helper.checkConfigPositive(tokenExpirationTimeInMillis, "tokenExpirationTimeInMillis");
    }


    /**
     * Get user auth object from request.
     *
     * @param request the servlet request.
     * @return the user auth request.
     */
    public UserAuthentication getAuthentication(HttpServletRequest request) {
        final String tokenString = request.getHeader(AUTH_HEADER_NAME);
        if (tokenString != null && tokenString.split(" ").length > 1) {
            final String token = tokenString.split(" ")[1];
            User user;
            try {
                user = userService.getUserByAccessToken(token);
            } catch (AttendeeException e) {
                user = null;
            }
            if (user != null) {
                UserAuthentication authentication = new UserAuthentication(user);
                authentication.setCurrentAuthToken(token);
                return authentication;
            }
        }
        return null;
    }

}
