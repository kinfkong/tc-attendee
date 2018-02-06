package com.wiproevents.services.springdata;

import com.wiproevents.entities.BaseSearchCriteria;
import com.wiproevents.entities.NotificationType;
import com.wiproevents.entities.UserPreference;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.exceptions.EntityNotFoundException;
import com.wiproevents.services.UserPreferenceService;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * The Spring Data JPA implementation of UserService,
 * extends BaseService<User,UserSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class UserPreferenceServiceImpl extends BaseService<UserPreference, BaseSearchCriteria> implements UserPreferenceService {

    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    @Autowired
    private NotificationTypeRepository notificationTypeRepository;

    @Override
    public UserPreference findByUserId(String userId) throws AttendeeException {
        List<UserPreference> userPreferences = userPreferenceRepository.findByUserId(userId);
        if (userPreferences.isEmpty()) {
            throw new EntityNotFoundException("no user preference found");
        }
        return userPreferences.get(0);
    }

    @Override
    protected DocumentDbSpecification<UserPreference> getSpecification(BaseSearchCriteria criteria) throws AttendeeException {
        return new UserPreferenceSpecification(criteria);
    }

    @Override
    public UserPreference update(String id, UserPreference entity) throws AttendeeException {

        // update the nested lookup fields
        entity.getNotificationMethodPreferences().forEach(notification -> {
            // make sure the user id is correct
            if (!notification.getUserId().equals(entity.getUserId())) {
                throw new IllegalArgumentException("The user id in notification method preference is not correct.");
            }
            ArrayList<NotificationType> dbNotificationTypes = new ArrayList<>();
            notification.getNotificationTypesCovered().forEach(notificationType -> {
                NotificationType dbNotificationType = notificationTypeRepository.findOne(notificationType.getId());
                if (dbNotificationType == null) {
                    throw new IllegalArgumentException(
                            "notification type of id: " + notificationType.getId() + " not exists");
                }
                dbNotificationTypes.add(dbNotificationType);
            });
            notification.setNotificationTypesCovered(dbNotificationTypes);
        });

        return super.update(id, entity);
    }
}

