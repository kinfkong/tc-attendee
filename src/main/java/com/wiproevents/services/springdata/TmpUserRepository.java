/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */

package com.wiproevents.services.springdata;

import com.wiproevents.entities.TmpUser;
import com.microsoft.azure.spring.data.documentdb.repository.DocumentDbRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TmpUserRepository extends DocumentDbRepository<TmpUser, String> {
    List<TmpUser> findByFirstName(String firstName);

    List<TmpUser> findByLastName(String lastName);
}

