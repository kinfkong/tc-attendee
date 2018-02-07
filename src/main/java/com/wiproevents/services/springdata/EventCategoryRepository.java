package com.wiproevents.services.springdata;

import com.wiproevents.entities.EventCategory;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * The User repository.
 */
@Repository
public interface EventCategoryRepository extends DocumentDbSpecificationExecutor<EventCategory, String> {
}

