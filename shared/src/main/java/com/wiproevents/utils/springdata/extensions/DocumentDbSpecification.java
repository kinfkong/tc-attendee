package com.wiproevents.utils.springdata.extensions;

import com.microsoft.azure.spring.data.documentdb.core.query.Query;

import java.util.Map;

/**
 * Created by wangjinggang on 2018/2/5.
 */
public interface DocumentDbSpecification<T> {
    Query toQuery(Query query, Map<String, Object> values);
}
