package com.wiproevents.entities;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import com.wiproevents.entities.statuses.EmailStatus;
import com.wiproevents.entities.statuses.NotificationStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangjinggang on 2018/2/16.
 */
@Document(collection = "email")
@Getter
@Setter
public class Email extends AuditableUserEntity {
    private List<String> emails = new ArrayList<>();
    private String title;
    private String text;
    private boolean scheduled;
    private Date dateScheduled;
    private Timezone timezone;
    private EmailStatus status;
}
