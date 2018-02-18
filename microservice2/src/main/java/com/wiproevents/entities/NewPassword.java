package com.wiproevents.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * The new password request.
 */
@Getter
@Setter
public class NewPassword  {

    private String email;

    /**
     * The token.
     */
    private String forgotPasswordToken;

    private String oldPassword;

    /**
     * The new password.
     */
    private String newPassword;
}

