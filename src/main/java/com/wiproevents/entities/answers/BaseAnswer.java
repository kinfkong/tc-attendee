package com.wiproevents.entities.answers;

import com.wiproevents.entities.IdentifiableEntity;
import com.wiproevents.entities.briefs.UserBrief;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BaseAnswer<T extends BaseQuestionAnswer> extends IdentifiableEntity {
    private UserBrief user;
    List<T> userAnswers = new ArrayList<>();
    private boolean completed;
}