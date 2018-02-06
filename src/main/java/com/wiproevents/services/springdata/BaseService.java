package com.wiproevents.services.springdata;

import com.wiproevents.entities.AuditableEntity;
import com.wiproevents.entities.IdentifiableEntity;
import com.wiproevents.exceptions.ConfigurationException;
import com.wiproevents.exceptions.EntityNotFoundException;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.utils.CustomMessageSource;
import com.wiproevents.utils.Helper;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationExecutor;
import com.wiproevents.utils.springdata.extensions.Paging;
import com.wiproevents.utils.springdata.extensions.SearchResult;
import com.microsoft.azure.spring.data.documentdb.repository.DocumentDbRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

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
     * The repository for CRUD operations. Should be non-null after injection.
     */
    @Autowired
    @Getter(value = PROTECTED)
    private DocumentDbRepository<T, String> repository;

    /**
     * The specification executor. Should be non-null after injection.
     */
    @Autowired
    private DocumentDbSpecificationExecutor<T, String> specificationExecutor;

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
        return ensureEntityExist(id);
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
        if (entity.getId() == null) {
            // auto generate the id
            entity.setId(UUID.randomUUID().toString());
        }

        handleNestedProperties(entity, true);

        if (entity instanceof AuditableEntity) {
            AuditableEntity auditableEntity = (AuditableEntity) entity;
            Date now = new Date();
            auditableEntity.setCreatedOn(new Date());
            auditableEntity.setUpdatedOn(now);
        }
        return repository.save(entity);
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
        handleNestedProperties(entity, false);
        if (entity instanceof AuditableEntity) {
            AuditableEntity auditableEntity = (AuditableEntity) entity;
            auditableEntity.setCreatedOn(((AuditableEntity) existing).getCreatedOn());
            auditableEntity.setUpdatedOn(new Date());
        }
        return repository.save(entity);
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
        return specificationExecutor.findAll(getSpecification(criteria), paging);
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
        return specificationExecutor.countAll(getSpecification(criteria));
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

    /**
     * This method is used to handle nested properties.
     *
     * @param entity the entity
     * @param isCreate
     * @throws AttendeeException if any error occurred during operation
     */
    protected void handleNestedProperties(T entity, boolean isCreate) throws AttendeeException { }

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
        T entity = repository.findOne(id);
        if (entity == null) {
            throw new EntityNotFoundException(CustomMessageSource.getMessage("entity.notFound.byId", id));
        }
        return entity;
    }
}

