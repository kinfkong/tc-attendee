package com.wiproevents.controllers;

import com.wiproevents.entities.*;
import com.wiproevents.exceptions.AccessDeniedException;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.exceptions.ConfigurationException;
import com.wiproevents.exceptions.EntityNotFoundException;
import com.wiproevents.security.UserAuthentication;
import com.wiproevents.services.UserService;
import com.wiproevents.utils.Helper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
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

    @Value("${cookie.tokenMaxAgesInSec}")
    private int cookieTokenMaxAgesInSec;

    @Value("${cookie.tokenName}")
    private String cookieAuthTokenName;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        super.checkConfiguration();
        Helper.checkConfigNotNull(userService, "userService");
        Helper.checkConfigPositive(cookieTokenMaxAgesInSec, "cookieTokenMaxAgesInSec");
        Helper.checkConfigNotNull(cookieAuthTokenName, "cookieAuthTokenName");
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
        User user =  userService.create(entity);
        VerifyEmailToken verifyEmailToken = userService.createVerifyEmailToken(user.getId());
        // send the email
        Context context = new Context();

        context.setVariable("user", user);
        context.setVariable("verifyEmailToken", verifyEmailToken);

        super.sendEmail(user.getEmail(), "signup", context);
        return user;
    }

    /**
     * This method is used to login with basic auth and return JWT token with expired time.
     *
     * @return the JWT token
     * @throws AccessDeniedException if does not allow to perform action
     * @throws AttendeeException if any other error occurred during operation
     */
    @RequestMapping(method = RequestMethod.POST, value = "login")
    public Map<String, String> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) throws AttendeeException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // validate valid user exists in SimpleUserDetailsService already
        User user = userService.getUserByEmail(authentication.getName());

        String token = userService.createTokenForUser(user);

        Map<String, String> result = new HashMap<>();
        result.put("token", token);

        // write the cookies
        Cookie cookie = new Cookie(cookieAuthTokenName, token);
        if (loginRequest.isRememberMe()) {
            cookie.setMaxAge(cookieTokenMaxAgesInSec);
        } else {
            // session cookie
            cookie.setMaxAge(-1);
        }

        response.addCookie(cookie);
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "logout")
    public boolean logout(HttpServletResponse response) throws AttendeeException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UserAuthentication) {
            String currentToken = ((UserAuthentication) authentication).getCurrentAuthToken();
            // revoke the token
            userService.revokeAccessToken(currentToken);
        }
        Cookie cookie = new Cookie(cookieAuthTokenName, null);
        // delete the cookie
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return true;
    }

    @RequestMapping(method = RequestMethod.GET, value = "me")
    public User getMe() throws AttendeeException {
        return userService.getMe();
    }

    @RequestMapping(method = RequestMethod.POST, value = "initiateForgotPassword")
    public boolean initiateForgotPassword(@RequestParam String email) throws AttendeeException {
        Helper.checkNullOrEmpty(email, email);
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

    @RequestMapping(method = RequestMethod.GET, value = "verifyEmail")
    public boolean verifyEmail(@RequestParam String email, @RequestParam String verificationToken) throws AttendeeException {
        return userService.verifyEmail(email, verificationToken);
    }

}

