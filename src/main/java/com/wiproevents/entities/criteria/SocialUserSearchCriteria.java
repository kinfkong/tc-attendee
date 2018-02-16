package com.wiproevents.entities.criteria;

import com.wiproevents.entities.criteria.BaseSearchCriteria;
import lombok.Getter;
import lombok.Setter;


/**
 * The user search criteria.
 */
@Getter
@Setter
public class SocialUserSearchCriteria extends BaseSearchCriteria {
    private String providerId;
    private String providerUserId;
    private String userId;
}

