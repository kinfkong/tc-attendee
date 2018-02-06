package com.wiproevents.entities;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by wangjinggang on 2018/2/6.
 */
@Getter
@Setter
@Document(collection = "verify_email_token")
public class VerifyEmailToken extends Token {
    private String email;
}
