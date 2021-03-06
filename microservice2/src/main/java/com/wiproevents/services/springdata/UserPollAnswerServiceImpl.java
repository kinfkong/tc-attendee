/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.services.springdata;

import com.wiproevents.entities.answers.UserPollAnswer;
import com.wiproevents.entities.criteria.UserPollAnswerSearchCriteria;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.UserPollAnswerService;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of UserPollAnswerService,
 * extends BaseService<UserPollAnswer, UserPollAnswerSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class UserPollAnswerServiceImpl
        extends BaseService<UserPollAnswer, UserPollAnswerSearchCriteria> implements UserPollAnswerService {

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws AttendeeException if any other error occurred during operation
     */
    @Override
    protected DocumentDbSpecification<UserPollAnswer> getSpecification(UserPollAnswerSearchCriteria criteria)
            throws AttendeeException {
        return new UserPollAnswerSpecification(criteria);
    }
}

