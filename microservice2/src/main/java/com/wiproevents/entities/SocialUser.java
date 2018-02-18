package com.wiproevents.entities;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by wangjinggang on 2018/2/9.
 */
@Document(collection = "social_user")
@Getter
@Setter
public class SocialUser extends AuditableEntity {
    private String userId;
    private String providerUserId;
    private String providerId;
}
