package com.wiproevents.services.springdata;

import com.wiproevents.entities.EventDayAgenda;
import com.wiproevents.entities.EventDayAgendaSearchCriteria;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.EventDayAgendaService;
import com.wiproevents.utils.Helper;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of UserService,
 * extends BaseService<User,UserSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class EventDayAgendaServiceImpl extends BaseService<EventDayAgenda, EventDayAgendaSearchCriteria> implements EventDayAgendaService {

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws AttendeeException if any other error occurred during operation
     */
    protected DocumentDbSpecification<EventDayAgenda> getSpecification(EventDayAgendaSearchCriteria criteria) throws AttendeeException {
        return new EventDayAgendaSpecification(criteria);
    }

    @Override
    protected void handleNestedCreate(EventDayAgenda entity) throws AttendeeException {
        super.handleNestedCreate(entity);
        Helper.updateAudition(entity.getBreaks(), null);
    }

    @Override
    protected void handleNestedUpdate(EventDayAgenda entity, EventDayAgenda oldEntity) throws AttendeeException {
        super.handleNestedUpdate(entity, oldEntity);
        Helper.updateAudition(entity.getBreaks(), oldEntity.getBreaks());
    }

}

