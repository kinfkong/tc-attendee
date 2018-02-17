package com.wiproevents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * The main application.
 */
@SpringBootApplication
@EnableZuulProxy
public class GatewayApplication {

    /**
     * The main entry point of the application.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
