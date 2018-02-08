package com.wiproevents.services.springdata;

import com.microsoft.azure.spring.data.documentdb.core.query.Query;
import com.wiproevents.entities.EventDayAgenda;
import com.wiproevents.entities.EventDayAgendaSearchCriteria;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * The specification used to query User by criteria.
 */
@AllArgsConstructor
public class EventDayAgendaSpecification implements DocumentDbSpecification<EventDayAgenda> {
    /**
     * The criteria. Final.
     */
    private final EventDayAgendaSearchCriteria criteria;


    @Override
    public Query toQuery(Query query, Map<String, Object> values) {
        return query;
    }
}

