package com.wiproevents.services.springdata;

import com.wiproevents.entities.VerifyEmailToken;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;

import java.util.List;

/**
 * The ForgotPassword repository.
 */
public interface VerifyEmailTokenRepository extends DocumentDbSpecificationRepository<VerifyEmailToken, String> {
    /**
     * This method is used to get the ForgotPassword by token.
     * @param token the reset password token
     * @return the forgot password
     */
    List<VerifyEmailToken> findByToken(String token);
}

