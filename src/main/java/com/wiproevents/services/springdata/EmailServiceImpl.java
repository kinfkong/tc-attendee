/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.services.springdata;

import com.wiproevents.entities.Email;
import com.wiproevents.entities.criteria.EmailSearchCriteria;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.EmailService;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of EmailService,
 * extends BaseService<Email, EmailSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class EmailServiceImpl
        extends BaseService<Email, EmailSearchCriteria> implements EmailService {

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws AttendeeException if any other error occurred during operation
     */
    @Override
    protected DocumentDbSpecification<Email> getSpecification(EmailSearchCriteria criteria)
            throws AttendeeException {
        return new EmailSpecification(criteria);
    }
}

