package com.wiproevents.services.springdata;

import com.wiproevents.entities.NotificationType;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationExecutor;

/**
 * Created by wangjinggang on 2018/2/6.
 */
public interface NotificationTypeRepository extends DocumentDbSpecificationExecutor<NotificationType, String> {
}
