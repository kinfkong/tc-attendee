package com.wiproevents.utils.springdata.extensions;

import com.microsoft.azure.spring.data.documentdb.core.DocumentDbOperations;
import com.microsoft.azure.spring.data.documentdb.repository.support.DocumentDbRepositoryFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.core.RepositoryMetadata;

/**
 * Created by wangjinggang on 2018/2/5.
 */
public class ExtDocumentDbRepositoryFactory extends DocumentDbRepositoryFactory {
    private final ApplicationContext applicationContext;

    public ExtDocumentDbRepositoryFactory(DocumentDbOperations dbOperations, ApplicationContext applicationContext) {
        super(dbOperations, applicationContext);
        this.applicationContext = applicationContext;
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return DocumentDbSpecificationRepositoryImpl.class;
    }

}
