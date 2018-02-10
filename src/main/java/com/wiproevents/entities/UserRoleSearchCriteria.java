package com.wiproevents.entities;

import lombok.Getter;
import lombok.Setter;


/**
 * The user search criteria.
 */
@Getter
@Setter
public class UserRoleSearchCriteria extends BaseSearchCriteria {
    private String name;
}

