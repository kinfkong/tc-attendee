/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.services.springdata;

import com.wiproevents.entities.answers.UserPollAnswer;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;
import org.springframework.stereotype.Repository;

/**
 * The user poll answer repository.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Repository
public interface UserPollAnswerRepository extends DocumentDbSpecificationRepository<UserPollAnswer, String> {
}

