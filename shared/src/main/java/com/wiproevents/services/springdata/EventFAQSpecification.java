package com.wiproevents.services.springdata;

import com.microsoft.azure.spring.data.documentdb.core.query.Query;
import com.wiproevents.entities.EventFAQ;
import com.wiproevents.entities.criteria.EventFAQSearchCriteria;
import com.wiproevents.utils.Helper;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * The specification used to query User by criteria.
 */
@AllArgsConstructor
public class EventFAQSpecification implements DocumentDbSpecification<EventFAQ> {
    /**
     * The criteria. Final.
     */
    private final EventFAQSearchCriteria criteria;


    @Override
    public Query toQuery(Query query, Map<String, Object> values) {
        Helper.buildEqualPredict(query, values, "eventId", criteria.getEventId());
        return query;
    }
}

