package com.livingprogress.mentorme.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

/**
 * Base identifiable entity.
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class IdentifiableEntity {
    /**
     * The id.
     */
    @Id
    private String id;

    /**
     * Override the equals method.
     *
     * @param target the target
     * @return true if two entities have the same ID
     */
    @Override
    public boolean equals(Object target) {
        if (target instanceof IdentifiableEntity) {
            IdentifiableEntity entity = (IdentifiableEntity) target;
            return entity.getId().equals(this.id);
        }
        return false;
    }

    /**
     * Override the hashCode method.
     *
     * @return the hash code of the entity
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

