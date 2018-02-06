package com.wiproevents.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by wangjinggang on 2018/2/6.
 */
@Getter
@Setter
public class EventCategoryLogo extends IdentifiableEntity {
    private String name;
    private String fileURL;
}
