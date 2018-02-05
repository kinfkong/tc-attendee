package com.livingprogress.mentorme.utils.springdata.extensions;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The search result.
 * @param <T> the entity type
 */
@Getter
@Setter
public class SearchResult<T> {
    /**
     * The total number.
     */
    private long total;

    private String requestContinuation;

    /**
     * The values.
     */
    private List<T> entities;
}

