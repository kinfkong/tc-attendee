package com.wiproevents.services.springdata;

import com.wiproevents.entities.AccessToken;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;

import java.util.List;

/**
 * Created by wangjinggang on 2018/2/6.
 */
public interface AccessTokenRepository extends DocumentDbSpecificationRepository<AccessToken, String> {
    List<AccessToken> findByToken(String token);
}
