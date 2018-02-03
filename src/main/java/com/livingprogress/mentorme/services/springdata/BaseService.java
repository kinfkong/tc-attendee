package mentorme.services.springdata;

import com.livingprogress.mentorme.entities.*;
import com.livingprogress.mentorme.exceptions.ConfigurationException;
import com.livingprogress.mentorme.exceptions.EntityNotFoundException;
import com.livingprogress.mentorme.exceptions.MentorMeException;
import com.livingprogress.mentorme.utils.CustomMessageSource;
import com.livingprogress.mentorme.utils.Helper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
    private JpaRepository<T, Long> repository;

    /**
     * The specification executor. Should be non-null after injection.
     */
    @Autowired
    private JpaSpecificationExecutor<T> specificationExecutor;


    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(repository, "repository");
        Helper.checkConfigNotNull(specificationExecutor, "specificationExecutor");
    }

    /**
     * This method is used to retrieve an entity.
     *
     * @param id the id of the entity to retrieve
     * @return the match entity
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws MentorMeException if any other error occurred during operation
     */
    public T get(long id) throws MentorMeException {
        return ensureEntityExist(id);
    }

    /**
     * This method is used to create an entity.
     *
     * @param entity the entity to create
     * @return the created entity
     * @throws IllegalArgumentException if entity is null or not valid
     * @throws MentorMeException if any other error occurred during operation
     */
    @Transactional
    public T create(T entity) throws MentorMeException {
        Helper.checkNull(entity, "entity");
        handleNestedProperties(entity);
        if (entity instanceof AuditableEntity) {
            if (entity instanceof AuditableUserEntity) {
                Helper.audit((AuditableUserEntity) entity);
            } else {
                AuditableEntity auditableEntity = (AuditableEntity) entity;
                Date now = new Date();
                auditableEntity.setCreatedOn(new Date());
                auditableEntity.setLastModifiedOn(now);
            }
        }
        return repository.save(entity);
    }

    /**
     * This method is used to delete an entity.
     *
     * @param id the id of the entity to delete
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the entity does not exist
     * @throws MentorMeException if any other error occurred during operation
     */
    @Transactional
    public void delete(long id) throws MentorMeException {
        Helper.checkPositive(id, "id");
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
     * @throws MentorMeException if any other error occurred during operation
     */
    @Transactional
    public T update(long id, T entity) throws MentorMeException {
        T existing = checkUpdate(id, entity);
        handleNestedProperties(entity);
        if (entity instanceof AuditableEntity) {
            AuditableEntity auditableEntity = (AuditableEntity) entity;
            auditableEntity.setCreatedOn(((AuditableEntity) existing).getCreatedOn());
            auditableEntity.setLastModifiedOn(new Date());
            if (entity instanceof AuditableUserEntity) {
                AuditableUserEntity newEntity = (AuditableUserEntity) entity;
                AuditableUserEntity existingEntity = (AuditableUserEntity) existing;
                newEntity.setCreatedBy(existingEntity.getCreatedBy());
                User authUser = Helper.getAuthUser();
                if (authUser != null) {
                    newEntity.setLastModifiedBy(authUser.getId());
                }
            }
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
    protected T checkUpdate(long id, T entity) throws EntityNotFoundException {
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
     * @throws MentorMeException if any other error occurred during operation
     */
    public SearchResult<T> search(S criteria, Paging paging) throws MentorMeException {
        Pageable page = remapPaging(paging);
        if (page != null) {
            return remapResult(specificationExecutor.findAll(getSpecification(criteria), remapPaging(paging)));
        }
        if (paging != null && paging.getPageNumber() == 0 && paging.getPageSize() == 0) {
            return remapResult(specificationExecutor.findAll(getSpecification(criteria), remapSort(paging)));
        }
        return remapResult(specificationExecutor.findAll(getSpecification(criteria)));
    }

    /**
     * This method is used to get total count for search results of entities with criteria.
     *
     * @param criteria the search criteria
     * @return the total count of search result
     * @throws IllegalArgumentException if pageSize is not positive or pageNumber is negative
     * @throws MentorMeException if any other error occurred during operation
     */
    public long count(S criteria) throws MentorMeException {
        return specificationExecutor.count(getSpecification(criteria));
    }


    /**
     * This method is used to get the Specification<T>.
     *
     * @param criteria the criteria
     * @return the specification
     * @throws IllegalArgumentException if pageSize is not positive or pageNumber is negative
     * @throws MentorMeException if any other error occurred during operation
     */
    protected abstract Specification<T> getSpecification(S criteria) throws MentorMeException;

    /**
     * This method is used to handle nested properties.
     *
     * @param entity the entity
     * @throws MentorMeException if any error occurred during operation
     */
    protected void handleNestedProperties(T entity) throws MentorMeException { }

    /**
     * This method is used to handle nested properties for user.
     *
     * @param entity the entity
     */
    protected  void handleUserNestedProperties(User entity) {
        Helper.checkEntity(entity.getCountry(), "entity.country");
        Helper.checkEntity(entity.getState(), "entity.state");
    }

    /**
     * This method is used to handle nested properties for institution user.
     *
     * @param entity the entity
     * @param <T> the entity that extends institution user
     */
    protected <T extends InstitutionUser> void handleInstitutionUserNestedProperties(T entity) {

        if (entity.getPersonalInterests() != null) {
            entity.getPersonalInterests().forEach(p -> p.setUser(entity));
        } else {
            entity.setPersonalInterests(Collections.emptyList());
        }

        if (entity.getProfessionalInterests() != null) {
            entity.getProfessionalInterests().forEach(p -> p.setUser(entity));
        } else {
            entity.setProfessionalInterests(Collections.emptyList());
        }
    }

    /**
     * This method is used to handle nested properties for goal.
     *
     * @param entity the entity
     */
    protected  void handleGoalNestedProperties(Goal entity) {
        Helper.checkEntity(entity.getGoalCategory(), "entity.goalCategory");
        Helper.checkPositive(entity.getNumber(), "entity.number");
        if (entity.getCustomData() != null) {
            entity.getCustomData().setGoal(entity);
        }
        if (entity.getTasks() != null) {
            entity.getTasks()
                  .forEach(t -> {
                      t.setGoal(entity);
                      t.setGoalId(entity.getId());
                      if (t.getCustomData() != null) {
                          t.getCustomData().setTask(t);
                      }
                  });
        } else {
            entity.setTasks(Collections.emptyList());
        }
    }

    /**
     * This method is used to to remap paging options to spring pageable data.
     *
     * @param paging the paging options
     * @return the spring pageable data
     */
    private Pageable remapPaging(Paging paging) {
        if (paging == null || (paging.getPageNumber() == 0 && paging.getPageSize() == 0)) {
            return null;
        }
        Sort.Direction direction = remapSortDirection(paging);
        if (paging.getSortColumn() != null) {
            return new PageRequest(paging.getPageNumber(), paging.getPageSize(), direction, paging.getSortColumn());
        }
        return new PageRequest(paging.getPageNumber(), paging.getPageSize(), direction, ID);
    }

    /**
     * This method is used to to remap paging options to spring sort data.
     *
     * @param paging the paging options
     * @return the spring sort data
     */
    private Sort remapSort(Paging paging) {
        return new Sort(remapSortDirection(paging), ID);
    }

    /**
     * This method is used to to remap paging options to spring sort data direction.
     *
     * @param paging the paging options
     * @return the spring sort data direction
     */
    private Sort.Direction remapSortDirection(Paging paging) {
        return paging.getSortOrder() == SortOrder.DESC ? Sort.Direction.DESC : Sort.Direction.ASC;
    }

    /**
     * This method is used to to remap spring page data to SearchResult.
     *
     * @param pageResult the spring page result
     * @return the search result
     */
    private SearchResult<T> remapResult(Page<T> pageResult) {
        SearchResult<T> result = new SearchResult<>();
        result.setEntities(pageResult.getContent());
        result.setTotal(pageResult.getTotalElements());
        result.setTotalPages(pageResult.getTotalPages());
        return result;
    }

    /**
     * This method is used to to remap list result from spring data to SearchResult.
     *
     * @param listResult the list result
     * @return the search result
     */
    private SearchResult<T> remapResult(List<T> listResult) {
        SearchResult<T> result = new SearchResult<>();
        result.setEntities(listResult);
        result.setTotal(listResult.size());
        result.setTotalPages(listResult.isEmpty() ? 0 : 1);
        return result;
    }

    /**
     * Check whether an identifiable entity with a given id exists.
     *
     * @param id the id of entity
     * @return the match entity
     * @throws IllegalArgumentException if id is not positive
     * @throws EntityNotFoundException if the match entity can not be found in DB
     */
    private T ensureEntityExist(long id) throws EntityNotFoundException {
        Helper.checkPositive(id, "id");
        T entity = repository.findOne(id);
        if (entity == null) {
            throw new EntityNotFoundException(CustomMessageSource.getMessage("entity.notFound.byId", id));
        }
        return entity;
    }
}

