package com.wiproevents.utils.springdata.extensions;

import com.microsoft.azure.documentdb.Document;
import com.microsoft.azure.spring.data.documentdb.core.convert.MappingDocumentDbConverter;
import com.microsoft.azure.spring.data.documentdb.core.mapping.DocumentDbPersistentEntity;
import com.microsoft.azure.spring.data.documentdb.core.mapping.DocumentDbPersistentProperty;
import org.springframework.data.mapping.PersistentPropertyAccessor;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mapping.model.ConvertingPropertyAccessor;
import org.springframework.data.mapping.model.MappingException;

import java.lang.reflect.Field;
import java.util.Date;

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
            for (final Field field : clazz.getDeclaredFields()) {
                if (null != idProperty && field.getName().equals(idProperty.getName())) {
                    continue;
                }
                Object value = accessor.getProperty(entityInformation.getPersistentProperty(field.getName()));
                if (value instanceof Date) {
                    value = ((Date) value).getTime();
                } else if (value != null && value.getClass().isEnum()) {
                    value = value.toString();
                }
                targetDocument.set(field.getName(), value);
            }
            clazz = clazz.getSuperclass();
        }

    }

    private ConvertingPropertyAccessor getPropertyAccessor(Object entity) {
        final DocumentDbPersistentEntity<?> entityInformation = mappingContext.getPersistentEntity(entity.getClass());
        final PersistentPropertyAccessor accessor = entityInformation.getPropertyAccessor(entity);
        return new ConvertingPropertyAccessor(accessor, conversionService);
    }
}
