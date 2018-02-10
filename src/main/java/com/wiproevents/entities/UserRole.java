package com.wiproevents.entities;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The user role.
 */
@Getter
@Setter
@Document(collection = "user_role")
public class UserRole extends IdentifiableEntity {
    private String name;
    private List<UserPermission> permissions = new ArrayList<>();
}

