package com.wiproevents.entities.answers;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import com.wiproevents.entities.briefs.QuizBrief;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by wangjinggang on 2018/2/15.
 */
@Document(collection = "user_quiz_answer")
@Getter
@Setter
public class UserQuizAnswer extends BaseAnswer<UserQuizQuestionAnswer> {
    private QuizBrief poll;
    private double score;
    private double totalWeight;
}
