package com.wiproevents.entities.types;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import com.wiproevents.entities.LookupEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by wangjinggang on 2018/2/6.
 */
@Getter
@Setter
@Document(collection = "notification_type")
public class NotificationType extends LookupEntity {
    private String text;
    private String relatedModel;
}
