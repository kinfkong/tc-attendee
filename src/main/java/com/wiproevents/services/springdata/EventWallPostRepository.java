package com.wiproevents.services.springdata;

import com.wiproevents.entities.EventWallPost;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;
import org.springframework.stereotype.Repository;

/**
 * The User repository.
 */
@Repository
public interface EventWallPostRepository extends DocumentDbSpecificationRepository<EventWallPost, String> {
}

