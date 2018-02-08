package com.wiproevents.services.springdata;

import com.wiproevents.entities.FileEntity;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;
import org.springframework.stereotype.Repository;

/**
 * The User repository.
 */
@Repository
public interface FileEntityRepository extends DocumentDbSpecificationRepository<FileEntity, String> {
}

