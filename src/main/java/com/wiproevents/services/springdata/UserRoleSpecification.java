package com.wiproevents.services.springdata;

import com.microsoft.azure.spring.data.documentdb.core.query.Query;
import com.wiproevents.entities.UserRole;
import com.wiproevents.entities.UserRoleSearchCriteria;
import com.wiproevents.utils.Helper;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * The specification used to query User by criteria.
 */
@AllArgsConstructor
public class UserRoleSpecification implements DocumentDbSpecification<UserRole> {
    /**
     * The criteria. Final.
     */
    private final UserRoleSearchCriteria criteria;


    @Override
    public Query toQuery(Query query, Map<String, Object> values) {
        Helper.buildEqualPredict(query, values, "name", criteria.getName());
        return query;
    }
}

