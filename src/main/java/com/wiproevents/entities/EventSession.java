package com.wiproevents.entities;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangjinggang on 2018/2/7.
 */
@Document(collection = "event_session")
@Getter
@Setter
public class EventSession extends AuditableUserEntity {
    private EventBrief event;
    private String name;
    private String venue;
    private String building;
    private String rooms;
    private FileEntity mapImage;
    private Date startTime;
    private Date endTime;
    private int seatCapability;
    private List<User> assignedSpeakers = new ArrayList<>();
    private List<Speaker> addedSpeakers = new ArrayList<>();
    private List<FileEntity> galleryImages = new ArrayList<>();
    private List<EventSessionFile> files = new ArrayList<>();
    private List<EventSessionFeedbackSurvey> feedbackSurvey = new ArrayList<>();
    private EventSessionStatus status;

}
