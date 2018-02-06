package com.wiproevents.services.springdata;

import com.wiproevents.entities.EventType;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * The User repository.
 */
@Repository
public interface EventTypeRepository extends DocumentDbSpecificationExecutor<EventType, String> {
}

