/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */

package com.wiproevents;

import com.wiproevents.utils.springdata.extensions.ExtDocumentDbRepositoryFactoryBean;
import com.wiproevents.utils.springdata.extensions.ExtDocumentDbTemplate;
import com.wiproevents.utils.springdata.extensions.ExtMappingDocumentDbConverter;
import com.microsoft.azure.documentdb.ConnectionPolicy;
import com.microsoft.azure.documentdb.ConsistencyLevel;
import com.microsoft.azure.documentdb.DocumentClient;
import com.microsoft.azure.spring.data.documentdb.config.AbstractDocumentDbConfiguration;
import com.microsoft.azure.spring.data.documentdb.core.DocumentDbTemplate;
import com.microsoft.azure.spring.data.documentdb.core.convert.MappingDocumentDbConverter;
import com.microsoft.azure.spring.data.documentdb.repository.config.EnableDocumentDbRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDocumentDbRepositories(repositoryFactoryBeanClass = ExtDocumentDbRepositoryFactoryBean.class)
public class DatabaseConfig extends AbstractDocumentDbConfiguration {

    @Value("${azure.documentdb.uri}")
    private String uri;

    @Value("${azure.documentdb.key}")
    private String key;

    @Value("${azure.documentdb.database}")
    private String dbName;

    public DocumentClient documentClient() {
        return new DocumentClient(uri, key, ConnectionPolicy.GetDefault(), ConsistencyLevel.Session);
    }

    public String getDatabase() {
        return dbName;
    }

    @Override
    @Bean
    public DocumentDbTemplate documentDbTemplate() throws Exception {
        return new ExtDocumentDbTemplate(this.documentDbFactory(), this.mappingDocumentDbConverter(), this.getDatabase());
    }

    @Override
    @Bean
    public MappingDocumentDbConverter mappingDocumentDbConverter() throws Exception {
        return new ExtMappingDocumentDbConverter(this.documentDbMappingContext());
    }

}
