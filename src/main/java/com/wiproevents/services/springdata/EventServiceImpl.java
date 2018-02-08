package com.wiproevents.services.springdata;

import com.microsoft.azure.spring.data.documentdb.repository.DocumentDbRepository;
import com.wiproevents.entities.Event;
import com.wiproevents.entities.EventInvitation;
import com.wiproevents.entities.EventSearchCriteria;
import com.wiproevents.entities.Location;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.services.EventService;
import com.wiproevents.utils.Helper;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The Spring Data JPA implementation of UserService,
 * extends BaseService<User,UserSearchCriteria>. Effectively thread safe after configuration.
 */
@Service
public class EventServiceImpl extends BaseService<Event, EventSearchCriteria> implements EventService {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private FileEntityRepository fileEntityRepository;

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;



    /**
     * This method is used to get the specification.
     *
     * @param criteria the search criteria
     * @return the specification
     * @throws AttendeeException if any other error occurred during operation
     */
    protected DocumentDbSpecification<Event> getSpecification(EventSearchCriteria criteria) throws AttendeeException {
        return new EventSpecification(criteria);
    }

    @Override
    protected Map<String, DocumentDbRepository<?, String>> getNestedRepositories() {
        Map<String, DocumentDbRepository<?, String>> result = new HashMap<>();
        result.put("location.country", countryRepository);
        result.put("galleryImages", fileEntityRepository);
        result.put("splashScreenFile", fileEntityRepository);
        result.put("imageThumbnailFile", fileEntityRepository);
        result.put("type", eventTypeRepository);
        result.put("category", eventCategoryRepository);
        return result;
    }


    @Override
    protected void handleNestedUpdate(Event entity, Event oldEntity) throws AttendeeException {
        // handle location
        handleNested(entity);

        Location loc = entity.getLocation();

        String userId = Helper.getAuthUser().getId();

        if (loc != null) {
            Location oldLoc = oldEntity.getLocation();
            if (oldLoc != null) {
                loc.setCreatedBy(oldLoc.getCreatedBy());
                loc.setCreatedOn(oldLoc.getCreatedOn());
                if (Helper.isUpdated(loc, oldLoc)) {
                    loc.setUpdatedOn(new Date());
                    loc.setUpdatedBy(userId);
                } else {
                    loc.setUpdatedBy(oldLoc.getUpdatedBy());
                    loc.setUpdatedOn(oldLoc.getUpdatedOn());
                }
            } else {
                loc.setCreatedBy(userId);
                loc.setCreatedOn(new Date());

                loc.setUpdatedBy(userId);
                loc.setUpdatedOn(new Date());
            }
        }
    }

    @Override
    protected void handleNestedCreate(Event entity) throws AttendeeException {
        // handle location
        handleNested(entity);

        Location loc = entity.getLocation();
        String userId = Helper.getAuthUser().getId();
        if (loc != null) {
            loc.setCreatedBy(userId);
            loc.setCreatedOn(new Date());
            loc.setUpdatedBy(userId);
            loc.setUpdatedOn(new Date());
        }
    }

    private void handleNested(Event entity) throws AttendeeException {
        if (entity.getInvitations() != null) {
            for (EventInvitation eventInvitation : entity.getInvitations()) {
                eventInvitation.setEventId(entity.getId());
            }
        }
    }
}

