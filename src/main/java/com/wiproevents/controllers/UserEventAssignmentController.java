package com.wiproevents.controllers;

import com.wiproevents.entities.UserEventAssignment;
import com.wiproevents.entities.criteria.UserEventAssignmentSearchCriteria;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.UserEventAssignmentService;
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
@RequestMapping("/userEventAssignments")
@NoArgsConstructor
public class UserEventAssignmentController {
    /**
     * The service used to perform operations. Should be non-null after injection.
     */
    @Autowired
    private UserEventAssignmentService userEventAssignmentService;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(userEventAssignmentService, "userEventAssignmentService");
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
    public UserEventAssignment get(@PathVariable String id) throws AttendeeException {
        return userEventAssignmentService.get(id);
    }

    /**
     * This method is used to create an entity.
     *
     * @param entity the entity to create
     * @param documents the documents
     * @return the created entity
     * @throws IllegalArgumentException if entity is null or not valid
     * @throws AttendeeException if any other error occurred during operation
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public UserEventAssignment create(@RequestBody UserEventAssignment entity) throws AttendeeException  {
        return userEventAssignmentService.create(entity);
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
    public UserEventAssignment update(@PathVariable String id, @RequestBody UserEventAssignment entity) throws AttendeeException  {
        return userEventAssignmentService.update(id, entity);
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
        userEventAssignmentService.delete(id);
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
    public SearchResult<UserEventAssignment> search(@ModelAttribute UserEventAssignmentSearchCriteria criteria,
                                        @ModelAttribute Paging paging) throws AttendeeException  {
        return userEventAssignmentService.search(criteria, paging);
    }
}

