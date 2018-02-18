/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.services;

import com.wiproevents.entities.Notification;
import com.wiproevents.entities.criteria.NotificationSearchCriteria;

/**
 * The notification service.Implementation should be effectively thread-safe.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public interface NotificationService extends GenericService<Notification, NotificationSearchCriteria> {
}

