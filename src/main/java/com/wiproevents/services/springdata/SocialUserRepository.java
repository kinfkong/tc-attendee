package com.wiproevents.services.springdata;

import com.wiproevents.entities.SocialUser;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;
import org.springframework.stereotype.Repository;

/**
 * The User repository.
 */
@Repository
public interface SocialUserRepository extends DocumentDbSpecificationRepository<SocialUser, String> {

}

