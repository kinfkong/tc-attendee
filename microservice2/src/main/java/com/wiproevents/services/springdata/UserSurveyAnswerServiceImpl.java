/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.services.springdata;

import com.wiproevents.entities.answers.UserSurveyAnswer;
import com.wiproevents.entities.criteria.UserSurveyAnswerSearchCriteria;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.UserSurveyAnswerService;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of UserSurveyAnswerService,
 * extends BaseService<UserSurveyAnswer, UserSurveyAnswerSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class UserSurveyAnswerServiceImpl
        extends BaseService<UserSurveyAnswer, UserSurveyAnswerSearchCriteria> implements UserSurveyAnswerService {

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws AttendeeException if any other error occurred during operation
     */
    @Override
    protected DocumentDbSpecification<UserSurveyAnswer> getSpecification(UserSurveyAnswerSearchCriteria criteria)
            throws AttendeeException {
        return new UserSurveyAnswerSpecification(criteria);
    }
}

