/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.services;

import com.wiproevents.entities.answers.UserPollAnswer;
import com.wiproevents.entities.criteria.UserPollAnswerSearchCriteria;

/**
 * The user poll answer service.Implementation should be effectively thread-safe.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public interface UserPollAnswerService extends GenericService<UserPollAnswer, UserPollAnswerSearchCriteria> {
}

