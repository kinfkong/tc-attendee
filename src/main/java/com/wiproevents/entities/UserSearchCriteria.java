package com.wiproevents.entities;

import lombok.Getter;
import lombok.Setter;


/**
 * The user search criteria.
 */
@Getter
@Setter
public class UserSearchCriteria {
    /**
     * The fullName.
     */
    private String fullName;

    /**
     * The email.
     */
    private String email;

    /**
     * The role.
     */
    private String userRoleId;

    private UserStatus status;
}

