package com.livingprogress.mentorme.controllers;

import com.livingprogress.mentorme.entities.UserRole;
import com.livingprogress.mentorme.exceptions.ConfigurationException;
import com.livingprogress.mentorme.exceptions.MentorMeException;
import com.livingprogress.mentorme.services.LookupService;
import com.livingprogress.mentorme.utils.Helper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * The Lookup REST controller. Is effectively thread safe.
 */
@RestController
@RequestMapping("/lookups")
@NoArgsConstructor
public class LookupController {
    /**
     * The lookup service used to perform operations. Should be non-null after injection.
     */
    @Autowired
    private LookupService lookupService;


    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(lookupService, "lookupService");
    }

    /**
     * This method is used to get user role lookups.
     *
     * @return the lookups for user role.
     * @throws MentorMeException if any other error occurred during operation
     */
    @RequestMapping(value = "/userRoles", method = RequestMethod.GET)
    public List<UserRole> getUserRoles() throws MentorMeException {
        return lookupService.getUserRoles();
    }

}

