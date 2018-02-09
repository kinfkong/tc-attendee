package com.wiproevents.services.springdata;

import com.microsoft.azure.spring.data.documentdb.core.query.Query;
import com.wiproevents.entities.SocialUser;
import com.wiproevents.entities.SocialUserSearchCriteria;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * The specification used to query User by criteria.
 */
@AllArgsConstructor
public class SocialUserSpecification implements DocumentDbSpecification<SocialUser> {
    /**
     * The criteria. Final.
     */
    private final SocialUserSearchCriteria criteria;


    @Override
    public Query toQuery(Query query, Map<String, Object> values) {
        return query;
    }
}

