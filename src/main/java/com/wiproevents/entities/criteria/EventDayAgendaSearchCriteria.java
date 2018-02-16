package com.wiproevents.entities.criteria;

import com.wiproevents.entities.criteria.BaseSearchCriteria;
import lombok.Getter;
import lombok.Setter;


/**
 * The user search criteria.
 */
@Getter
@Setter
public class EventDayAgendaSearchCriteria extends BaseSearchCriteria {
    private String eventId;
}

