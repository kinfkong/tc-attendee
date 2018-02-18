package com.wiproevents.entities;

import com.wiproevents.entities.statuses.EngagementStatus;
import com.wiproevents.entities.types.EngagementContextType;
import com.wiproevents.entities.types.EngagementType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by wangjinggang on 2018/2/17.
 */
@Getter
@Setter
public class BaseEngagement extends IdentifiableEntity {
    private String name;
    private EngagementContextType context;
    private String eventId;
    private String sessionId;
    private String description;
    private EngagementStatus status;
    private EngagementType type;
    private Date startTime;
    private Date endTime;
}
