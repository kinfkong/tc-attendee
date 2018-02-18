package com.wiproevents.entities;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import com.wiproevents.entities.questions.PollQuestion;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Document(collection = "poll")
public class Poll extends BaseEngagement {
    private List<PollQuestion> questions = new ArrayList<>();
}
