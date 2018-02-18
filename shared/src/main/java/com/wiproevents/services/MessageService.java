/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.services;

import com.wiproevents.entities.Message;
import com.wiproevents.entities.criteria.MessageSearchCriteria;

/**
 * The message service.Implementation should be effectively thread-safe.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public interface MessageService extends GenericService<Message, MessageSearchCriteria> {
}

