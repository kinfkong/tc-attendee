package com.wiproevents.entities;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The user.
 */
@Getter
@Setter
@Document(collection = "user_preference")
public class UserPreference extends AuditableEntity {
    private String userId;
    private List<NotificationMethodPreference> notificationMethodPreferences = new ArrayList<>();
    private List<String> rooms = new ArrayList<>();
    private List<String> food = new ArrayList<>();
}

