package com.wiproevents.controllers;

import com.wiproevents.entities.SessionAttendee;
import com.wiproevents.entities.SessionAttendeeSearchCriteria;
import com.wiproevents.entities.SessionAttendeeStatus;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.SessionAttendeeService;
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
@RequestMapping("/sessionAttendees")
@NoArgsConstructor
public class SessionAttendeeController {
    /**
     * The service used to perform operations. Should be non-null after injection.
     */
    @Autowired
    private SessionAttendeeService sessionAttendeeService;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(sessionAttendeeService, "sessionAttendeeService");
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
    public SessionAttendee get(@PathVariable String id) throws AttendeeException {
        return sessionAttendeeService.get(id);
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
    public SessionAttendee create(@RequestBody SessionAttendee entity) throws AttendeeException  {
        return sessionAttendeeService.create(entity);
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
    public SessionAttendee update(@PathVariable String id, @RequestBody SessionAttendee entity) throws AttendeeException  {
        return sessionAttendeeService.update(id, entity);
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
        sessionAttendeeService.delete(id);
    }

    /**
     * This method is used to delete an entity.
     *
     * @param id the id of the entity to delete
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws AttendeeException if any other error occurred during operation
     */
    @RequestMapping(value = "{id}/delete", method = RequestMethod.PUT)
    @Transactional
    public void markDeleted(@PathVariable String id) throws AttendeeException  {
        SessionAttendee sa = sessionAttendeeService.get(id);
        sa.setStatus(SessionAttendeeStatus.Deleted);
        sessionAttendeeService.update(id, sa);
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
    public SearchResult<SessionAttendee> search(@ModelAttribute SessionAttendeeSearchCriteria criteria,
                                        @ModelAttribute Paging paging) throws AttendeeException  {
        return sessionAttendeeService.search(criteria, paging);
    }
}

