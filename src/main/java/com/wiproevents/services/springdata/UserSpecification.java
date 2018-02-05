package com.wiproevents.services.springdata;

import com.wiproevents.entities.User;
import com.wiproevents.entities.UserSearchCriteria;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import com.microsoft.azure.spring.data.documentdb.core.query.Query;
import lombok.AllArgsConstructor;

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
    public Query toQuery(Query query) {
        return query;
    }
}

