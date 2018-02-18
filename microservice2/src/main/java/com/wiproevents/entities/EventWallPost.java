package com.wiproevents.entities;

import com.wiproevents.entities.briefs.UserBrief;
import com.wiproevents.entities.statuses.EventWallPostStatus;

import java.util.List;

/**
 * Created by wangjinggang on 2018/2/16.
 */
public class EventWallPost extends AuditableEntity {
    private String parentPostId;
    private UserBrief user;
    private String text;
    private EventWallPostStatus status;
    private List<FileEntity> attachedFiles;
    private List<String> childPosts;
    private List<Integer> likeFromUsers;
}
