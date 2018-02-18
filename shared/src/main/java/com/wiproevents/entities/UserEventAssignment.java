package com.wiproevents.entities;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjinggang on 2018/2/7.
 */
@Getter
@Setter
@Document(collection = "user_event_assignment")
public class UserEventAssignment extends IdentifiableEntity {
    private User user;
    private Event event;
    private List<UserRole> roles = new ArrayList<>();
}
