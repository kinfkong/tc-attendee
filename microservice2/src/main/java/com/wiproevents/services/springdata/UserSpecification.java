package com.wiproevents.services.springdata;

import com.microsoft.azure.spring.data.documentdb.core.query.Query;
import com.wiproevents.entities.User;
import com.wiproevents.entities.criteria.UserSearchCriteria;
import com.wiproevents.utils.Helper;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * The specification used to query User by criteria.
 */
@AllArgsConstructor
public class UserSpecification implements DocumentDbSpecification<User> {
    /**
     * The criteria. Final.
     */
    private final UserSearchCriteria criteria;


    @Override
    public Query toQuery(Query query, Map<String, Object> values) {

        Helper.buildEqualPredict(query, values, "fullName", this.criteria.getFullName());
        Helper.buildEqualPredict(query, values, "email", this.criteria.getEmail());
        Helper.buildEqualPredict(query, values, "status", this.criteria.getStatus());
        return query;
    }
}

