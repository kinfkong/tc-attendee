package com.wiproevents.entities;

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
public class NotificationMethodPreference {
    private String userId;
    private List<NotificationType> notificationTypesCovered = new ArrayList<>();
    private String notificationMethod;
}
