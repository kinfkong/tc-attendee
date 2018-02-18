package com.wiproevents.controllers;

import com.wiproevents.entities.User;
import com.wiproevents.entities.UserPreference;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.exceptions.ConfigurationException;
import com.wiproevents.exceptions.EntityNotFoundException;
import com.wiproevents.services.UserPreferenceService;
import com.wiproevents.utils.Helper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

/**
 * The User REST controller. Is effectively thread safe.
 */
@RestController
@RequestMapping("/userPreferences")
@NoArgsConstructor
public class UserPreferenceController {
    /**
     * The user service used to perform operations. Should be non-null after injection.
     */
    @Autowired
    private UserPreferenceService userPreferenceService;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(userPreferenceService, "userPreferenceService");
    }


    /**
     * This method is used to retrieve an entity.
     *
     * @return the match entity
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws AttendeeException if any other error occurred during operation
     */
    @RequestMapping(method = RequestMethod.GET)
    public UserPreference get() throws AttendeeException {
        // get the user id of the current auth user
        User user = Helper.getAuthUser();

        return userPreferenceService.findByUserId(user.getId());
    }

    /**
     * This method is used to update an entity.
     *
     * @param entity the entity to update
     * @return the updated entity
     * @throws IllegalArgumentException if id is not positive or entity is null or id of entity is not positive
     * or id of  entity not match id or entity is invalid
     * @throws EntityNotFoundException if the entity does not exist
     * @throws AttendeeException if any other error occurred during operation
     */
    @RequestMapping(method = RequestMethod.PUT)
    @Transactional
    public UserPreference update(@RequestBody UserPreference entity) throws AttendeeException {
        // get the user id of the current auth user
        User user = Helper.getAuthUser();

        UserPreference userPreference = userPreferenceService.findByUserId(user.getId());
        entity.setUserId(user.getId());
        entity.setId(userPreference.getId());

        if (!userPreference.getUserId().equals(user.getId())) {
            throw new IllegalArgumentException("the user id is not correct.");
        }

        entity.setId(userPreference.getId());

        return userPreferenceService.update(entity.getId(), entity);
    }


}

