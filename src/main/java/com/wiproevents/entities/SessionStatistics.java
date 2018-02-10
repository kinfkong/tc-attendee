package com.wiproevents.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Created by wangjinggang on 2018/2/10.
 */
@Getter
@Setter
public class SessionStatistics extends IdentifiableEntity {
    private EventBrief event;
    private String name;
    private List<String> speakers;
    private SessionStatus activityStatus;
    private Date startTime;
    private Date endTime;
    private int numberOfRegistered;
    private int numberOfWaitlisted;
    private boolean timeIsConflicted;
}
