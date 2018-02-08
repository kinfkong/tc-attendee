package com.wiproevents.services.springdata;

import com.wiproevents.entities.EventCategory;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;
import org.springframework.stereotype.Repository;

/**
 * The User repository.
 */
@Repository
public interface EventCategoryRepository extends DocumentDbSpecificationRepository<EventCategory, String> {
}

