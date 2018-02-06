package com.wiproevents.services.springdata;

import com.microsoft.azure.spring.data.documentdb.core.query.Criteria;
import com.microsoft.azure.spring.data.documentdb.core.query.Query;
import com.wiproevents.entities.User;
import com.wiproevents.entities.UserSearchCriteria;
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

        if (this.criteria.getFullName() != null) {
            values.put("fullName", this.criteria.getFullName());
            query.addCriteria(Criteria.where("fullName", values));
        }

        if (this.criteria.getEmail() != null) {
            values.put("email", this.criteria.getEmail());
            query.addCriteria(Criteria.where("email", values));
        }
        return query;
    }
}

