package com.wiproevents.entities.answers;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import com.wiproevents.entities.briefs.SurveyBrief;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by wangjinggang on 2018/2/15.
 */
@Document(collection = "user_survey_answer")
@Getter
@Setter
public class UserSurveyAnswer extends BaseAnswer<UserSurveyQuestionAnswer> {
    private SurveyBrief poll;
}
