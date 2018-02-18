package com.wiproevents.entities;

import com.wiproevents.entities.statuses.NotificationStatus;
import com.wiproevents.entities.types.NotificationType;

import java.util.Date;

/**
 * Created by wangjinggang on 2018/2/16.
 */
public class Notification extends IdentifiableEntity {
    private String userId;
    private String title;
    private String text;
    private NotificationType type;
    private NotificationStatus status;
    private Date createdOn;
    private Date readOn;
}
