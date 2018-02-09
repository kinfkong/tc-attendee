package com.wiproevents.security.social;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.WebRequest;

/**
 * Created by wangjinggang on 2018/2/9.
 */
@Component
public class FacebookConnectInterceptor implements ConnectInterceptor<Facebook> {

    /**
     * Called during connection initiation, immediately before user authorization.
     * May be used to store custom connection attributes in the session before redirecting the user to the provider's site or to contribute parameters to the authorization URL.
     * @param connectionFactory the connection factory in play for this connection
     * @param parameters parameters being sent to the provider during authorization
     * @param request the request
     */
    @Override
    public void preConnect(ConnectionFactory<Facebook> connectionFactory, MultiValueMap<String, String> parameters, WebRequest request) {

    }

    /**
     * Called immediately after the connection is established.
     * Used to invoke the service API on behalf of the user upon connecting.
     * @param connection the connection that was just established
     * @param request the request
     */
    @Override
    public void postConnect(Connection<Facebook> connection, WebRequest request) {
        System.out.println("connected");
    }
}