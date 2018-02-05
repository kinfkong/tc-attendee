package com.wiproevents.entities;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import com.microsoft.azure.spring.data.documentdb.core.mapping.PartitionKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

/**
 * Created by wangjinggang on 2018/2/4.
 */
@Getter
@Setter
@Document(collection = "tmp_user")
@NoArgsConstructor
public class TmpUser {
    @Id
    private String emailAddress;
    private String firstName;
    @PartitionKey
    private String lastName;
}
