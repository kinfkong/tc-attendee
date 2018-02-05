package com.wiproevents.controllers;

import com.wiproevents.utils.springdata.extensions.SearchResult;
import com.wiproevents.entities.User;
import com.wiproevents.entities.UserSearchCriteria;
import com.wiproevents.exceptions.AccessDeniedException;
import com.wiproevents.exceptions.ConfigurationException;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.UserService;
import com.wiproevents.utils.Helper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * The login REST controller. Is effectively thread safe.
 */
@RestController
@RequestMapping("/login")
@NoArgsConstructor
public class LoginController {

    /**
     * The user service used to perform operations. Should be non-null after injection.
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
     * This method is used to login with basic auth and return JWT token with expired time.
     *
     * @return the JWT token
     * @throws AccessDeniedException if does not allow to perform action
     * @throws AttendeeException if any other error occurred during operation
     */
    @RequestMapping(method = RequestMethod.POST)
    public Map<String, String> login() throws AttendeeException {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        UserSearchCriteria criteria = new UserSearchCriteria();
        criteria.setEmail(authentication.getName());
        SearchResult<User> users = userService.search(criteria, null);
        // validate valid user exists in SimpleUserDetailsService already
        User user = users.getEntities().get(0);
        String token = userService.createTokenForUser(user);
        Map<String, String> result = new HashMap<>();
        result.put("token", token);
        return result;
    }
}
