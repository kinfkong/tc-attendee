package com.livingprogress.mentorme.utils.springdata.extensions;

import com.microsoft.azure.spring.data.documentdb.core.query.Query;

/**
 * Created by wangjinggang on 2018/2/5.
 */
public interface DocumentDbSpecification<T> {
    Query toQuery(Query query);
}
