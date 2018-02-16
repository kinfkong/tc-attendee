/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.services.springdata;

import com.wiproevents.entities.EventCategory;
import com.wiproevents.entities.criteria.EventCategorySearchCriteria;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.EventCategoryService;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of EventCategoryService,
 * extends BaseService<EventCategory, EventCategorySearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class EventCategoryServiceImpl
        extends BaseService<EventCategory, EventCategorySearchCriteria> implements EventCategoryService {

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws AttendeeException if any other error occurred during operation
     */
    @Override
    protected DocumentDbSpecification<EventCategory> getSpecification(EventCategorySearchCriteria criteria)
            throws AttendeeException {
        return new EventCategorySpecification(criteria);
    }
}

