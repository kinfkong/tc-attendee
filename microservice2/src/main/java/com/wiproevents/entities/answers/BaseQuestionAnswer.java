package com.wiproevents.entities.answers;

import com.wiproevents.entities.AnswerOption;
import com.wiproevents.entities.IdentifiableEntity;
import com.wiproevents.entities.briefs.UserBrief;
import com.wiproevents.entities.questions.BaseQuestion;

import java.util.List;

/**
 * Created by wangjinggang on 2018/2/16.
 */
public class BaseQuestionAnswer<T extends BaseQuestion> extends IdentifiableEntity {
    private T question;
    private UserBrief user;
    private List<AnswerOption> selectedAnswerOptions;
}
