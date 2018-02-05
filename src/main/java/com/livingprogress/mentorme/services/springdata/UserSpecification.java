package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.User;
import com.livingprogress.mentorme.entities.UserSearchCriteria;
import com.livingprogress.mentorme.utils.springdata.extensions.DocumentDbSpecification;
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

