/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.services.springdata;

import com.wiproevents.entities.Notification;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;
import org.springframework.stereotype.Repository;

/**
 * The notification repository.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Repository
public interface NotificationRepository extends DocumentDbSpecificationRepository<Notification, String> {
}

