package com.wiproevents.services;

import com.wiproevents.entities.Event;
import com.wiproevents.entities.EventSearchCriteria;

/**
 * The lookup service.Implementation should be effectively thread-safe.
 */
public interface EventService extends GenericService<Event, EventSearchCriteria> {
}

