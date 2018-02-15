package com.wiproevents.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


/**
 * The user search criteria.
 */
@Getter
@Setter
public class SessionSearchCriteria extends BaseSearchCriteria {
    private String name;
    private String dayAgendaId;
    private String eventId;
    private Date startDateBefore;
    private Date startDateAfter;
    private Date endDateBefore;
    private Date endDateAfter;
}

