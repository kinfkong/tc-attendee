/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.services.springdata;

import com.wiproevents.entities.Message;
import com.wiproevents.entities.criteria.MessageSearchCriteria;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.MessageService;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of MessageService,
 * extends BaseService<Message, MessageSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class MessageServiceImpl
        extends BaseService<Message, MessageSearchCriteria> implements MessageService {

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws AttendeeException if any other error occurred during operation
     */
    @Override
    protected DocumentDbSpecification<Message> getSpecification(MessageSearchCriteria criteria)
            throws AttendeeException {
        return new MessageSpecification(criteria);
    }
}

