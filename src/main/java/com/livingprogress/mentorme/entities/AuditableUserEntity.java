package com.livingprogress.mentorme.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

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
    @Column(name = "created_by", insertable = true, updatable = false)
    private String createdBy;

    /**
     * The last modified by.
     */
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
}

