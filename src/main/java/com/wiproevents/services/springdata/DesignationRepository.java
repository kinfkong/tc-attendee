package com.wiproevents.services.springdata;

import com.wiproevents.entities.Designation;
import com.wiproevents.utils.springdata.extensions.DocumentDbSpecificationExecutor;

/**
 * Created by wangjinggang on 2018/2/6.
 */
public interface DesignationRepository extends DocumentDbSpecificationExecutor<Designation, String> {
}
