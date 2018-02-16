package com.wiproevents.services.springdata;

import com.wiproevents.entities.types.NotificationType;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;

/**
 * Created by wangjinggang on 2018/2/6.
 */
public interface NotificationTypeRepository extends DocumentDbSpecificationRepository<NotificationType, String> {
}
