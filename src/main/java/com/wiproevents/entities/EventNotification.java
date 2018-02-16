package com.wiproevents.entities;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import com.wiproevents.entities.types.NotificationType;
import com.wiproevents.entities.types.RecipientType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangjinggang on 2018/2/16.
 */
@Document(collection = "event_notification")
@Getter
@Setter
public class EventNotification extends IdentifiableEntity {
    private String eventId;
    private String title;
    private String text;
    private List<RecipientType> recipientTypes = new ArrayList<>();
    private NotificationType type;
    private Date createdOn;
    private String createdBy;
}
