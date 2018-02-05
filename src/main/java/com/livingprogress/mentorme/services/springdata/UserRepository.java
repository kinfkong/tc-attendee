package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.User;
import com.livingprogress.mentorme.utils.springdata.extensions.DocumentDbSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * The User repository.
 */
@Repository
public interface UserRepository extends DocumentDbSpecificationExecutor<User, String> {
}

