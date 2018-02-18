/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.services;

import com.wiproevents.entities.answers.UserQuizAnswer;
import com.wiproevents.entities.criteria.UserQuizAnswerSearchCriteria;

/**
 * The user quiz answer service.Implementation should be effectively thread-safe.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public interface UserQuizAnswerService extends GenericService<UserQuizAnswer, UserQuizAnswerSearchCriteria> {
}

