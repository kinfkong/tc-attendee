package com.wiproevents.services.springdata;

import com.wiproevents.entities.User;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * The User repository.
 */
@Repository
public interface UserRepository extends DocumentDbSpecificationExecutor<User, String> {
}

