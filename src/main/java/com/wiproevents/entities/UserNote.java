package com.wiproevents.entities;

import com.wiproevents.entities.briefs.EventBrief;
import com.wiproevents.entities.briefs.SessionBrief;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjinggang on 2018/2/16.
 */
public class UserNote extends AuditableEntity {
    private String userId;
    private SessionBrief session;
    private EventBrief event;
    private String text;
    private List<FileEntity> attachedFiles = new ArrayList<>();
}
