package com.wiproevents.entities;

import lombok.Getter;
import lombok.Setter;


/**
 * The user search criteria.
 */
@Getter
@Setter
public class EventCategorySearchCriteria extends BaseSearchCriteria {
    private String name;
}

