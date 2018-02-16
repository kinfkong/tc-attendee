package com.wiproevents.entities.criteria;

import com.wiproevents.entities.criteria.BaseSearchCriteria;
import com.wiproevents.entities.statuses.SessionAttendeeStatus;
import lombok.Getter;
import lombok.Setter;


/**
 * The user search criteria.
 */
@Getter
@Setter
public class SessionAttendeeSearchCriteria extends BaseSearchCriteria {
    private String userId;
    private String sessionId;
    private SessionAttendeeStatus status;
}

