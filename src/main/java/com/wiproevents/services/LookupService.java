package com.wiproevents.services;

import com.wiproevents.entities.UserRole;
import com.wiproevents.exceptions.AttendeeException;

import java.util.List;

/**
 * The lookup service.Implementation should be effectively thread-safe.
 */
public interface LookupService {
    /**
     * This method is used to get user role lookups.
     *
     * @return the lookups for user role.
     * @throws AttendeeException if any other error occurred during operation
     */
   List<UserRole> getUserRoles() throws AttendeeException;
}

