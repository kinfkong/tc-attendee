package com.wiproevents.services.springdata;

import com.wiproevents.entities.ForgotPassword;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;

/**
 * The ForgotPassword repository.
 */
public interface ForgotPasswordRepository extends DocumentDbSpecificationRepository<ForgotPassword, String> {
    /**
     * This method is used to get the ForgotPassword by token.
     * @param token the reset password token
     * @return the forgot password
     */
    ForgotPassword findByToken(String token);

    /**
     * Get count by user id.
     * @param userId the user id.
     * @return the count of forgot password entities by user id.
     */
    long countByUserId(String userId);


    /**
     * Delete all forgot passwords by user id.
     * @param userId the user id.
     */
    void deleteByUserId(String userId);
}

