package com.wiproevents.services.springdata;

import com.wiproevents.entities.briefs.EventBrief;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;
import org.springframework.stereotype.Repository;

/**
 * The User repository.
 */
@Repository
public interface EventBriefRepository extends DocumentDbSpecificationRepository<EventBrief, String> {

}

