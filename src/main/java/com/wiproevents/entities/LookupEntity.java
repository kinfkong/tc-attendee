package com.wiproevents.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Base lookup entity.
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class LookupEntity extends IdentifiableEntity {
    /**
     * The lookup value.
     */
    private String name;
}

