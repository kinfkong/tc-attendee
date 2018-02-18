package com.wiproevents.entities;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import com.wiproevents.entities.questions.QuizQuestion;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjinggang on 2018/2/17.
 */
@Getter
@Setter
@Document(collection = "quiz")
public class Quiz extends BaseEngagement {
    private List<QuizQuestion> questions = new ArrayList<>();
}
