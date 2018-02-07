package com.wiproevents.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by wangjinggang on 2018/2/7.
 */
@Getter
@Setter
public class Location extends AuditableUserEntity {
    private String name;
    private Country country;
    private String state;
    private String locality;
    private String streetAndBuilding;
    private String zip;
    private Double latitude;
    private Double longitude;

}
