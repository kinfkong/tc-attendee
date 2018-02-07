package com.wiproevents.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The auditable user entity.
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AuditableUserEntity extends AuditableEntity {
    /**
     * The created by.
     */
    private long createdBy;

    /**
     * The last modified by.
     */
    private long updatedBy;
}

