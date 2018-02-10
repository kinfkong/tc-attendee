package com.wiproevents.controllers;

import com.wiproevents.entities.ForgotPassword;
import com.wiproevents.entities.NewPassword;
import com.wiproevents.entities.User;
import com.wiproevents.exceptions.AccessDeniedException;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.exceptions.ConfigurationException;
import com.wiproevents.exceptions.EntityNotFoundException;
import com.wiproevents.services.UserService;
import com.wiproevents.utils.Helper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * The User REST controller. Is effectively thread safe.
 */
@RestController
@RequestMapping("/")
@NoArgsConstructor
public class SecurityController extends BaseEmailController {
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
        super.checkConfiguration();
        Helper.checkConfigNotNull(userService, "userService");
    }


    /**
     * This method is used to create an entity.
     *
     * @param entity the entity to create
     * @return the created entity
     * @throws IllegalArgumentException if entity is null or not valid
     * @throws AttendeeException if any other error occurred during operation
     */
    @Transactional
    @RequestMapping(method = RequestMethod.POST, value = "signup")
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User entity) throws AttendeeException {
        return userService.create(entity);
    }

    /**
     * This method is used to login with basic auth and return JWT token with expired time.
     *
     * @return the JWT token
     * @throws AccessDeniedException if does not allow to perform action
     * @throws AttendeeException if any other error occurred during operation
     */
    @RequestMapping(method = RequestMethod.POST, value = "login")
    public Map<String, String> login() throws AttendeeException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // validate valid user exists in SimpleUserDetailsService already
        User user = userService.getUserByEmail(authentication.getName());

        String token = userService.createTokenForUser(user);

        Map<String, String> result = new HashMap<>();
        result.put("token", token);

        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "me")
    public User getMe() throws AttendeeException {
        return userService.getMe();
    }

    @RequestMapping(method = RequestMethod.POST, value = "initiateForgotPassword")
    public boolean initiateForgotPassword(@RequestParam String email) throws AttendeeException {
        Helper.checkNullOrEmpty("email", email);
        User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new EntityNotFoundException("There is no user of email: " + email);
        }
        ForgotPassword forgotPassword = userService.forgotPassword(user.getId());
        // send the email
        Context context = new Context();

        context.setVariable("user", user);
        context.setVariable("forgotPassword", forgotPassword);

        super.sendEmail(email, "forgotPassword", context);
        return true;
    }

    @RequestMapping(method = RequestMethod.POST, value = "changeForgotPassword")
    public boolean changeForgotPassword(@RequestBody NewPassword newPassword) throws AttendeeException {
        userService.updatePasswordWithForgotPasswordToken(newPassword);
        return true;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "updatePassword")
    public boolean updatePassword(@RequestBody NewPassword newPassword) throws AttendeeException {
        userService.updatePasswordWithOldPassword(newPassword);
        return true;
    }
}

