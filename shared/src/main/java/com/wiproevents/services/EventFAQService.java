package com.wiproevents.services;

import com.wiproevents.entities.EventFAQ;
import com.wiproevents.entities.criteria.EventFAQSearchCriteria;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.exceptions.EntityNotFoundException;

/**
 * The user service. Extends generic service interface.Implementation should be effectively thread-safe.
 */
public interface EventFAQService extends GenericService<EventFAQ, EventFAQSearchCriteria> {
    /**
     * This method is used to retrieve an entity.
     *
     * @param eventId the id of the entity to retrieve
     * @return the match entity
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws AttendeeException if any other error occurred during operation
     */
    EventFAQ getByEventId(String eventId) throws AttendeeException;

    /**
     * This method is used to create an entity.
     *
     * @param entity the entity to create
     * @return the created entity
     * @throws IllegalArgumentException if entity is null or not valid
     * @throws AttendeeException if any other error occurred during operation
     */
    EventFAQ createByEventId(String eventId, EventFAQ entity) throws AttendeeException;

    /**
     * This method is used to update an entity.
     *
     * @param id the id of the entity to update
     * @param entity the entity to update
     * @return the updated entity
     * @throws IllegalArgumentException if id is not positive or entity is null or id of entity is not positive
     *                                  or id of  entity not match id or entity is invalid
     * @throws EntityNotFoundException if the entity does not exist
     * @throws AttendeeException if any other error occurred during operation
     */
    EventFAQ updateByEventId(String eventId, EventFAQ entity) throws AttendeeException;

    /**
     * This method is used to delete an entity.
     *
     * @param eventId the id of the entity to delete
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws AttendeeException if any other error occurred during operation
     */
    void deleteByEventId(String eventId)throws AttendeeException;
}

