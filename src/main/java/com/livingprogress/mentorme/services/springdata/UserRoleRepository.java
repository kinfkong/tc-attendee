package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.UserRole;
import com.livingprogress.mentorme.utils.springdata.extensions.DocumentDbSpecificationExecutor;

/**
 * The UserRole repository.
 */
public interface UserRoleRepository extends DocumentDbSpecificationExecutor<UserRole, String> {
}

