package com.wiproevents.services.springdata;

import com.wiproevents.entities.EventType;
import com.wiproevents.entities.EventTypeSearchCriteria;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.EventTypeService;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of UserService,
 * extends BaseService<User,UserSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class EventTypeServiceImpl extends BaseService<EventType, EventTypeSearchCriteria> implements EventTypeService {

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws AttendeeException if any other error occurred during operation
     */
    protected DocumentDbSpecification<EventType> getSpecification(EventTypeSearchCriteria criteria) throws AttendeeException {
        return new EventTypeSpecification(criteria);
    }
}

