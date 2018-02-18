package com.wiproevents.services.springdata;

import com.wiproevents.entities.SocialUser;
import com.wiproevents.entities.criteria.SocialUserSearchCriteria;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.SocialUserService;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import org.springframework.stereotype.Service;

/**
 * The Spring Data JPA implementation of UserService,
 * extends BaseService<User,UserSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class SocialUserServiceImpl extends BaseService<SocialUser, SocialUserSearchCriteria> implements SocialUserService {


    @Override
    protected DocumentDbSpecification<SocialUser> getSpecification(SocialUserSearchCriteria criteria) throws AttendeeException {
        return new SocialUserSpecification(criteria);
    }
}

