package com.wiproevents.entities.criteria;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


/**
 * The user search criteria.
 */
@Getter
@Setter
public class EventSearchCriteria extends BaseSearchCriteria {
    private String name;
    private Integer durationDays;
    private Date startDateBefore;
    private Date startDateAfter;
    private Date endDateBefore;
    private Date endDateAfter;
    private Date registrationStartDateBefore;
    private Date registrationStartDateAfter;
    private Date registrationEndDateBefore;
    private Date registrationEndDateAfter;
}

