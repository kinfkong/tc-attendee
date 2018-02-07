package com.wiproevents.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Created by wangjinggang on 2018/2/7.
 */
@Getter
@Setter
public class EventSessionFeedbackSurvey extends IdentifiableEntity {
    private String name;
    private String description;
    private String context;
    private String eventId;
    private String sessionId;
    private List<FeedbackSurveyQuestion> questions;
    private SurveyStatus status;
    private SurveyType type;
    private Date startTime;
    private Date endTime;
}
