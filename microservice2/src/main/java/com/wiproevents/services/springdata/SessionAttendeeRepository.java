package com.wiproevents.services.springdata;

import com.wiproevents.entities.SessionAttendee;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;

/**
 * Created by wangjinggang on 2018/2/10.
 */
public interface SessionAttendeeRepository extends DocumentDbSpecificationRepository<SessionAttendee, String>  {
}
