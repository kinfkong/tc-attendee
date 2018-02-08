package com.wiproevents.services.springdata;

import com.wiproevents.entities.User;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The User repository.
 */
@Repository
public interface UserRepository extends DocumentDbSpecificationRepository<User, String> {
    List<User> findByEmail(String email);
}

