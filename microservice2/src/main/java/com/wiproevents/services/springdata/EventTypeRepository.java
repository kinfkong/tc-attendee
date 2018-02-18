package com.wiproevents.services.springdata;

import com.wiproevents.entities.types.EventType;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;
import org.springframework.stereotype.Repository;

/**
 * The User repository.
 */
@Repository
public interface EventTypeRepository extends DocumentDbSpecificationRepository<EventType, String> {
}

