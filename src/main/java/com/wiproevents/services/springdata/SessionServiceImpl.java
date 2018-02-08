package com.wiproevents.services.springdata;

import com.wiproevents.entities.Session;
import com.wiproevents.entities.SessionSearchCriteria;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.SessionService;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of UserService,
 * extends BaseService<User,UserSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class SessionServiceImpl extends BaseService<Session, SessionSearchCriteria> implements SessionService {

    @Override
    protected DocumentDbSpecification<Session> getSpecification(SessionSearchCriteria criteria) throws AttendeeException {
        return new SessionSpecification(criteria);
    }

}

