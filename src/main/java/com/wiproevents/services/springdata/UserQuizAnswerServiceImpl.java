/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.services.springdata;

import com.wiproevents.entities.answers.UserQuizAnswer;
import com.wiproevents.entities.criteria.UserQuizAnswerSearchCriteria;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.UserQuizAnswerService;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of UserQuizAnswerService,
 * extends BaseService<UserQuizAnswer, UserQuizAnswerSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class UserQuizAnswerServiceImpl
        extends BaseService<UserQuizAnswer, UserQuizAnswerSearchCriteria> implements UserQuizAnswerService {

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws AttendeeException if any other error occurred during operation
     */
    @Override
    protected DocumentDbSpecification<UserQuizAnswer> getSpecification(UserQuizAnswerSearchCriteria criteria)
            throws AttendeeException {
        return new UserQuizAnswerSpecification(criteria);
    }
}

