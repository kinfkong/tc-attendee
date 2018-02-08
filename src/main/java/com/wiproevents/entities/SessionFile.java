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
public class SessionFile implements Model {
    private List<FileEntity> files = new ArrayList<>();
    private FileCategory category;
}
