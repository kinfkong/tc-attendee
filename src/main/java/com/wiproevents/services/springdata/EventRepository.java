package com.wiproevents.services.springdata;

import com.wiproevents.entities.Event;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * The User repository.
 */
@Repository
public interface EventRepository extends DocumentDbSpecificationExecutor<Event, String> {

}

