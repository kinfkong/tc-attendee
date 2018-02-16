package com.wiproevents.services;

import com.wiproevents.entities.types.EventType;
import com.wiproevents.entities.criteria.EventTypeSearchCriteria;

/**
 * The lookup service.Implementation should be effectively thread-safe.
 */
public interface EventTypeService extends GenericService<EventType, EventTypeSearchCriteria> {
}

