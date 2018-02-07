package com.wiproevents.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by wangjinggang on 2018/2/7.
 */
@Getter
@Setter
public class AnswerOption extends IdentifiableEntity {
    private String questionId;
    private String text;
    private int orderNumber;
}
