package com.wiproevents.entities;

import com.wiproevents.entities.types.NotificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjinggang on 2018/2/6.
 */
@Getter
@Setter
@NoArgsConstructor
public class NotificationMethodPreference implements Model {
    private String userId;
    private List<NotificationType> notificationTypesCovered = new ArrayList<>();
    private String notificationMethod;
}
