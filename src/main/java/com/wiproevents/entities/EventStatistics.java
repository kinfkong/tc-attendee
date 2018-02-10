package com.wiproevents.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjinggang on 2018/2/10.
 */
@Getter
@Setter
public class EventStatistics {
    private String eventId;
    private String eventName;
    private int numberOfSessions;
    private int numberOfAttendees;
    private int numberOfSpeakers;
    private int numberOfApprovedAttendees;
    private int numberOfRejectedAttendees;
    private int numberOfTicketsLefts;
    private List<SessionStatistics> sessions = new ArrayList<>();
}
