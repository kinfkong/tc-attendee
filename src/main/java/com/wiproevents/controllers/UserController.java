package com.wiproevents.controllers;

import com.wiproevents.entities.ForgotPassword;
import com.wiproevents.entities.NewPassword;
import com.wiproevents.entities.User;
import com.wiproevents.entities.UserSearchCriteria;
import com.wiproevents.exceptions.AccessDeniedException;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.exceptions.ConfigurationException;
import com.wiproevents.exceptions.EntityNotFoundException;
import com.wiproevents.services.UserService;
import com.wiproevents.utils.Helper;
import com.wiproevents.utils.springdata.extensions.Paging;
import com.wiproevents.utils.springdata.extensions.SearchResult;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

/**
 * The User REST controller. Is effectively thread safe.
 */
@RestController
@RequestMapping("/users")
@NoArgsConstructor
public class UserController extends BaseEmailController {
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
     * This method is used to retrieve an entity.
     *
     * @param id the id of the entity to retrieve
     * @return the match entity
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws AttendeeException if any other error occurred during operation
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public User get(@PathVariable String id) throws AttendeeException {
        return userService.get(id);
    }


    /**
     * This method is used to update an entity.
     *
     * @param id the id of the entity to update
     * @param entity the entity to update
     * @return the updated entity
     * @throws IllegalArgumentException if id is not positive or entity is null or id of entity is not positive
     * or id of  entity not match id or entity is invalid
     * @throws EntityNotFoundException if the entity does not exist
     * @throws AttendeeException if any other error occurred during operation
     */
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @Transactional
    public User update(@PathVariable String id, @RequestBody User entity) throws AttendeeException {
        return userService.update(id, entity);
    }

    /**
     * This method is used to delete an entity.
     *
     * @param id the id of the entity to delete
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws AttendeeException if any other error occurred during operation
     */
    @Transactional
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String id) throws AttendeeException {
        userService.delete(id);
    }

    /**
     * This method is used to search for entities by criteria and paging params.
     *
     * @param criteria the search criteria
     * @param paging the paging data
     * @return the search result
     * @throws IllegalArgumentException if pageSize is not positive or pageNumber is negative
     * @throws AttendeeException if any other error occurred during operation
     */
    @RequestMapping(method = RequestMethod.GET)
    public SearchResult<User> search(@ModelAttribute UserSearchCriteria criteria,
                                     @ModelAttribute Paging paging) throws AttendeeException {
        return userService.search(criteria, paging);
    }



    /**
     * This method is used to start the forgot password process.
     *
     * @param email the user email.
     * @throws IllegalArgumentException if email is null or empty or not email address
     * @throws EntityNotFoundException if the entity does not exist
     * @throws AccessDeniedException if does not allow to perform action
     * @throws AttendeeException if any other error occurred during operation
     */
    @Transactional(rollbackOn = AttendeeException.class)
    @RequestMapping(value = "forgotPassword", method = RequestMethod.PUT)
    public void forgotPassword(@RequestParam(value = "email") String email) throws AttendeeException {
        Helper.checkEmail(email, "email");
        UserSearchCriteria criteria = new UserSearchCriteria();
        criteria.setEmail(email);
        SearchResult<User> users = userService.search(criteria, null);
        if (users.getTotal() == 0) {
            throw new EntityNotFoundException(String.format("No user found with email %s", email));
        }
        User user = users.getEntities().get(0);
        String userId = user.getId();
        ForgotPassword forgotPassword = userService.forgotPassword(userId);
        Context model = new Context();
        model.setVariable("user", user);
        model.setVariable("forgotPassword", forgotPassword);
        sendEmail(email, "forgotPassword", model);
    }


    /**
     * This method is used to update the password.
     *
     * @param newPassword the new password request
     * @return true if update the password successfully otherwise false
     * @throws IllegalArgumentException if newPassword is null or invalid
     * @throws AttendeeException if any other error occurred during operation
     */
    @Transactional
    @RequestMapping(value = "updatePassword", method = RequestMethod.PUT)
    public boolean updatePassword(@RequestBody NewPassword newPassword) throws AttendeeException {
        return userService.updatePassword(newPassword);
    }


}

