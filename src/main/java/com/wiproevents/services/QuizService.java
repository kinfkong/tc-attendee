/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.services;

import com.wiproevents.entities.Quiz;
import com.wiproevents.entities.criteria.QuizSearchCriteria;

/**
 * The quiz service.Implementation should be effectively thread-safe.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public interface QuizService extends GenericService<Quiz, QuizSearchCriteria> {
}

