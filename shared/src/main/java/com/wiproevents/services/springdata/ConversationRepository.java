/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.services.springdata;

import com.wiproevents.entities.Conversation;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;
import org.springframework.stereotype.Repository;

/**
 * The conversation repository.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Repository
public interface ConversationRepository extends DocumentDbSpecificationRepository<Conversation, String> {
}

