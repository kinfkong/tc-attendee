package com.wiproevents.entities;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * The forgot password.
 */
@Getter
@Setter
@Document(collection = "forgot_password_token")
public class ForgotPassword extends IdentifiableEntity {

    private String userId;

    /**
     * The token.
     */
    private String token;

    /**
     * The expired on date.
     */
    private Date expiredOn;
}

