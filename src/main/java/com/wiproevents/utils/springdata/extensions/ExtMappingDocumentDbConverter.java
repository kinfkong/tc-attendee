package com.wiproevents.utils.springdata.extensions;

import com.microsoft.azure.documentdb.Document;
import com.microsoft.azure.spring.data.documentdb.core.convert.MappingDocumentDbConverter;
import com.microsoft.azure.spring.data.documentdb.core.mapping.DocumentDbPersistentEntity;
import com.microsoft.azure.spring.data.documentdb.core.mapping.DocumentDbPersistentProperty;
import com.wiproevents.entities.IdentifiableEntity;
import org.springframework.data.mapping.PersistentPropertyAccessor;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mapping.model.ConvertingPropertyAccessor;
import org.springframework.data.mapping.model.MappingException;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by wangjinggang on 2018/2/5.
 */
public class ExtMappingDocumentDbConverter extends MappingDocumentDbConverter {

    public ExtMappingDocumentDbConverter(
            MappingContext<? extends DocumentDbPersistentEntity<?>, DocumentDbPersistentProperty> mappingContext) {
        super(mappingContext);
    }

    @Override
    public void writeInternal(Object entity, Document targetDocument, DocumentDbPersistentEntity<?> entityInformation) {
        if (entity == null) {
            return;
        }

        if (entityInformation == null) {
            throw new MappingException("no mapping metadata for entity type: " + entity.getClass().getName());
        }

        final ConvertingPropertyAccessor accessor = getPropertyAccessor(entity);
        final DocumentDbPersistentProperty idProperty = entityInformation.getIdProperty();

        if (idProperty != null) {
            targetDocument.setId((String) accessor.getProperty(idProperty));
        }

        Class<? extends Object> clazz = entity.getClass();
        while (clazz != null) {
            // for reference document, save the id only
            for (final Field field : clazz.getDeclaredFields()) {
                if (null != idProperty && field.getName().equals(idProperty.getName())) {
                    continue;
                }
                Object value = accessor.getProperty(entityInformation.getPersistentProperty(field.getName()));
                value = modifyValue(value);
                targetDocument.set(field.getName(), value);
            }
            clazz = clazz.getSuperclass();
        }
    }

    @SuppressWarnings("unchecked")
    private Object modifyValue(Object value) {
        if (value == null) {
            return null;
        }
        Class<?> type = value.getClass();
        if (List.class.isAssignableFrom(type)) {
            List<Object> list = (List<Object>) value;
            List<Object> result = new ArrayList<>();
            list.forEach(item -> result.add(modifyValue(item)));
            return result;
        } else if (type.isAnnotationPresent(
                com.microsoft.azure.spring.data.documentdb.core.mapping.Document.class)) {
            // save id only
            Map<String, Object> result = new HashMap<>();
            result.put("id", ((IdentifiableEntity) value).getId());
            return result;
        } else if (value instanceof Date) {
            return ((Date) value).getTime();
        } else if (value.getClass().isEnum()) {
            return value.toString();
        } else {
            return value;
        }
    }

    private ConvertingPropertyAccessor getPropertyAccessor(Object entity) {
        final DocumentDbPersistentEntity<?> entityInformation = mappingContext.getPersistentEntity(entity.getClass());
        final PersistentPropertyAccessor accessor = entityInformation.getPropertyAccessor(entity);
        return new ConvertingPropertyAccessor(accessor, conversionService);
    }
}
