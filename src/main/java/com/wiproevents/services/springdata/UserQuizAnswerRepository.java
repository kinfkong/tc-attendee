/*
 * Copyright (c) 2018 TopCoder, Inc. All rights reserved.
 */
package com.wiproevents.services.springdata;

import com.wiproevents.entities.answers.UserQuizAnswer;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationRepository;
import org.springframework.stereotype.Repository;

/**
 * The user quiz answer repository.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
@Repository
public interface UserQuizAnswerRepository extends DocumentDbSpecificationRepository<UserQuizAnswer, String> {
}

