package com.wiproevents.services.springdata;

import com.wiproevents.entities.EventWallPost;
import com.wiproevents.entities.criteria.EventWallPostSearchCriteria;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.EventWallPostService;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of UserService,
 * extends BaseService<User,UserSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class EventWallPostServiceImpl extends BaseService<EventWallPost, EventWallPostSearchCriteria> implements EventWallPostService {

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws AttendeeException if any other error occurred during operation
     */
    protected DocumentDbSpecification<EventWallPost> getSpecification(EventWallPostSearchCriteria criteria) throws AttendeeException {
        return new EventWallPostSpecification(criteria);
    }
}

