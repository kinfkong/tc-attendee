package com.wiproevents.entities.answers;

import com.wiproevents.entities.AuditableUserEntity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FAQQuestionAnswer extends AuditableUserEntity {
    private String eventId;

    private String questionText;
    private String answerText;
    private int orderNumber;
}
