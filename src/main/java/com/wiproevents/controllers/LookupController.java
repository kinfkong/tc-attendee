package com.wiproevents.controllers;

import com.wiproevents.entities.*;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.exceptions.ConfigurationException;
import com.wiproevents.services.LookupService;
import com.wiproevents.utils.Helper;
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
     * @throws AttendeeException if any other error occurred during operation
     */
    @RequestMapping(value = "/userRoles", method = RequestMethod.GET)
    public List<UserRole> getUserRoles() throws AttendeeException {
        return lookupService.getUserRoles();
    }

    @RequestMapping(value = "/designations", method = RequestMethod.GET)
    public List<Designation> getDesignations() throws AttendeeException {
        return lookupService.getDesignations();
    }

    @RequestMapping(value = "/countries", method = RequestMethod.GET)
    public List<Country> getCountries() throws AttendeeException {
        return lookupService.getCountries();
    }

    @RequestMapping(value = "/permissions", method = RequestMethod.GET)
    public List<UserPermission> getPermissions() throws AttendeeException {
        return lookupService.getPermissions();
    }

    @RequestMapping(value = "/timezones", method = RequestMethod.GET)
    public List<Timezone> getTimezones() throws AttendeeException {
        return lookupService.getTimezones();
    }
}

