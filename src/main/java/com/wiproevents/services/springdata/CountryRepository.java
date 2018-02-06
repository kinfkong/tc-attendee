package com.wiproevents.services.springdata;

import com.wiproevents.entities.Country;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationExecutor;

/**
 * Created by wangjinggang on 2018/2/6.
 */
public interface CountryRepository extends DocumentDbSpecificationExecutor<Country, String> {
}
