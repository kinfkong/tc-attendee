package com.wiproevents.entities.questions;

import com.wiproevents.entities.AnswerOption;
import com.wiproevents.entities.IdentifiableEntity;

import java.util.List;

/**
 * Created by wangjinggang on 2018/2/15.
 */
public class BaseQuestion extends IdentifiableEntity {
    private String text;
    private int orderNumber;
    private boolean multiSelectAllowed;
    private List<AnswerOption> answerOptions;
}
