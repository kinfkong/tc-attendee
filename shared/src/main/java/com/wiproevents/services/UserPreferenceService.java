package com.wiproevents.services;

import com.wiproevents.entities.criteria.BaseSearchCriteria;
import com.wiproevents.entities.UserPreference;
import com.wiproevents.exceptions.AttendeeException;

/**
 * The user service. Extends generic service interface.Implementation should be effectively thread-safe.
 */
public interface UserPreferenceService extends GenericService<UserPreference, BaseSearchCriteria> {
    UserPreference findByUserId(String userId) throws AttendeeException;
}

