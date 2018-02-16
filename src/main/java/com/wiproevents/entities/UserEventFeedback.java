package com.wiproevents.entities;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import com.wiproevents.entities.answers.UserSurveyQuestionAnswer;
import com.wiproevents.entities.briefs.EventBrief;
import com.wiproevents.entities.briefs.UserBrief;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by wangjinggang on 2018/2/16.
 */
@Getter
@Setter
@Document(collection = "user_event_feedback")
public class UserEventFeedback extends AuditableEntity {
    private EventBrief event;
    private UserBrief user;
    private UserSurveyQuestionAnswer feedback;
    private int eventRating;
    private String comment;
    private boolean completed;
}
