/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.services.springdata;

import com.wiproevents.entities.UserNote;
import com.wiproevents.entities.criteria.UserNoteSearchCriteria;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.UserNoteService;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of UserNoteService,
 * extends BaseService<UserNote, UserNoteSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class UserNoteServiceImpl
        extends BaseService<UserNote, UserNoteSearchCriteria> implements UserNoteService {

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws AttendeeException if any other error occurred during operation
     */
    @Override
    protected DocumentDbSpecification<UserNote> getSpecification(UserNoteSearchCriteria criteria)
            throws AttendeeException {
        return new UserNoteSpecification(criteria);
    }
}

