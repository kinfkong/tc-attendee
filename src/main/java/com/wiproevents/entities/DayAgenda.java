package com.wiproevents.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjinggang on 2018/2/7.
 */
@Getter
@Setter
public class DayAgenda extends AuditableUserEntity {
    private EventBrief event;
    private int day;
    private List<EventSession> sessions = new ArrayList<>();
    private List<AgendaBreak> breaks = new ArrayList<>();
}
