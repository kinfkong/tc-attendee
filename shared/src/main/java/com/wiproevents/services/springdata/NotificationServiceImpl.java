/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.services.springdata;

import com.wiproevents.entities.Notification;
import com.wiproevents.entities.criteria.NotificationSearchCriteria;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.NotificationService;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of NotificationService,
 * extends BaseService<Notification, NotificationSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class NotificationServiceImpl
        extends BaseService<Notification, NotificationSearchCriteria> implements NotificationService {

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws AttendeeException if any other error occurred during operation
     */
    @Override
    protected DocumentDbSpecification<Notification> getSpecification(NotificationSearchCriteria criteria)
            throws AttendeeException {
        return new NotificationSpecification(criteria);
    }
}

