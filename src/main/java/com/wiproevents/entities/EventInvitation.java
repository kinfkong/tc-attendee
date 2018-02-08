package com.wiproevents.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by wangjinggang on 2018/2/7.
 */
@Getter
@Setter
public class EventInvitation implements Model {
    private String eventId;
    private String name;
    private String email;
    private String phone;
}
