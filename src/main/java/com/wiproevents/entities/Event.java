package com.wiproevents.entities;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import com.wiproevents.entities.statuses.EventStatus;
import com.wiproevents.entities.types.EventType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangjinggang on 2018/2/7.
 */
@Getter
@Setter
@Document(collection = "event")
public class Event extends AuditableUserEntity {
    private boolean template;
    private String name;
    private String description;

    private Date startDate;

    private Date endDate;

    private Date registrationStartDate;

    private Date registrationEndDate;

    private Location location;

    private List<EventInvitation> invitations = new ArrayList<>();

    private List<FileEntity> galleryImages = new ArrayList<>();

    private boolean splashScreenRequired;
    private FileEntity splashScreenFile;
    private FileEntity imageThumbnailFile;
    private String headerColor;
    private EventCategory category;
    private EventType type;
    private boolean souvenirsProvided;
    private List<Souvenir> souvenirs = new ArrayList<>();
    private List<TicketOption> ticketOptions = new ArrayList<>();
    private List<PaymentOption> paymentOptions = new ArrayList<>();
    private List<EventDayAgenda> dayAgendas = new ArrayList<>();
    private EventStatus status;

}
