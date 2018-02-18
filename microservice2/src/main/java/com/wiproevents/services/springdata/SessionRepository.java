package com.wiproevents.services.springdata;

import com.wiproevents.entities.Session;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;
import org.springframework.stereotype.Repository;

/**
 * The User repository.
 */
@Repository
public interface SessionRepository extends DocumentDbSpecificationRepository<Session, String> {

}

