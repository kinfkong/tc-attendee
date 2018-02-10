package com.wiproevents.services.springdata;

import com.wiproevents.entities.*;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.EventDayAgendaService;
import com.wiproevents.services.EventService;
import com.wiproevents.services.SessionService;
import com.wiproevents.utils.Helper;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import com.wiproevents.utils.springdata.extensions.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Spring Data JPA implementation of UserService,
 * extends BaseService<User,UserSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class EventServiceImpl extends BaseService<Event, EventSearchCriteria> implements EventService {

    @Autowired
    private EventDayAgendaService dayAgendaService;

    @Autowired
    private SessionAttendeeRepository sessionAttendeeRepository;

    @Autowired
    private SessionService sessionService;

    @Override
    public EventStatistics calculateDashboard(String id) throws AttendeeException {
        Event e = get(id);
        EventStatistics es = new EventStatistics();
        es.setEventId(e.getId());
        es.setEventName(e.getName());
        es.setNumberOfApprovedAttendees(0);
        es.setNumberOfAttendees(0);
        es.setNumberOfRejectedAttendees(0);
        es.setNumberOfSessions(0);
        es.setNumberOfSpeakers(0);
        es.setNumberOfTicketsLefts(0);

        List<SessionStatistics> sss = new ArrayList<>();

        int sessionsCount = 0;
        int speakersCount = 0;

        for (EventDayAgenda d : e.getDayAgendas()) {
            for (Session s : d.getSessions()) {
                sessionsCount += 1;
                SessionStatistics ss = new SessionStatistics();
                ss.setId(s.getId());
                ss.setActivityStatus(s.getStatus());
                ss.setEndTime(s.getEndTime());
                ss.setStartTime(s.getStartTime());
                EventBrief eb = new EventBrief();
                eb.setId(e.getId());
                eb.setEndDate(e.getEndDate());
                eb.setStartDate(e.getStartDate());
                eb.setName(e.getName());
                ss.setEvent(eb);
                int registeredCount = 0;
                int waitingCount = 0;
                // get the attendees for the session
                SessionAttendeeSearchCriteria criteria = new SessionAttendeeSearchCriteria();
                criteria.setSessionId(s.getId());
                SearchResult<SessionAttendee> attendees =
                        sessionAttendeeRepository.findAll(new SessionAttendeeSpecification(criteria), null);
                for (SessionAttendee sa : attendees.getEntities()) {
                    if (sa.getStatus() == SessionAttendeeStatus.Deleted) {
                        continue;
                    }
                    registeredCount += 1;
                    if (sa.getStatus() == SessionAttendeeStatus.Waiting) {
                        waitingCount += 1;
                    }
                }
                ss.setNumberOfRegistered(registeredCount);
                ss.setNumberOfWaitlisted(waitingCount);
                List<String> speakers = new ArrayList<>();
                for (User speaker : s.getAssignedSpeakers()) {
                    speakers.add(speaker.getFullName());
                    speakersCount += 1;
                }
                for (Speaker speaker: s.getAddedSpeakers()) {
                    speakers.add(speaker.getName());
                    speakersCount += 1;
                }
                ss.setSpeakers(speakers);

                sss.add(ss);
            }
        }

        es.setNumberOfSessions(sessionsCount);
        es.setNumberOfSpeakers(speakersCount);

        es.setSessions(sss);
        return es;
    }

    @Override
    public List<Session> getSessions(String id) throws AttendeeException {
        List<EventDayAgenda> agendas = getDayAgendas(id);
        List<Session> result = new ArrayList<>();
        agendas.forEach(a -> {
            a.getSessions().forEach(result::add);
        });
        return result;
    }

    @Override
    public List<EventDayAgenda> getDayAgendas(String id) throws AttendeeException {
        EventDayAgendaSearchCriteria criteria = new EventDayAgendaSearchCriteria();
        criteria.setEventId(id);
        return dayAgendaService.search(criteria, null).getEntities();
    }


    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws AttendeeException if any other error occurred during operation
     */
    protected DocumentDbSpecification<Event> getSpecification(EventSearchCriteria criteria) throws AttendeeException {
        return new EventSpecification(criteria);
    }


    @Override
    protected void handlePopulate(Event entity) throws AttendeeException {
        super.handlePopulate(entity);

        EventDayAgendaSearchCriteria criteria = new EventDayAgendaSearchCriteria();
        criteria.setEventId(entity.getId());
        SearchResult<EventDayAgenda> dayAgendaResult = dayAgendaService.search(criteria, null);
        entity.setDayAgendas(dayAgendaResult.getEntities());

    }

    @Override
    protected void handleNestedUpdate(Event entity, Event oldEntity) throws AttendeeException {
        // handle location
        handleNested(entity);

        Location loc = entity.getLocation();

        String userId = Helper.getAuthUser().getId();

        if (loc != null) {
            Location oldLoc = oldEntity.getLocation();
            if (oldLoc != null) {
                loc.setCreatedBy(oldLoc.getCreatedBy());
                loc.setCreatedOn(oldLoc.getCreatedOn());
                if (Helper.isUpdated(loc, oldLoc)) {
                    loc.setUpdatedOn(new Date());
                    loc.setUpdatedBy(userId);
                } else {
                    loc.setUpdatedBy(oldLoc.getUpdatedBy());
                    loc.setUpdatedOn(oldLoc.getUpdatedOn());
                }
            } else {
                loc.setCreatedBy(userId);
                loc.setCreatedOn(new Date());

                loc.setUpdatedBy(userId);
                loc.setUpdatedOn(new Date());
            }
        }
    }

    @Override
    protected void handleNestedCreate(Event entity) throws AttendeeException {
        // handle location
        handleNested(entity);

        Location loc = entity.getLocation();
        String userId = Helper.getAuthUser().getId();
        if (loc != null) {
            loc.setCreatedBy(userId);
            loc.setCreatedOn(new Date());
            loc.setUpdatedBy(userId);
            loc.setUpdatedOn(new Date());
        }
    }

    private void handleNested(Event entity) throws AttendeeException {
        if (entity.getInvitations() != null) {
            for (EventInvitation eventInvitation : entity.getInvitations()) {
                eventInvitation.setEventId(entity.getId());
            }
        }
    }
}

