package com.wiproevents.services;

import com.wiproevents.entities.IdentifiableEntity;
import com.wiproevents.utils.springdata.extensions.Paging;
import com.wiproevents.utils.springdata.extensions.SearchResult;
import com.wiproevents.exceptions.EntityNotFoundException;
import com.wiproevents.exceptions.AttendeeException;

/**
 * The generic service. Served as a base interface for CRUD operations.
 * @param <T> the entity
 * @param <S> the search criteria
 */
public interface GenericService<T extends IdentifiableEntity, S> {
    /**
     * This method is used to retrieve an entity.
     *
     * @param id the id of the entity to retrieve
     * @return the match entity
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws AttendeeException if any other error occurred during operation
     */
    T get(String id) throws AttendeeException;

    /**
     * This method is used to create an entity.
     *
     * @param entity the entity to create
     * @return the created entity
     * @throws IllegalArgumentException if entity is null or not valid
     * @throws AttendeeException if any other error occurred during operation
     */
     T create(T entity) throws AttendeeException;

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
    T update(String id, T entity) throws AttendeeException;

    /**
     * This method is used to delete an entity.
     *
     * @param id the id of the entity to delete
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws AttendeeException if any other error occurred during operation
     */
     void delete(String id)throws AttendeeException;

    /**
     * This method is used to search for entities by criteria and paging params.
     *
     * @param criteria the search criteria
     * @param paging the paging data
     * @return the search result
     * @throws IllegalArgumentException if pageSize is not positive or pageNumber is negative
     * @throws AttendeeException if any other error occurred during operation
     */
     SearchResult<T> search(S criteria, Paging paging) throws AttendeeException;

    /**
     * This method is used to get total count for search results of entities with criteria.
     *
     * @param criteria the search criteria
     * @return the total count of search result
     * @throws IllegalArgumentException if pageSize is not positive or pageNumber is negative
     * @throws AttendeeException if any other error occurred during operation
     */
    long count(S criteria) throws AttendeeException;
}

