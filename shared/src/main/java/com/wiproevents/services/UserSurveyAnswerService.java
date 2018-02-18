/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.services;

import com.wiproevents.entities.answers.UserSurveyAnswer;
import com.wiproevents.entities.criteria.UserSurveyAnswerSearchCriteria;

/**
 * The user survey answer service.Implementation should be effectively thread-safe.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public interface UserSurveyAnswerService extends GenericService<UserSurveyAnswer, UserSurveyAnswerSearchCriteria> {
}

