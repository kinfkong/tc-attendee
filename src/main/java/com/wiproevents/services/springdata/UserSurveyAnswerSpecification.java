/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.services.springdata;

import com.microsoft.azure.spring.data.documentdb.core.query.Query;
import com.wiproevents.entities.answers.UserSurveyAnswer;
import com.wiproevents.entities.criteria.UserSurveyAnswerSearchCriteria;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import lombok.AllArgsConstructor;

import java.util.Map;

/**
 * The specification used to query user survey answer by criteria.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@AllArgsConstructor
public class UserSurveyAnswerSpecification implements DocumentDbSpecification<UserSurveyAnswer> {
    /**
     * The criteria. Final.
     */
    private final UserSurveyAnswerSearchCriteria criteria;


    /**
     * Converts the search criteria to sql query.
     *
     * @param query the sql query.
     * @param values the parameter values.
     * @return the sql query.
     */
    @Override
    public Query toQuery(Query query, Map<String, Object> values) {
        return query;
    }
}

