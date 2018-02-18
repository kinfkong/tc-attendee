package com.wiproevents.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Token extends AuditableEntity {
    // token stored should be hashed
    private String token;
    private Date expires;
}
