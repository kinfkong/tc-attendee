/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.entities.criteria;

import lombok.Getter;
import lombok.Setter;


/**
 * The user event feedback search criteria.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Getter
@Setter
public class UserEventFeedbackSearchCriteria extends BaseSearchCriteria {
    private String name;
}

