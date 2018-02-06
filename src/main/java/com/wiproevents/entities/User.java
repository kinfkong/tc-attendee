package com.wiproevents.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Enumerated;
import java.util.List;

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


    private String fullName;


    private List<UserRole> roles;

    /**
     * The email.
     */
    private String email;

    /**
     * The user status.
     */
    @Enumerated(STRING)
    private UserStatus status;
}

