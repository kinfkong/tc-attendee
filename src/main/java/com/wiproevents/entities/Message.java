package com.wiproevents.entities;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import com.wiproevents.entities.briefs.UserBrief;
import com.wiproevents.entities.statuses.MessageStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjinggang on 2018/2/16.
 */
@Getter
@Setter
@Document(collection = "message")
public class Message extends AuditableUserEntity {
    private String conversationId;
    private UserBrief sender;
    private String text;
    private MessageStatus status;
    private List<FileEntity> attachedFiles = new ArrayList<>();
}
