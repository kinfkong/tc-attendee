package com.wiproevents.services.springdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by wangjinggang on 2018/2/8.
 */
@Component
public class RepositoryRelation {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private FileEntityRepository fileEntityRepository;

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @Autowired
    private EventDayAgendaRepository eventDayAgendaRepository;

    @Autowired
    private EventBriefRepository eventBriefRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileCategoryRepository fileCategoryRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private NotificationTypeRepository notificationTypeRepository;

    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    @Autowired
    private UserEventAssignmentRepository userEventAssignmentRepository;

    @Autowired
    private SessionAttendeeRepository sessionAttendeeRepository;

    @PostConstruct
    public void handleRepositoryRelations() {
        handleEventRepository();
        handleEventCategoryRepository();
        handleEventDayAgendaRepository();
        handleSessionRepository();
        handleUserRoleRepository();
        handleUserRepository();
        handleUserPreferenceRepository();
        handleUserEventAssignmentRepository();
        handleSessionAttendeeRepository();
    }

    private void handleEventRepository() {
        eventRepository.addNestedRepository("location.country", countryRepository);
        eventRepository.addNestedRepository("galleryImages", fileEntityRepository);
        eventRepository.addNestedRepository("splashScreenFile", fileEntityRepository);
        eventRepository.addNestedRepository("imageThumbnailFile", fileEntityRepository);
        eventRepository.addNestedRepository("type", eventTypeRepository);
        eventRepository.addNestedRepository("category", eventCategoryRepository);
    }

    private void handleEventCategoryRepository() {
        eventCategoryRepository.addNestedRepository("logo", fileEntityRepository);
    }

    private void handleEventDayAgendaRepository() {
        eventDayAgendaRepository.addNestedRepository("breaks[*].mapImage", fileEntityRepository);
        eventDayAgendaRepository.addNestedRepository("event", eventBriefRepository);
    }

    private void handleSessionRepository() {
        sessionRepository.addNestedRepository("event", eventBriefRepository);
        sessionRepository.addNestedRepository("galleryImages", fileEntityRepository);
        sessionRepository.addNestedRepository("mapImage", fileEntityRepository);
        sessionRepository.addNestedRepository("assignedSpeakers", userRepository);
        sessionRepository.addNestedRepository("files[*].files", fileEntityRepository);
        sessionRepository.addNestedRepository("files[*].category", fileCategoryRepository);
    }

    private void handleUserRoleRepository() {
        userRoleRepository.addNestedRepository("permissions", userPermissionRepository);
    }

    private void handleUserRepository() {
        userRepository.addNestedRepository("roles", userRoleRepository);
        userRepository.addNestedRepository("designation", designationRepository);
    }

    private void handleUserPreferenceRepository() {
        userPreferenceRepository.addNestedRepository("notificationMethodPreferences[*].notificationTypesCovered", notificationTypeRepository);
    }

    private void handleUserEventAssignmentRepository() {
        userEventAssignmentRepository.addNestedRepository("user", userRepository);
        userEventAssignmentRepository.addNestedRepository("event", eventRepository);
        userEventAssignmentRepository.addNestedRepository("roles", userRoleRepository);
    }

    private void handleSessionAttendeeRepository() {
        sessionAttendeeRepository.addNestedRepository("user", userRepository);
        sessionAttendeeRepository.addNestedRepository("session", sessionRepository);
    }
}
