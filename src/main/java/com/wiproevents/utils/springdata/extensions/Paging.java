package com.wiproevents.utils.springdata.extensions;


import lombok.Getter;
import lombok.Setter;


/**
 * The paging parameters.
 */
@Getter
@Setter
public class Paging {

    private String requestContinuation;

    /**
     * The page size.
     */
    private int pageSize;

    /**
     * The sort column.
     */
    private String sortColumn;

    /**
     * The sort order.
     */
    private SortOrder sortOrder;
}

