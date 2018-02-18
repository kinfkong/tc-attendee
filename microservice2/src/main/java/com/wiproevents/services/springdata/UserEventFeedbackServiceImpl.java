/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.services.springdata;

import com.wiproevents.entities.UserEventFeedback;
import com.wiproevents.entities.criteria.UserEventFeedbackSearchCriteria;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.UserEventFeedbackService;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of UserEventFeedbackService,
 * extends BaseService<UserEventFeedback, UserEventFeedbackSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class UserEventFeedbackServiceImpl
        extends BaseService<UserEventFeedback, UserEventFeedbackSearchCriteria> implements UserEventFeedbackService {

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws AttendeeException if any other error occurred during operation
     */
    @Override
    protected DocumentDbSpecification<UserEventFeedback> getSpecification(UserEventFeedbackSearchCriteria criteria)
            throws AttendeeException {
        return new UserEventFeedbackSpecification(criteria);
    }
}

