package com.wiproevents.services.springdata;

import com.wiproevents.entities.EventFAQ;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;
import org.springframework.stereotype.Repository;

/**
 * The User repository.
 */
@Repository
public interface EventFAQRepository extends DocumentDbSpecificationRepository<EventFAQ, String> {
}

