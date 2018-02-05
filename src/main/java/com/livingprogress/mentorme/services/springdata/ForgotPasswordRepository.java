package com.livingprogress.mentorme.services.springdata;

import com.livingprogress.mentorme.entities.ForgotPassword;
import com.livingprogress.mentorme.utils.springdata.extensions.DocumentDbSpecificationExecutor;

/**
 * The ForgotPassword repository.
 */
public interface ForgotPasswordRepository extends DocumentDbSpecificationExecutor<ForgotPassword, String> {
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

