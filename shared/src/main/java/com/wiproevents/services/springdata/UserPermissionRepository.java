package com.wiproevents.services.springdata;

import com.wiproevents.entities.UserPermission;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;

/**
 * Created by wangjinggang on 2018/2/10.
 */
public interface UserPermissionRepository extends DocumentDbSpecificationRepository<UserPermission, String> {
}
