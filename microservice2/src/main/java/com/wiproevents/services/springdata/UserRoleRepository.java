package com.wiproevents.services.springdata;


import com.wiproevents.entities.UserRole;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;

import java.util.List;

/**
 * The UserRole repository.
 */
public interface UserRoleRepository extends DocumentDbSpecificationRepository<UserRole, String> {
    List<UserRole> findByName(String name);
}

