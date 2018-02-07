package com.wiproevents.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by wangjinggang on 2018/2/7.
 */
@Getter
@Setter
public class EventBrief extends IdentifiableEntity {
    private String name;
    private Date startDate;
    private Date endDate;
}
