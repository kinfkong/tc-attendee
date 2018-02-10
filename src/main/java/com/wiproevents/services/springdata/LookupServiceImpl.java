package com.wiproevents.services.springdata;

import com.wiproevents.entities.Country;
import com.wiproevents.entities.Designation;
import com.wiproevents.entities.UserPermission;
import com.wiproevents.entities.UserRole;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.exceptions.ConfigurationException;
import com.wiproevents.services.LookupService;
import com.wiproevents.utils.Helper;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Spring Data JPA implementation of LookupService. Is effectively thread safe.
 */
@Service
@NoArgsConstructor
public class LookupServiceImpl implements LookupService {

    /**
     * The user role repository for UserRole CRUD operations. Should be non-null after injection.
     */
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private UserPermissionRepository permisionRepository;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(userRoleRepository, "userRoleRepository");
    }

    /**
     * This method is used to get user role lookups.
     *
     * @return the lookups for user role.
     * @throws AttendeeException if any other error occurred during operation
     */
    public List<UserRole> getUserRoles() throws AttendeeException {
        List<UserRole> list = new ArrayList<>();
        userRoleRepository.findAll().forEach(list::add);
        return list;
    }

    @Override
    public List<Designation> getDesignations() throws AttendeeException {
        List<Designation> list = new ArrayList<>();
        designationRepository.findAll().forEach(list::add);
        return list;
    }

    @Override
    public List<Country> getCountries() throws AttendeeException {
        List<Country> list = new ArrayList<>();
        countryRepository.findAll().forEach(list::add);
        return list;
    }

    @Override
    public List<UserPermission> getPermissions() throws AttendeeException {
        List<UserPermission> list = new ArrayList<>();
        permisionRepository.findAll().forEach(list::add);
        return list;
    }
}

