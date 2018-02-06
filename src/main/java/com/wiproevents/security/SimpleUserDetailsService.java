package com.wiproevents.security;


import com.wiproevents.entities.User;
import com.wiproevents.entities.UserRole;
import com.wiproevents.entities.UserStatus;
import com.wiproevents.exceptions.ConfigurationException;
import com.wiproevents.services.UserService;
import com.wiproevents.utils.CustomMessageSource;
import com.wiproevents.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The simple implementation of the UserDetailsService.
 */
@Service
public class SimpleUserDetailsService implements UserDetailsService {
    /**
     * The UserService to load user by username.
     */
    @Autowired
    private UserService userService;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(userService, "userService");
    }

    /**
     * Locates the user based on the username.
     *
     * @param email the user email.
     * @return the UserDetails
     * @throws UsernameNotFoundException if there is no match or invalid user found.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = userService.getUserByEmail(email);
            if (user == null) {
                throw new UsernameNotFoundException(
                        CustomMessageSource.getMessage("user.notFound.byUsername", email));
            }
            if (user.getRoles() == null || user.getRoles().isEmpty()) {
                throw new UsernameNotFoundException(
                        CustomMessageSource.getMessage("user.noRoles.error", email));
            }
            List<GrantedAuthority> authorities = buildUserAuthority(user.getRoles());
            return buildUserForAuthentication(user, authorities);
        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UsernameNotFoundException(CustomMessageSource.getMessage("loadUserByUsername.error"), e);
        }
    }

    /**
     * Build the user authority.
     *
     * @param roles the roles
     * @return the authority
     */
    private List<GrantedAuthority> buildUserAuthority(List<UserRole> roles) {
        Set<GrantedAuthority> auths = new HashSet<>();
        for (UserRole role : roles) {
            auths.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new ArrayList<>(auths);
    }

    /**
     * Build the user details entity.
     *
     * @param user the user
     * @param authorities the authorities
     * @return user details entity
     */
    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        // will check active/inactive status here
        return new CustomUserDetails(user, UserStatus.ACTIVE.equals(user.getStatus()), true, true, true, authorities);
    }
}
