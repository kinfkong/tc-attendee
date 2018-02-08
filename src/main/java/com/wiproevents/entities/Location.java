package com.wiproevents.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by wangjinggang on 2018/2/7.
 */
@Getter
@Setter
public class Location {
    private String name;
    private Country country;
    private String state;
    private String locality;
    private String streetAndBuilding;
    private String zip;
    private Double latitude;
    private Double longitude;

    private String createdBy;
    private String updatedBy;
    private Date createdOn;
    private Date updatedOn;
}
