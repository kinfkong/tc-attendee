/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.services.springdata;

import com.wiproevents.entities.Poll;
import com.wiproevents.entities.criteria.PollSearchCriteria;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.PollService;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of PollService,
 * extends BaseService<Poll, PollSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class PollServiceImpl
        extends BaseService<Poll, PollSearchCriteria> implements PollService {

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws AttendeeException if any other error occurred during operation
     */
    @Override
    protected DocumentDbSpecification<Poll> getSpecification(PollSearchCriteria criteria)
            throws AttendeeException {
        return new PollSpecification(criteria);
    }
}

