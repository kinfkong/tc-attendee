/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.services;

import com.wiproevents.entities.UserNote;
import com.wiproevents.entities.criteria.UserNoteSearchCriteria;

/**
 * The user not service.Implementation should be effectively thread-safe.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public interface UserNoteService extends GenericService<UserNote, UserNoteSearchCriteria> {
}

