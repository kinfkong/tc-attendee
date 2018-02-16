package com.wiproevents.entities;

import com.wiproevents.entities.statuses.PollStatus;
import com.wiproevents.entities.types.PollType;

import java.util.Date;

/**
 * Created by wangjinggang on 2018/2/15.
 */
public class Poll extends IdentifiableEntity {
    private String name;
    private String eventId;
    private String context;
    private String sessionId;
    private PollStatus status;
    private PollType type;
    private Date startTime;
    private Date endTime;

}
