package com.wiproevents.services.springdata;

import com.wiproevents.entities.UserPreference;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The User repository.
 */
@Repository
public interface UserPreferenceRepository extends DocumentDbSpecificationRepository<UserPreference, String> {
    List<UserPreference> findByUserId(String userId);
}

