package com.wiproevents.services.springdata;

import com.microsoft.azure.spring.data.documentdb.core.query.Query;
import com.wiproevents.entities.UserEventAssignment;
import com.wiproevents.entities.UserEventAssignmentSearchCriteria;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * The specification used to query User by criteria.
 */
@AllArgsConstructor
public class UserEventAssigmentSpecification implements DocumentDbSpecification<UserEventAssignment> {
    /**
     * The criteria. Final.
     */
    private final UserEventAssignmentSearchCriteria criteria;

    @Override
    public Query toQuery(Query query, Map<String, Object> values) {
        return query;
    }
}

