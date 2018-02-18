package com.wiproevents.services.springdata;

import com.wiproevents.entities.EventFAQ;
import com.wiproevents.entities.criteria.EventFAQSearchCriteria;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.exceptions.EntityNotFoundException;
import com.wiproevents.services.EventFAQService;
import com.wiproevents.utils.Helper;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The Spring Data JPA implementation of UserService,
 * extends BaseService<User,UserSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class EventFAQServiceImpl extends BaseService<EventFAQ, EventFAQSearchCriteria> implements EventFAQService {

    @Override
    public EventFAQ getByEventId(String eventId) throws AttendeeException {
        Helper.checkNullOrEmpty(eventId, "eventId");
        EventFAQSearchCriteria criteria = new EventFAQSearchCriteria();
        criteria.setEventId(eventId);
        List<EventFAQ> result = super.search(criteria, null).getEntities();
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public EventFAQ createByEventId(String eventId, EventFAQ entity) throws AttendeeException {
        Helper.checkNullOrEmpty(eventId, "eventId");
        Helper.checkNull(entity, "entity");
        // check existence
        EventFAQ eventFAQ = getByEventId(eventId);
        if (eventFAQ != null) {
            throw new IllegalArgumentException("event faq for event: " + eventId + " already exists.");
        }
        entity.setEventId(eventId);

        return create(entity);
    }

    @Override
    public EventFAQ updateByEventId(String eventId, EventFAQ entity) throws AttendeeException {
        Helper.checkNullOrEmpty(eventId, "eventId");
        Helper.checkNull(entity, "entity");
        // check existence
        EventFAQ eventFAQ = getByEventId(eventId);
        if (eventFAQ == null) {
            throw new EntityNotFoundException("event faq for event: " + eventId + " does not exist.");
        }
        return update(eventFAQ.getId(), eventFAQ);
    }

    @Override
    public void deleteByEventId(String eventId) throws AttendeeException {
        Helper.checkNullOrEmpty(eventId, "eventId");
        // check existence
        EventFAQ eventFAQ = getByEventId(eventId);
        if (eventFAQ == null) {
            throw new EntityNotFoundException("event faq for event: " + eventId + " does not exist.");
        }

        delete(eventFAQ.getId());
    }

    @Override
    protected DocumentDbSpecification<EventFAQ> getSpecification(EventFAQSearchCriteria criteria) throws AttendeeException {
        return new EventFAQSpecification(criteria);
    }


}

