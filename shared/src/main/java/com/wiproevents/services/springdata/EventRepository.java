package com.wiproevents.services.springdata;

import com.wiproevents.entities.Event;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;
import org.springframework.stereotype.Repository;

/**
 * The User repository.
 */
@Repository
public interface EventRepository extends DocumentDbSpecificationRepository<Event, String> {

}

