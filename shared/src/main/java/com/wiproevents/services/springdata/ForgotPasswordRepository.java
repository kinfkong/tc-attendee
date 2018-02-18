package com.wiproevents.services.springdata;

import com.wiproevents.entities.ForgotPassword;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;

import java.util.List;

/**
 * The ForgotPassword repository.
 */
public interface ForgotPasswordRepository extends DocumentDbSpecificationRepository<ForgotPassword, String> {
    /**
     * This method is used to get the ForgotPassword by token.
     * @param token the reset password token
     * @return the forgot password
     */
    List<ForgotPassword> findByToken(String token);

}

