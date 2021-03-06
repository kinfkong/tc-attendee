package com.wiproevents.services.springdata;

import com.wiproevents.entities.EventDayAgenda;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;
import org.springframework.stereotype.Repository;

/**
 * The User repository.
 */
@Repository
public interface EventDayAgendaRepository extends DocumentDbSpecificationRepository<EventDayAgenda, String> {

}

