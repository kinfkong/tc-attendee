/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.services;

import com.wiproevents.entities.Conversation;
import com.wiproevents.entities.criteria.ConversationSearchCriteria;

/**
 * The conversation service.Implementation should be effectively thread-safe.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public interface ConversationService extends GenericService<Conversation, ConversationSearchCriteria> {
}

