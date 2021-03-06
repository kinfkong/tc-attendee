package com.wiproevents.security;

import com.wiproevents.entities.User;
import com.wiproevents.entities.UserPermission;
import com.wiproevents.entities.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

/**
 * The user authentication.
 */
public class UserAuthentication implements Authentication {
    /**
     * The user.
     */
    private User user;

    /**
     * The authenticated flag.
     */
    private boolean authenticated = true;


    private String currentAuthToken;

    /**
     * The user authentication constructor.
     *
     * @param entity the user.
     */
    public UserAuthentication(User entity) {
        this.user = entity;
    }

    /**
     * Get authorities.
     *
     * @return the user  authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> auths = new HashSet<>();
        List<UserRole> roles = user.getRoles();
        if (roles != null) {
            for (UserRole role : roles) {
                auths.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
                if (role.getPermissions() != null) {
                    for (UserPermission p : role.getPermissions()) {
                        auths.add(new SimpleGrantedAuthority(p.getName()));
                    }

                }

            }
        }
        return new ArrayList<>(auths);
    }

    /**
     * Get credentials.
     *
     * @return the credentials
     */
    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * Get user details.
     *
     * @return the user details.
     */
    @Override
    public Object getDetails() {
        return user;
    }

    /**
     * Get user principal.
     *
     * @return the user principal
     */
    @Override
    public Object getPrincipal() {
        return user;
    }

    /**
     * Return authenticated flag.
     *
     * @return authenticated flag.
     */
    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    /**
     * Set authenticated flag.
     *
     * @param authenticatedFlag authenticated flag
     * @throws IllegalArgumentException throws if invalid request.
     */
    @Override
    public void setAuthenticated(boolean authenticatedFlag) throws IllegalArgumentException {
        this.authenticated = authenticatedFlag;
    }

    /**
     * Get user fullName.
     *
     * @return the username.
     */
    @Override
    public String getName() {
        return user.getEmail();
    }

    public String getCurrentAuthToken() {
        return currentAuthToken;
    }

    public void setCurrentAuthToken(String currentAuthToken) {
        this.currentAuthToken = currentAuthToken;
    }
}
