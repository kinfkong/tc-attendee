package com.wiproevents.services.springdata;

import com.microsoft.azure.spring.data.documentdb.core.query.Query;
import com.wiproevents.entities.EventCategory;
import com.wiproevents.entities.criteria.EventCategorySearchCriteria;
import com.wiproevents.utils.Helper;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * The specification used to query User by criteria.
 */
@AllArgsConstructor
public class EventCategorySpecification implements DocumentDbSpecification<EventCategory> {
    /**
     * The criteria. Final.
     */
    private final EventCategorySearchCriteria criteria;


    @Override
    public Query toQuery(Query query, Map<String, Object> values) {
        Helper.buildEqualPredict(query, values, "name", this.criteria.getName());
        return query;
    }
}

