package com.wiproevents.security;

import com.wiproevents.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;

/**
 * The application security config.
 */
@Configuration
@EnableWebSecurity
@Order(1)
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * The user detail service.
     */
    @Autowired
    private UserDetailsService userDetailsService;



    /**
     * The stateless auth filter.
     */
    @Autowired
    private StatelessAuthenticationFilter statelessAuthenticationFilter;

    /**
     * The password encoder.
     *
     * @return password encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return Helper.getPasswordEncoder();
    }

    /**
     * Configure global auth manager builder.
     *
     * @param auth the auth manager builder
     * @throws Exception throws if any error happen
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(Helper.getPasswordEncoder());
        authProvider.setUserDetailsService(userDetailsService);
        auth.authenticationProvider(authProvider);
        auth.userDetailsService(userDetailsService);
    }

    /**
     * Create auth manager bean.
     *
     * @return the auth manager bean.
     * @throws Exception throws if any error happen
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/test_pages/**");
    }

    /**
     * Configure authentication.
     *
     * @param http the http
     * @throws Exception if there is any error
     */
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationEntryPoint entryPoint = new CustomAuthenticationEntryPoint();

        http.csrf()
            .disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(statelessAuthenticationFilter, AbstractPreAuthenticatedProcessingFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(entryPoint)
            .and()
            .anonymous()
            .and()
            .servletApi()
            .and()
            .headers()
            .cacheControl()
            .and()
            .and()

            .authorizeRequests()
            .antMatchers("/").permitAll()
                //allow anonymous for lookup,forgot password, update password requests
                .antMatchers("/favicon.ico")
                .permitAll()
                .antMatchers("/lookups/**")
                .permitAll()
                .antMatchers("/initiateForgotPassword")
                .permitAll()
                .antMatchers("/updateForgotPassword")
                .permitAll()
                .antMatchers("/changeForgotPassword")
                .permitAll()
                .antMatchers("/verifyEmail")
                .permitAll()
                .antMatchers(PUT, "/users")
                .hasRole("USER")
                .antMatchers(GET, "/users")
                .hasRole("USER")
                 //allow anonymous calls to social login
                .antMatchers("/signup/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic().authenticationEntryPoint(entryPoint)
                .and()
                .logout().logoutUrl("/x-logout"); // set to another dummy logout url so that it can use ours.
    }
}

