package com.wiproevents.services.springdata;

import com.wiproevents.entities.SessionAttendee;
import com.wiproevents.entities.SessionAttendeeSearchCriteria;
import com.wiproevents.entities.SessionAttendeeStatus;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.SessionAttendeeService;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of UserService,
 * extends BaseService<User,UserSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class SessionAttendeeServiceImpl extends BaseService<SessionAttendee, SessionAttendeeSearchCriteria> implements SessionAttendeeService {

    @Override
    protected DocumentDbSpecification<SessionAttendee> getSpecification(SessionAttendeeSearchCriteria criteria) throws AttendeeException {
        return new SessionAttendeeSpecification(criteria);
    }

    @Override
    protected void handleNestedCreate(SessionAttendee entity) throws AttendeeException {
        super.handleNestedCreate(entity);
        entity.setStatus(SessionAttendeeStatus.Waiting);
    }
}

