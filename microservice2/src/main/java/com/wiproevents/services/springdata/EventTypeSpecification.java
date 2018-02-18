package com.wiproevents.services.springdata;

import com.microsoft.azure.spring.data.documentdb.core.query.Query;
import com.wiproevents.entities.types.EventType;
import com.wiproevents.entities.criteria.EventTypeSearchCriteria;
import com.wiproevents.utils.Helper;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * The specification used to query User by criteria.
 */
@AllArgsConstructor
public class EventTypeSpecification implements DocumentDbSpecification<EventType> {
    /**
     * The criteria. Final.
     */
    private final EventTypeSearchCriteria criteria;


    @Override
    public Query toQuery(Query query, Map<String, Object> values) {
        Helper.buildEqualPredict(query, values, "name", this.criteria.getName());
        return query;
    }
}

