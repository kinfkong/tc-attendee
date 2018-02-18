package com.wiproevents.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by wangjinggang on 2018/2/7.
 */
@Getter
@Setter
public class Speaker extends AuditableUserEntity {
    private String name;
    private String company;
    private String job;
    private String description;
    private String profilePictureURL;
}
