package com.wiproevents.services.springdata;

import com.wiproevents.entities.Session;
import com.wiproevents.entities.criteria.SessionSearchCriteria;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.SessionService;
import com.wiproevents.utils.Helper;
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

    @Override
    protected void handleNestedCreate(Session entity) throws AttendeeException {
        super.handleNestedCreate(entity);
        Helper.updateAudition(entity.getAddedSpeakers(), null);
    }

    @Override
    protected void handleNestedUpdate(Session entity, Session oldEntity) throws AttendeeException {
        super.handleNestedUpdate(entity, oldEntity);
        Helper.updateAudition(entity.getAddedSpeakers(), oldEntity.getAddedSpeakers());
    }
}

