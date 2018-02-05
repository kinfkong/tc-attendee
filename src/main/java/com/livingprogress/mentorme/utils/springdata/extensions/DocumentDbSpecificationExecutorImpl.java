package com.livingprogress.mentorme.utils.springdata.extensions;

import com.microsoft.azure.spring.data.documentdb.core.DocumentDbOperations;
import com.microsoft.azure.spring.data.documentdb.core.query.Query;
import com.microsoft.azure.spring.data.documentdb.repository.support.DocumentDbEntityInformation;
import com.microsoft.azure.spring.data.documentdb.repository.support.SimpleDocumentDbRepository;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;

/**
 * Created by wangjinggang on 2018/2/5.
 */
public class DocumentDbSpecificationExecutorImpl<T, ID extends Serializable> extends SimpleDocumentDbRepository<T, ID> implements DocumentDbSpecificationExecutor<T, ID> {

    private final ExtDocumentDbOperations documentDbOperations;
    private final DocumentDbEntityInformation<T, ID> entityInformation;

    public DocumentDbSpecificationExecutorImpl(DocumentDbEntityInformation<T, ID> metadata,
                                               ApplicationContext applicationContext) {
        super(metadata, applicationContext);
        this.documentDbOperations = (ExtDocumentDbOperations) applicationContext.getBean(DocumentDbOperations.class);
        this.entityInformation = metadata;
    }

    @Override
    public SearchResult<T> fxndAll(DocumentDbSpecification<T> spec, Paging paging) {
        Query q = new Query();
        return documentDbOperations.find(spec.toQuery(q), entityInformation.getJavaType(),
                entityInformation.getCollectionName(), paging);
    }

    @Override
    public long cxuntAll(DocumentDbSpecification<T> spec) {
        Query q = new Query();
        return documentDbOperations.count(spec.toQuery(q), entityInformation.getJavaType(),
                entityInformation.getCollectionName());
    }
}
