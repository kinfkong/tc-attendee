package com.wiproevents.dbtool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.azure.documentdb.*;
import com.microsoft.azure.spring.data.documentdb.DocumentDbFactory;
import com.microsoft.azure.spring.data.documentdb.core.DocumentDbTemplate;
import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import com.wiproevents.utils.springdata.extensions.ExtDocumentDbTemplate;
import org.apache.commons.io.FileUtils;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by wangjinggang on 2018/2/10.
 */
@Service
public class CollectionTool {
    @Autowired
    private DocumentDbTemplate documentDbTemplate;

    @Autowired
    private DocumentDbFactory documentDbFactory;

    private static final String DUMP_DIR = "." + File.separator + "test_files" + File.separator;


    public void createCollection(String name, Integer ru) {
        RequestOptions options = new RequestOptions();
        options.setOfferThroughput(ru);
        IndexingPolicy indexingPolicy = new IndexingPolicy();
        indexingPolicy.setIndexingMode(IndexingMode.Consistent);

        List<IncludedPath> includePaths = new ArrayList<>();
        indexingPolicy.setIncludedPaths(includePaths);

        IncludedPath includePath = new IncludedPath();
        includePath.setPath("/*");
        List<Index> indexes = new ArrayList<>();
        Index numberIndex = new RangeIndex(DataType.Number, -1);
        Index stringIndex = new RangeIndex(DataType.String, -1);
        indexes.add(numberIndex);
        indexes.add(stringIndex);
        includePath.setIndexes(indexes);
        includePaths.add(includePath);

        ((ExtDocumentDbTemplate) documentDbTemplate).createCollection(name, options, null, indexingPolicy);
        System.out.println("Successfully create collection: " + name);
    }

    public void dropCollection(String name) {
        documentDbTemplate.deleteAll(name);
        System.out.println("Successfully drop collection: " + name);
    }

    public void dumpCollection(String name) {
        List<com.microsoft.azure.documentdb.Document> documents = ((ExtDocumentDbTemplate) documentDbTemplate).findAllDocuments(name);
        String result = "[\n";
        boolean first = true;
        for (com.microsoft.azure.documentdb.Document d : documents) {
            if (!first) {
                result += ",\n";
            }
            first = false;
            result += d.toJson();
        }
        result += "\n]";
        try {
            FileUtils.writeStringToFile(new File(DUMP_DIR + name + ".json"), result, "UTF-8");
        } catch (IOException e) {
            // ignore
        }
    }

    public void dumpCollections(List<String> names) {
        names.forEach(this::dumpCollection);
    }

    public void loadCollection(String name) {
        try {
            String dstring = FileUtils.readFileToString(new File(DUMP_DIR + name + ".json"), "UTF-8");
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            Object[] array = gson.fromJson(dstring, Object[].class);
            for (Object obj : array) {
                com.microsoft.azure.documentdb.Document d = new com.microsoft.azure.documentdb.Document(gson.toJson(obj));
                ((ExtDocumentDbTemplate) documentDbTemplate).insertDocument(name, d, null);
            }

        } catch(FileNotFoundException e) {
            // ignore
        } catch (IOException e) {
            // ignore
            e.printStackTrace();
        }

    }

    public void loadAllCollections() {
        String[] allCollectionNames = getAllCollectionNames();
        for (String name: allCollectionNames) {
            loadCollection(name);
        }
    }

    public void createAllCollections() {
        String[] allCollectionNames = getAllCollectionNames();
        for (String name: allCollectionNames) {
            createCollection(name, 400);
        }
    }

    public void dropAllCollections() {
        String[] allCollectionNames = getAllCollectionNames();
        for (String name: allCollectionNames) {
            dropCollection(name);
        }
    }

    private static String[] getAllCollectionNames() {
        Reflections reflections = new Reflections("com.wiproevents.entities");
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Document.class);
        Set<String> result = new HashSet<>();
        for (Class<?> clazz : annotated) {
            Document annotation = clazz.getAnnotation(Document.class);
            result.add(annotation.collection());
        }

        return result.toArray(new String[0]);
    }
}
