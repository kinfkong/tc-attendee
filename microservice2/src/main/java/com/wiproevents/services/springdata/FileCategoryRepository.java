package com.wiproevents.services.springdata;

import com.wiproevents.entities.FileCategory;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;
import org.springframework.stereotype.Repository;

/**
 * The User repository.
 */
@Repository
public interface FileCategoryRepository extends DocumentDbSpecificationRepository<FileCategory, String> {

}

