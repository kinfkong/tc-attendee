package com.wiproevents.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Enumerated;
import javax.persistence.Transient;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
import static javax.persistence.EnumType.STRING;

/**
 * The user.
 */
@Getter
@Setter
@Document(collection = "user")
public class User extends AuditableEntity {
    /**
     * The password (hashed).
     */
    @JsonProperty(access = WRITE_ONLY)
    private String password;

    /**
     * The first name.
     */
    private String firstName;

    /**
     * The last name.
     */
    private String lastName;

    private String fullName;

    private List<UserRole> roles;

    /**
     * The email.
     */
    private String email;


    /**
     * The access token.
     */
    @JsonIgnore
    private String accessToken;

    /**
     * Expires in millis.
     */
    @Transient
    @JsonInclude(NON_NULL)
    private Long expires;

    /**
     * The user status.
     */
    @Enumerated(STRING)
    private UserStatus status;
}

