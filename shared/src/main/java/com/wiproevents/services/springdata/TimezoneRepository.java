package com.wiproevents.services.springdata;

import com.wiproevents.entities.Timezone;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;
import org.springframework.stereotype.Repository;

/**
 * The User repository.
 */
@Repository
public interface TimezoneRepository extends DocumentDbSpecificationRepository<Timezone, String> {

}

