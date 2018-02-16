package com.wiproevents.entities;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import com.wiproevents.entities.briefs.EventBrief;
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
public class Session extends AuditableUserEntity {
    private EventBrief event;
    private String dayAgendaId;
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
    private List<SessionFile> files = new ArrayList<>();
    private SessionStatus status;
}
