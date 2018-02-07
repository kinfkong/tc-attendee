package com.wiproevents.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjinggang on 2018/2/7.
 */
@Getter
@Setter
public class FeedbackSurveyQuestion extends IdentifiableEntity {
    private String surveyId;
    private String text;
    private String type;
    private int orderNumber;
    private List<AnswerOption> answerOptions = new ArrayList<>();

}
