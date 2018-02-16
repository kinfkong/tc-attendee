package com.wiproevents.services.springdata;

import com.microsoft.azure.spring.data.documentdb.core.query.Query;
import com.wiproevents.entities.criteria.BaseSearchCriteria;
import com.wiproevents.entities.UserPreference;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * The specification used to query User by criteria.
 */
@AllArgsConstructor
public class UserPreferenceSpecification implements DocumentDbSpecification<UserPreference> {
    /**
     * The criteria. Final.
     */
    private final BaseSearchCriteria criteria;


    @Override
    public Query toQuery(Query query, Map<String, Object> values) {
        return query;
    }
}

