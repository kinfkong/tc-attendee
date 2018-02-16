/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.services;

import com.wiproevents.entities.EventCategory;
import com.wiproevents.entities.criteria.EventCategorySearchCriteria;

/**
 * The event category service.Implementation should be effectively thread-safe.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public interface EventCategoryService extends GenericService<EventCategory, EventCategorySearchCriteria> {
}

