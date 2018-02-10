package com.wiproevents.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

/**
 * The user.
 */
@Getter
@Setter
@Document(collection = "user")
public class User extends AuditableEntity {
    /**
     * The email.
     */
    private String email;
    /**
     * The password (hashed).
     */
    @JsonProperty(access = WRITE_ONLY)
    private String password;


    private String fullName;

    private String personalPhone;
    private String businessPhone;
    private String businessUnit;
    private Designation designation;
    private String aboutMe;
    private String profilePictureURL;
    private boolean emailVerified = false;
    /**
     * The user status.
     */
    private UserStatus status;


    private List<UserRole> roles;
}

