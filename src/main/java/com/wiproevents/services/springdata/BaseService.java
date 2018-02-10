package com.wiproevents.services.springdata;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import com.microsoft.azure.spring.data.documentdb.repository.DocumentDbRepository;
import com.wiproevents.entities.AuditableEntity;
import com.wiproevents.entities.AuditableUserEntity;
import com.wiproevents.entities.IdentifiableEntity;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.exceptions.ConfigurationException;
import com.wiproevents.exceptions.EntityNotFoundException;
import com.wiproevents.utils.Helper;
import com.wiproevents.utils.springdata.extensions.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import static lombok.AccessLevel.PROTECTED;

/**
 * This is a base class for services that provides basic CRUD capabilities.
 *
 * @param <T> the identifiable entity
 * @param <S> the search criteria
 */
@NoArgsConstructor(access = PROTECTED)
public abstract class BaseService<T extends IdentifiableEntity, S> {
    /**
     * The default sort column by id.
     */
    public static final String ID = "id";

    /**
     * The specification executor. Should be non-null after injection.
     */
    @Autowired
    @Getter(value = PROTECTED)
    private DocumentDbSpecificationRepository<T, String> repository;

    private PropertyUtilsBean beanUtils = new PropertyUtilsBean();

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(repository, "repository");
    }

    /**
     * This method is used to retrieve an entity.
     *
     * @param id the id of the entity to retrieve
     * @return the match entity
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws AttendeeException if any other error occurred during operation
     */
    public T get(String id) throws AttendeeException {
        T entity = ensureEntityExist(id);
        handlePopulate(entity);
        return entity;
    }

    /**
     * This method is used to create an entity.
     *
     * @param entity the entity to create
     * @return the created entity
     * @throws IllegalArgumentException if entity is null or not valid
     * @throws AttendeeException if any other error occurred during operation
     */
    @Transactional
    public T create(T entity) throws AttendeeException {
        Helper.checkNull(entity, "entity");

        // assign id
        if (entity.getId() != null) {
            throw new IllegalArgumentException("You cannot assign the id on create.");
        }

        entity.setId(UUID.randomUUID().toString());

        handleNestedValidation(entity);

        handleNestedCreate(entity);

        if (entity instanceof AuditableEntity) {
            AuditableEntity auditableEntity = (AuditableEntity) entity;
            Date now = new Date();
            auditableEntity.setCreatedOn(new Date());
            auditableEntity.setUpdatedOn(now);

            if (entity instanceof AuditableUserEntity) {
                AuditableUserEntity auditableUserEntity = (AuditableUserEntity) entity;
                auditableUserEntity.setCreatedBy(Helper.getAuthUser().getId());
                auditableUserEntity.setUpdatedBy(Helper.getAuthUser().getId());
            }

        }

        T obj = repository.save(entity);

        // retrieve with populations
        return this.get(obj.getId());
    }


    /**
     * This method is used to delete an entity.
     *
     * @param id the id of the entity to delete
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws AttendeeException if any other error occurred during operation
     */
    @Transactional
    public void delete(String id) throws AttendeeException {
        Helper.checkNullOrEmpty(id, "id");
        ensureEntityExist(id);
        repository.delete(id);
    }


    /**
     * This method is used to update an entity.
     *
     * @param id the id of the entity to update
     * @param entity the entity to update
     * @return the updated entity
     * @throws IllegalArgumentException if id is not positive or entity is null or id of entity is not positive
     * or id of  entity not match id or entity is invalid
     * @throws EntityNotFoundException if the entity does not exist
     * @throws AttendeeException if any other error occurred during operation
     */
    @Transactional
    public T update(String id, T entity) throws AttendeeException {
        T existing = checkUpdate(id, entity);
        handleNestedValidation(entity);
        handleNestedUpdate(entity, existing);

        if (entity instanceof AuditableEntity) {
            AuditableEntity auditableEntity = (AuditableEntity) entity;
            auditableEntity.setCreatedOn(((AuditableEntity) existing).getCreatedOn());
            auditableEntity.setUpdatedOn(new Date());

            if (entity instanceof AuditableUserEntity) {
                AuditableUserEntity auditableUserEntity = (AuditableUserEntity) entity;

                auditableUserEntity.setCreatedBy(((AuditableUserEntity) existing).getCreatedBy());
                auditableUserEntity.setUpdatedBy(Helper.getAuthUser().getId());
            }
        }

        repository.save(entity);

        // for population
        return get(entity.getId());
    }

    /**
     * Check id and entity for update method.
     *
     * @param id the id of the entity to update
     * @param entity the entity to update
     * @return the existing entity
     * @throws IllegalArgumentException if id is not positive or entity is null or id of entity is not positive
     * or id of  entity not match id
     * @throws EntityNotFoundException if the entity does not exist
     */
    protected T checkUpdate(String id, T entity) throws EntityNotFoundException {
        Helper.checkUpdate(id, entity);
        return ensureEntityExist(id);
    }


    /**
     * This method is used to search for entities by criteria and paging params.
     *
     * @param criteria the search criteria
     * @param paging the paging data
     * @return the search result
     * @throws IllegalArgumentException if pageSize is not positive or pageNumber is negative
     * @throws AttendeeException if any other error occurred during operation
     */
    public SearchResult<T> search(S criteria, Paging paging) throws AttendeeException {
        SearchResult<T> result = repository.findAll(getSpecification(criteria), paging, true);
        for (T item : result.getEntities()) {
            handlePopulate(item);
        }
        return result;
    }

    /**
     * This method is used to get total count for search results of entities with criteria.
     *
     * @param criteria the search criteria
     * @return the total count of search result
     * @throws IllegalArgumentException if pageSize is not positive or pageNumber is negative
     * @throws AttendeeException if any other error occurred during operation
     */
    public long count(S criteria) throws AttendeeException {
        return repository.countAll(getSpecification(criteria));
    }


    /**
     * This method is used to get the Specification<T>.
     *
     * @param criteria the criteria
     * @return the specification
     * @throws IllegalArgumentException if pageSize is not positive or pageNumber is negative
     * @throws AttendeeException if any other error occurred during operation
     */
    protected abstract DocumentDbSpecification<T> getSpecification(S criteria) throws AttendeeException;



    protected void handleNestedUpdate(T entity, T oldEntity) throws AttendeeException {

    }

    protected void handleNestedCreate(T entity) throws AttendeeException {

    }

    protected  void handlePopulate(T entity) throws AttendeeException {

    }

    protected  void handleNestedValidation(T entity) throws AttendeeException {
        Map<String, DocumentDbRepository<?, String>> repositories = repository.getNestedRepositories();
        for (String path : repositories.keySet()) {
            DocumentDbRepository<?, String> pathRepository = repositories.get(path);
            // get value from the build
            try {
                Object value = Helper.getPropertyExt(beanUtils, entity, path);

                validateReference(path, value, pathRepository);

            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new AttendeeException("failed to read: " + path + " from class: " + entity.getClass());
            }
        }
    }

    private void validateReference(String path, Object value, DocumentDbRepository<?, String> pathRepository) throws AttendeeException {
        if (value == null) {
            return;
        }
        if (value instanceof List) {
            for (Object v : (List<?>) value) {
                validateReference(path, v, pathRepository);
            }
            return;
        }

        if (!(value instanceof IdentifiableEntity)) {
            throw new AttendeeException(
                    "The class: " + value.getClass() + " of path: " + path + " is not IdentifiableEntity");
        }

        IdentifiableEntity v = (IdentifiableEntity) value;
        String entityId = v.getId();
        Helper.checkNullOrEmpty(entityId, "The id in path: " + path);


        // check existence
        Object nestedEntity = pathRepository.findOne(entityId);
        if (nestedEntity == null) {
            throw new IllegalArgumentException(
                    "The id " + entityId + " in path: " + path + " cannot find entity.");
        }
    }

    @SuppressWarnings("unchecked")
    private void assignIds(IdentifiableEntity entity, boolean isRoot) {
        if (!isRoot && entity.getClass().getAnnotation(Document.class) != null) {
            // only assign ids for embedded type.
            return;
        }

        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
        }
        // check the sub fields
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (IdentifiableEntity.class.isAssignableFrom(field.getType())) {
                try {
                    assignIds((IdentifiableEntity) field.get(entity), false);
                } catch (IllegalAccessException e) {
                    // ignore
                    e.printStackTrace();
                }
            } else if (List.class.isAssignableFrom(field.getType())) {
                ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
                Class<?> actualType = (Class<?>) stringListType.getActualTypeArguments()[0];
                if (actualType.isAssignableFrom(IdentifiableEntity.class)) {
                    try {
                        List<IdentifiableEntity> list = (List<IdentifiableEntity>) field.get(entity);
                        list.forEach(item -> assignIds(item, false));
                    } catch (IllegalAccessException e) {
                        // ignore
                    }
                }
            }
        }
    }
    /**
     * Check whether an identifiable entity with a given id exists.
     *
     * @param id the id of entity
     * @return the match entity
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the match entity can not be found in DB
     */
    private T ensureEntityExist(String id) throws EntityNotFoundException {
        Helper.checkNullOrEmpty(id, "id");
        T entity = repository.findOne(id, true);
        if (entity == null) {
            throw new EntityNotFoundException(String.format("Entity with ID=%s can not be found", id));
        }
        return entity;
    }
}

