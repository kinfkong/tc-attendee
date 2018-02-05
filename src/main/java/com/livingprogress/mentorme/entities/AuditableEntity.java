package com.livingprogress.mentorme.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Temporal;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * The auditable entity.
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AuditableEntity extends IdentifiableEntity {
    /**
     * The created on date.
     */
    @Temporal(TIMESTAMP)
    private Date createdOn;

    /**
     * The last update on date.
     */
    @Temporal(TIMESTAMP)
    private Date updatedOn;
}

