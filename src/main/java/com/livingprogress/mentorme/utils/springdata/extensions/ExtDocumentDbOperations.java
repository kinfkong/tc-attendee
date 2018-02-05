package com.livingprogress.mentorme.utils.springdata.extensions;

import com.microsoft.azure.spring.data.documentdb.core.query.Query;

/**
 * Created by wangjinggang on 2018/2/5.
 */
public interface ExtDocumentDbOperations {
    <T> SearchResult<T> find(Query query, Class<T> domainClass, String collectionName, Paging paging);
    <T> int count(Query query, Class<T> domainClass, String collectionName);
}
