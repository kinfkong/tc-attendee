package com.wiproevents.services.springdata;

import com.wiproevents.entities.UserRole;
import com.wiproevents.entities.UserRoleSearchCriteria;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.UserRoleService;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of UserService,
 * extends BaseService<User,UserSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class UserRoleServiceImpl extends BaseService<UserRole, UserRoleSearchCriteria> implements UserRoleService {

    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws AttendeeException if any other error occurred during operation
     */
    protected DocumentDbSpecification<UserRole> getSpecification(UserRoleSearchCriteria criteria) throws AttendeeException {
        return new UserRoleSpecification(criteria);
    }

    @Override
    public UserRole create(UserRole entity) throws AttendeeException {
        // validate the existence of the name
        UserRoleSearchCriteria criteria = new UserRoleSearchCriteria();
        criteria.setName(entity.getName());
        if (search(criteria, null).getEntities().size() > 0) {
            throw new IllegalArgumentException("The role with name " + entity.getName() + " already exists.");
        }
        return super.create(entity);
    }

}

