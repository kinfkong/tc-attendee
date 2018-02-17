/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.services.springdata;

import com.wiproevents.entities.Quiz;
import com.wiproevents.entities.criteria.QuizSearchCriteria;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.QuizService;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of QuizService,
 * extends BaseService<Quiz, QuizSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class QuizServiceImpl
        extends BaseService<Quiz, QuizSearchCriteria> implements QuizService {

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws AttendeeException if any other error occurred during operation
     */
    @Override
    protected DocumentDbSpecification<Quiz> getSpecification(QuizSearchCriteria criteria)
            throws AttendeeException {
        return new QuizSpecification(criteria);
    }
}

