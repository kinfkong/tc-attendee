package com.livingprogress.mentorme.utils.springdata.extensions;
/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */

import com.microsoft.azure.spring.data.documentdb.core.DocumentDbOperations;
import com.microsoft.azure.spring.data.documentdb.core.mapping.DocumentDbMappingContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import java.io.Serializable;


public class ExtDocumentDbRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
        extends RepositoryFactoryBeanSupport<T, S, ID>
        implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    private DocumentDbOperations operations;
    private boolean mappingContextConfigured = false;


    public ExtDocumentDbRepositoryFactoryBean(Class<? extends T> repositoryInterface,
                                           DocumentDbOperations operations) {

        super(repositoryInterface);
        this.operations = operations;
    }

    public void setDocumentDbOperations(DocumentDbOperations operations) {
        this.operations = operations;
    }

    @Override
    protected final RepositoryFactorySupport createRepositoryFactory() {
        return getFactoryInstance(applicationContext);
    }

    protected RepositoryFactorySupport getFactoryInstance(ApplicationContext applicationContext) {
        return new ExtDocumentDbRepositoryFactory(operations, applicationContext);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    protected void setMappingContext(MappingContext<?, ?> mappingContext) {
        super.setMappingContext(mappingContext);
        this.mappingContextConfigured = true;
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();

        if (!this.mappingContextConfigured) {
            if (operations != null) {
                setMappingContext(operations.getConverter().getMappingContext());
            } else {
                setMappingContext(new DocumentDbMappingContext());
            }
        }
    }
}
