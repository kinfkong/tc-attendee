package com.wiproevents.controllers;

import com.wiproevents.entities.Session;
import com.wiproevents.entities.UserRole;
import com.wiproevents.entities.UserRoleSearchCriteria;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.UserRoleService;
import com.wiproevents.utils.Helper;
import com.wiproevents.utils.springdata.extensions.Paging;
import com.wiproevents.utils.springdata.extensions.SearchResult;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

/**
 * The Task REST controller. Is effectively thread safe.
 */
@RestController
@RequestMapping("/userRoles")
@NoArgsConstructor
public class UserRoleController {
    /**
     * The service used to perform operations. Should be non-null after injection.
     */
    @Autowired
    private UserRoleService userRoleService;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(userRoleService, "userRoleService");
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
    public UserRole get(@PathVariable String id) throws AttendeeException {
        return userRoleService.get(id);
    }

    /**
     * This method is used to create an entity.
     *
     * @param entity the entity to create
     * @return the created entity
     * @throws IllegalArgumentException if entity is null or not valid
     * @throws AttendeeException if any other error occurred during operation
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public UserRole create(@RequestBody UserRole entity) throws AttendeeException  {
        return userRoleService.create(entity);
    }

    /**
     * This method is used to update an entity.
     *
     * @param id the id of the entity to update
     * @param entity the entity to update
     * @param documents the documents to upload
     * @return the updated entity
     * @throws IllegalArgumentException if id is not positive or entity is null or id of entity is not positive
     * or id of  entity not match id or entity is invalid
     * @throws EntityNotFoundException if the entity does not exist
     * @throws AttendeeException if any other error occurred during operation
     */
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @Transactional
    public UserRole update(@PathVariable String id, @RequestBody UserRole entity) throws AttendeeException  {
        return userRoleService.update(id, entity);
    }

    /**
     * This method is used to delete an entity.
     *
     * @param id the id of the entity to delete
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws AttendeeException if any other error occurred during operation
     */
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @Transactional
    public void delete(@PathVariable String id) throws AttendeeException  {
        userRoleService.delete(id);
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
    public SearchResult<UserRole> search(@ModelAttribute UserRoleSearchCriteria criteria,
                                        @ModelAttribute Paging paging) throws AttendeeException  {
        return userRoleService.search(criteria, paging);
    }
}

