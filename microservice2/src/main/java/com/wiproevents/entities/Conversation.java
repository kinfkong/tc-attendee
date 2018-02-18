package com.wiproevents.entities;

import com.wiproevents.entities.briefs.SessionBrief;
import com.wiproevents.entities.briefs.UserBrief;
import com.wiproevents.entities.statuses.ConversationStatus;
import com.wiproevents.entities.types.ConversationType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjinggang on 2018/2/16.
 */
public class Conversation extends AuditableUserEntity {
    private List<UserBrief> participants = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();
    private ConversationStatus status;
    private ConversationType type;
    private SessionBrief session;
}
