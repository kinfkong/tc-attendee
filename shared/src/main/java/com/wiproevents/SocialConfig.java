package com.wiproevents;

import com.wiproevents.security.social.DefaultConnectionFactoryConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

@Configuration
public class SocialConfig  {

    @Value("${social.facebook.appId}")
    private String facebookClientId;

    @Value("${social.facebook.appSecret}")
    private String facebookClientSecret;

    @Value("${social.linkedin.appId}")
    private String linkedInClientId;

    @Value("${social.linkedin.appSecret}")
    private String linkedInClientSecret;

    @Value("${social.twitter.appId}")
    private String twitterClientId;

    @Value("${social.twitter.appSecret}")
    private String twitterClientSecret;

    @Value("${social.google.appId}")
    private String googleClientId;

    @Value("${social.google.appSecret}")
    private String googleClientSecret;



    private DefaultConnectionFactoryConfigurer defaultConnectionFactoryConfigurer = new DefaultConnectionFactoryConfigurer();

    @Bean
    public ConnectionFactoryLocator connectionFactoryLocator() {

        FacebookConnectionFactory fbFactory = new FacebookConnectionFactory(facebookClientId, facebookClientSecret);
        fbFactory.setScope("public_profile, email");
        defaultConnectionFactoryConfigurer.addConnectionFactory(fbFactory);

        LinkedInConnectionFactory linkedInFactory = new LinkedInConnectionFactory(linkedInClientId, linkedInClientSecret);
        defaultConnectionFactoryConfigurer.addConnectionFactory(linkedInFactory);

        TwitterConnectionFactory twitterConnectionFactory = new TwitterConnectionFactory(twitterClientId, twitterClientSecret);
        defaultConnectionFactoryConfigurer.addConnectionFactory(twitterConnectionFactory);

        GoogleConnectionFactory googleConnectionFactory = new GoogleConnectionFactory(googleClientId, googleClientSecret);
        googleConnectionFactory.setScope("profile email openid");
        defaultConnectionFactoryConfigurer.addConnectionFactory(googleConnectionFactory);
        return defaultConnectionFactoryConfigurer.getConnectionFactoryLocator();

    }
}