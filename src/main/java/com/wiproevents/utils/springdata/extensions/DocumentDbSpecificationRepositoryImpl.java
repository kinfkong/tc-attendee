package com.wiproevents.utils.springdata.extensions;

import com.microsoft.azure.spring.data.documentdb.core.DocumentDbOperations;
import com.microsoft.azure.spring.data.documentdb.core.query.Query;
import com.microsoft.azure.spring.data.documentdb.repository.DocumentDbRepository;
import com.microsoft.azure.spring.data.documentdb.repository.support.DocumentDbEntityInformation;
import com.microsoft.azure.spring.data.documentdb.repository.support.SimpleDocumentDbRepository;
import com.wiproevents.entities.IdentifiableEntity;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by wangjinggang on 2018/2/5.
 */
public class DocumentDbSpecificationRepositoryImpl<T, ID extends Serializable> extends SimpleDocumentDbRepository<T, ID> implements DocumentDbSpecificationRepository<T, ID> {

    private final ExtDocumentDbOperations documentDbOperations;
    private final DocumentDbEntityInformation<T, ID> entityInformation;

    private PropertyUtilsBean beanUtils = new PropertyUtilsBean();
    private Map<String, DocumentDbRepository<?, ID>> nestedRepositories = new HashMap<>();

    public DocumentDbSpecificationRepositoryImpl(DocumentDbEntityInformation<T, ID> metadata,
                                                 ApplicationContext applicationContext) {
        super(metadata, applicationContext);
        this.documentDbOperations = (ExtDocumentDbOperations) applicationContext.getBean(DocumentDbOperations.class);
        this.entityInformation = metadata;
    }

    @Override
    public SearchResult<T> findAll(DocumentDbSpecification<T> spec, Paging paging) {
        Query q = new Query();
        LinkedHashMap<String, Object> values = new LinkedHashMap<>();
        return documentDbOperations.find(spec.toQuery(q, values), entityInformation.getJavaType(),
                entityInformation.getCollectionName(), paging);
    }

    @Override
    public SearchResult<T> findAll(DocumentDbSpecification<T> spec, Paging paging, Boolean withPopulatedFields) {
        SearchResult<T> result = this.findAll(spec, paging);
        result.getEntities().forEach(this::populateReference);
        return result;
    }

    @Override
    public T findOne(ID id, Boolean withPopulatedFields) {
        T entity = super.findOne(id);
        populateReference(entity);
        return entity;
    }



    private void populateReference(T entity) {
        for (String path : this.nestedRepositories.keySet()) {
            DocumentDbRepository<?, ID> pathRepository = this.nestedRepositories.get(path);
            // get value from the build
            try {
                Object value = beanUtils.getProperty(entity, path);

                Object populatedValue = populateReference(path, value, pathRepository);

                beanUtils.setProperty(entity, path, populatedValue);

            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new IllegalStateException("failed to read: " + path + " from class: " + entity.getClass());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Object populateReference(String path, Object value, DocumentDbRepository<?, ID> pathRepository) {
        if (value == null) {
            return null;
        }
        if (value instanceof List) {
            List<Object> result = new ArrayList<>();
            for (Object v : (List<?>) value) {
                result.add(populateReference(path, v, pathRepository));
            }
            return result;
        }

        if (!(value instanceof IdentifiableEntity)) {
            throw new IllegalStateException(
                    "The class: " + value.getClass() + " of path: " + path + " is not IdentifiableEntity");
        }

        IdentifiableEntity v = (IdentifiableEntity) value;
        ID entityId = (ID) v.getId();

        if (entityId == null) {
            throw new IllegalStateException(
                    "The class: " + value.getClass() + " of path:  " + path + " id cannot be null.");
        }

        Object nestedEntity;

        if (pathRepository instanceof  DocumentDbSpecificationRepository) {
            DocumentDbSpecificationRepository<?, ID> specificationRepository =
                    (DocumentDbSpecificationRepository<?, ID>) pathRepository;
            nestedEntity = specificationRepository.findOne((ID) entityId, true);
        } else {
            nestedEntity = pathRepository.findOne(entityId);
        }
        if (nestedEntity == null) {
            throw new IllegalStateException(
                    "The id " + entityId + " in path: " + path + " cannot find entity.");
        }

        return nestedEntity;
    }

    @Override
    public long countAll(DocumentDbSpecification<T> spec) {
        Query q = new Query();
        LinkedHashMap<String, Object> values = new LinkedHashMap<>();
        return documentDbOperations.count(spec.toQuery(q, values), entityInformation.getJavaType(),
                entityInformation.getCollectionName());
    }


    @Override
    public void addNestedRepository(String path, DocumentDbRepository<?, ID> repository) {
        this.nestedRepositories.put(path, repository);
    }
}
