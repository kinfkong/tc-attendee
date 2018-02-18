package com.wiproevents.entities;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by wangjinggang on 2018/2/10.
 */
@Document(collection = "user_permission")
@Getter
@Setter
public class UserPermission extends LookupEntity {

}
