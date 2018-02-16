package com.wiproevents.services.springdata;

import com.wiproevents.entities.UserEventAssignment;
import com.wiproevents.entities.criteria.UserEventAssignmentSearchCriteria;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.UserEventAssignmentService;
import com.wiproevents.services.UserService;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of UserService,
 * extends BaseService<User,UserSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class UserEventAssignmentServiceImpl extends BaseService<UserEventAssignment, UserEventAssignmentSearchCriteria> implements UserEventAssignmentService {

    @Autowired
    private UserService userService;

    @Override
    protected DocumentDbSpecification<UserEventAssignment> getSpecification(UserEventAssignmentSearchCriteria criteria) throws AttendeeException {
        return new UserEventAssignmentSpecification(criteria);
    }


}

