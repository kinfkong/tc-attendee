package com.livingprogress.mentorme.services;

import com.livingprogress.mentorme.entities.*;
import com.livingprogress.mentorme.exceptions.MentorMeException;

import java.util.List;

/**
 * The lookup service.Implementation should be effectively thread-safe.
 */
public interface LookupService {
    /**
     * This method is used to get user role lookups.
     *
     * @return the lookups for user role.
     * @throws MentorMeException if any other error occurred during operation
     */
   List<UserRole> getUserRoles() throws MentorMeException;
}

