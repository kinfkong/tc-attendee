package com.wiproevents.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * The forgot password.
 */
@Getter
@Setter
public class ForgotPassword extends IdentifiableEntity {
    /**
     * The user id.
     */
    @JoinColumn(name = "user_id")
    private String userId;

    /**
     * The token.
     */
    private String token;

    /**
     * The expired on date.
     */
    @Temporal(TIMESTAMP)
    private Date expiredOn;
}

