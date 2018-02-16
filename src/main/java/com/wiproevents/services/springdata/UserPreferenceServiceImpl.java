package com.wiproevents.services.springdata;

import com.wiproevents.entities.criteria.BaseSearchCriteria;
import com.wiproevents.entities.UserPreference;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.exceptions.EntityNotFoundException;
import com.wiproevents.services.UserPreferenceService;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        // populate it use get
        return this.get(userPreferences.get(0).getId());
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
            if (notification.getUserId() == null) {
                notification.setUserId(entity.getUserId());
            }
            if (!notification.getUserId().equals(entity.getUserId())) {
                throw new IllegalArgumentException("The user id in notification method preference is not correct.");
            }
        });

        return super.update(id, entity);
    }
}

