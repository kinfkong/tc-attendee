package com.wiproevents.entities;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by wangjinggang on 2018/2/11.
 */
@Getter
@Setter
@Document(collection = "timezone")
public class Timezone extends LookupEntity {
}
