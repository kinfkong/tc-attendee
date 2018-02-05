package com.wiproevents.services.springdata;

import com.wiproevents.entities.UserRole;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationExecutor;

/**
 * The UserRole repository.
 */
public interface UserRoleRepository extends DocumentDbSpecificationExecutor<UserRole, String> {
}

