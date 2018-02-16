package com.wiproevents.entities;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import com.wiproevents.entities.questions.SurveyQuestion;
import com.wiproevents.entities.statuses.SurveyStatus;
import com.wiproevents.entities.types.SurveyType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangjinggang on 2018/2/16.
 */
@Document(collection = "survey")
@Getter
@Setter
public class Survey extends IdentifiableEntity {
    private String name;
    private String description;
    private SurveyType context;
    private String eventId;
    private String sessionId;
    private List<SurveyQuestion> questions = new ArrayList<>();
    private SurveyStatus status;
    private SurveyType type;
    private Date startTime;
    private Date endTime;

}
