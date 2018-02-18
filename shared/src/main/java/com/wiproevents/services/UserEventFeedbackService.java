/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.services;

import com.wiproevents.entities.UserEventFeedback;
import com.wiproevents.entities.criteria.UserEventFeedbackSearchCriteria;

/**
 * The user event feedback service.Implementation should be effectively thread-safe.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public interface UserEventFeedbackService extends GenericService<UserEventFeedback, UserEventFeedbackSearchCriteria> {
}

