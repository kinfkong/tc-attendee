package com.wiproevents.utils.springdata.extensions;

import com.microsoft.azure.documentdb.*;
import com.microsoft.azure.spring.data.documentdb.DocumentDbFactory;
import com.microsoft.azure.spring.data.documentdb.core.DocumentDbTemplate;
import com.microsoft.azure.spring.data.documentdb.core.convert.MappingDocumentDbConverter;
import com.microsoft.azure.spring.data.documentdb.core.query.Query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by wangjinggang on 2018/2/5.
 */
public class ExtDocumentDbTemplate extends DocumentDbTemplate implements ExtDocumentDbOperations {
    private final DocumentDbFactory documentDbFactory;
    private final MappingDocumentDbConverter mappingDocumentDbConverter;
    private final String databaseName;

    public ExtDocumentDbTemplate(DocumentDbFactory documentDbFactory,
                              MappingDocumentDbConverter mappingDocumentDbConverter,
                              String dbName) {
        super(documentDbFactory, mappingDocumentDbConverter, dbName);
        this.databaseName = dbName;
        this.documentDbFactory = documentDbFactory;
        this.mappingDocumentDbConverter = mappingDocumentDbConverter;
    }

    public ExtDocumentDbTemplate(DocumentClient client,
                              MappingDocumentDbConverter mappingDocumentDbConverter,
                              String dbName) {

        this(new DocumentDbFactory(client), mappingDocumentDbConverter, dbName);
    }

    public Document insertDocument(String collectionName,
                        Document document,
                        String partitionKeyFieldValue) {

        try {
            documentDbFactory.getDocumentClient()
                    .createDocument(getCollectionLink(this.databaseName, collectionName), document,
                            null, false);
            return document;
        } catch (DocumentClientException e) {
            throw new RuntimeException("insert exception", e);
        }
    }
    private String getCollectionLink(String databaseName, String collectionName) {
        return getDatabaseLink(databaseName) + "/colls/" + collectionName;
    }


    @Override
    public <T> int count(Query query, Class<T> domainClass, String collectionName) {
        final SqlQuerySpec sqlQuerySpec = createCountSqlQuerySpec(query);

        final List<DocumentCollection> collections = documentDbFactory.getDocumentClient().
                queryCollections(
                        getDatabaseLink(this.databaseName),
                        new SqlQuerySpec("SELECT * FROM ROOT r WHERE r.id=@id",
                                new SqlParameterCollection(new SqlParameter("@id", collectionName))), null)
                .getQueryIterable().toList();

        if (collections.size() != 1) {
            throw new RuntimeException("expect only one collection: " + collectionName
                    + " in database: " + this.databaseName + ", but found " + collections.size());
        }

        final FeedOptions feedOptions = new FeedOptions();

        FeedResponse<Document> q = documentDbFactory.getDocumentClient()
                .queryDocuments(collections.get(0).getSelfLink(),
                        sqlQuerySpec, feedOptions);
        Iterator<Document> queryIterator = q.getQueryIterable().iterator();
        int aggregateCount = 0;
        while (queryIterator.hasNext()) {
            Document d = queryIterator.next();
            int docValue = Integer.parseInt(d.get("_aggregate").toString());
            aggregateCount += docValue;
        }
        return aggregateCount;
    }

    @Override
    public <T> SearchResult<T> find(Query query, Class<T> domainClass, String collectionName, Paging paging) {
        final SqlQuerySpec sqlQuerySpec = createSqlQuerySpec(query, paging);

        final List<DocumentCollection> collections = documentDbFactory.getDocumentClient().
                queryCollections(
                        getDatabaseLink(this.databaseName),
                        new SqlQuerySpec("SELECT * FROM ROOT r WHERE r.id=@id",
                                new SqlParameterCollection(new SqlParameter("@id", collectionName))), null)
                .getQueryIterable().toList();

        if (collections.size() != 1) {
            throw new RuntimeException("expect only one collection: " + collectionName
                    + " in database: " + this.databaseName + ", but found " + collections.size());
        }

        final FeedOptions feedOptions = new FeedOptions();

        if (paging != null && paging.getPageSize() != 0) {
            // enable paging
            feedOptions.setPageSize(paging.getPageSize());
            feedOptions.setRequestContinuation(paging.getRequestContinuation());
        }

        feedOptions.setEnableCrossPartitionQuery(true);

        FeedResponse<Document> q = documentDbFactory.getDocumentClient()
                .queryDocuments(collections.get(0).getSelfLink(),
                        sqlQuerySpec, feedOptions);

        Iterator<Document> it = q.getQueryIterator();
        final List<T> entities = new ArrayList<>();
        while(it.hasNext()) {
            Document d = it.next();
            final T entity = mappingDocumentDbConverter.read(domainClass, d);
            entities.add(entity);
        }

        SearchResult<T> result = new SearchResult<>();
        result.setTotal(count(query, domainClass, collectionName));
        result.setRequestContinuation(q.getResponseContinuation());
        result.setEntities(entities);
        return result;
    }

    public List<Document> findAllDocuments(String collectionName) {
        final List<DocumentCollection> collections = documentDbFactory.getDocumentClient().
                queryCollections(
                        getDatabaseLink(this.databaseName),
                        new SqlQuerySpec("SELECT * FROM ROOT r WHERE r.id=@id",
                                new SqlParameterCollection(new SqlParameter("@id", collectionName))), null)
                .getQueryIterable().toList();
        if (collections.size() != 1) {
            throw new RuntimeException("expect only one collection: " + collectionName
                    + " in database: " + this.databaseName + ", but found " + collections.size());
        }
        final FeedOptions feedOptions = new FeedOptions();

        feedOptions.setEnableCrossPartitionQuery(true);

        FeedResponse<Document> q = documentDbFactory.getDocumentClient()
                .queryDocuments(collections.get(0).getSelfLink(),
                        "SELECT * from ROOT r", feedOptions);

        Iterator<Document> it = q.getQueryIterator();
        final List<Document> entities = new ArrayList<>();
        while(it.hasNext()) {
            Document d = it.next();
            entities.add(d);
        }
        return entities;
    }

    public DocumentCollection createCollection(String collectionName,
                                               RequestOptions collectionOptions,
                                               String partitionKeyFieldName,
                                               IndexingPolicy indexingPolicy) {
        return createCollection(this.databaseName, collectionName, collectionOptions, partitionKeyFieldName, indexingPolicy);
    }

    public DocumentCollection createCollection(String dbName,
                                               String collectionName,
                                               RequestOptions collectionOptions,
                                               String partitionKeyFieldName,
                                               IndexingPolicy indexingPolicy) {
        DocumentCollection collection = new DocumentCollection();
        collection.setId(collectionName);

        if (partitionKeyFieldName != null && !partitionKeyFieldName.isEmpty()) {
            final PartitionKeyDefinition partitionKeyDefinition = new PartitionKeyDefinition();
            final ArrayList<String> paths = new ArrayList<String>();

            paths.add(getPartitionKeyPath(partitionKeyFieldName));
            partitionKeyDefinition.setPaths(paths);
            collection.setPartitionKey(partitionKeyDefinition);
        }
        if (indexingPolicy != null) {
            collection.setIndexingPolicy(indexingPolicy);
        }

        try {
            final Resource resource = documentDbFactory.getDocumentClient()
                    .createCollection(getDatabaseLink(dbName), collection, collectionOptions)
                    .getResource();
            if (resource instanceof DocumentCollection) {
                collection = (DocumentCollection) resource;
            }
            return collection;
        } catch (DocumentClientException e) {
            throw new RuntimeException("createCollection exception", e);
        }

    }



    private String getDatabaseLink(String databaseName) {
        return "dbs/" + databaseName;
    }

    private static String normalizeParamName(String param) {
        return param.replace(".", "_");
    }

    private static Object normalizeValue(Object value) {
        if (value.getClass().isEnum()) {
            return value.toString();
        }
        return value;
    }
    private static SqlQuerySpec createSqlQuerySpec(Query query, Paging paging) {
        String queryStr = "SELECT * FROM ROOT r WHERE 1=1";
        final SqlParameterCollection parameterCollection = new SqlParameterCollection();

        for (final Map.Entry<String, Object> entry : query.getCriteria().entrySet()) {
            queryStr += " AND r." + entry.getKey() + "=@" + normalizeParamName(entry.getKey());
            parameterCollection.add(new SqlParameter("@" + normalizeParamName(entry.getKey()),
                    normalizeValue(entry.getValue())));
        }
        if (paging != null && paging.getSortColumn() != null) {
            SortOrder sortOrder = paging.getSortOrder();
            if (sortOrder == null) {
                sortOrder = SortOrder.ASC;
            }
            queryStr += " ORDER BY r." + paging.getSortColumn() + " " + sortOrder;
        }
        System.out.println("queryStr: " + queryStr);

        return new SqlQuerySpec(queryStr, parameterCollection);
    }

    private static SqlQuerySpec createCountSqlQuerySpec(Query query) {
        String queryStr = "SELECT VALUE COUNT(r) FROM ROOT r WHERE 1=1";
        final SqlParameterCollection parameterCollection = new SqlParameterCollection();

        for (final Map.Entry<String, Object> entry : query.getCriteria().entrySet()) {
            queryStr += " AND r." + entry.getKey() + "=@" + normalizeParamName(entry.getKey());
            parameterCollection.add(new SqlParameter("@" + normalizeParamName(entry.getKey()),
                    normalizeValue(entry.getValue())));
        }

        System.out.println("queryStr: " + queryStr);
        return new SqlQuerySpec(queryStr, parameterCollection);
    }

    private String getPartitionKeyPath(String partitionKey) {
        return "/" + partitionKey;
    }


}
