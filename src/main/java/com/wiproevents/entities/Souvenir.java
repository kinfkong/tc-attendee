package com.wiproevents.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjinggang on 2018/2/7.
 */
@Getter
@Setter
public class Souvenir extends IdentifiableEntity {
    private String name;
    private String description;
    private List<String> options = new ArrayList<>();

}
