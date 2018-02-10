package com.wiproevents.services;

import com.wiproevents.entities.*;
import com.wiproevents.exceptions.AttendeeException;

import java.util.List;

/**
 * The lookup service.Implementation should be effectively thread-safe.
 */
public interface EventService extends GenericService<Event, EventSearchCriteria> {
    EventStatistics calculateDashboard(String id) throws AttendeeException;

    List<Session> getSessions(String id) throws AttendeeException;

    List<EventDayAgenda> getDayAgendas(String id) throws AttendeeException;
}

