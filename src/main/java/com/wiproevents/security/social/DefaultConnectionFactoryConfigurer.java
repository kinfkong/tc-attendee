package com.wiproevents.security.social;

import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;

/**
 * Created by wangjinggang on 2018/2/9.
 */
public class DefaultConnectionFactoryConfigurer implements ConnectionFactoryConfigurer {
    private ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();

    public DefaultConnectionFactoryConfigurer() {
    }

    public void addConnectionFactory(ConnectionFactory<?> connectionFactory) {
        this.registry.addConnectionFactory(connectionFactory);
    }

    public ConnectionFactoryRegistry getConnectionFactoryLocator() {
        return this.registry;
    }
}
