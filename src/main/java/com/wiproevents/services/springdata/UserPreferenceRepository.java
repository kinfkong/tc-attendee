package com.wiproevents.services.springdata;

import com.wiproevents.entities.UserPreference;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The User repository.
 */
@Repository
public interface UserPreferenceRepository extends DocumentDbSpecificationExecutor<UserPreference, String> {
    List<UserPreference> findByUserId(String userId);
}

