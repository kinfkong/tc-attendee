package com.wiproevents;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.social.config.annotation.SocialConfiguration;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.security.SocialAuthenticationServiceLocator;

@Configuration
@Order(1)
public class SocialConfig {

    @Value("${social.facebook.appId}")
    private String facebookClientId;

    @Value("${social.facebook.appSecret}")
    private String facebookClientSecret;

    @Bean
    public ConnectionFactoryLocator connectionFactoryLocator() {
        SocialConfiguration a;
        SocialAuthenticationServiceLocator b;
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();

        FacebookConnectionFactory fbFactory = new FacebookConnectionFactory (
                facebookClientId,
                facebookClientSecret);
        registry.addConnectionFactory(fbFactory);

        fbFactory.setScope("public_profile, email");

        return registry;
    }

}