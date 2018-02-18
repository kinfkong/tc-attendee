package com.wiproevents.entities;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import com.wiproevents.entities.briefs.EventBrief;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjinggang on 2018/2/7.
 */
@Getter
@Setter
@Document(collection = "day_agenda")
public class EventDayAgenda extends AuditableUserEntity {
    private EventBrief event;
    private int day;
    private List<Session> sessions = new ArrayList<>();
    private List<AgendaBreak> breaks = new ArrayList<>();
}
