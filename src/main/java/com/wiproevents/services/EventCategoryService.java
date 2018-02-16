package com.wiproevents.services;

import com.wiproevents.entities.EventCategory;
import com.wiproevents.entities.criteria.EventCategorySearchCriteria;

/**
 * The lookup service.Implementation should be effectively thread-safe.
 */
public interface EventCategoryService extends GenericService<EventCategory, EventCategorySearchCriteria> {
}

