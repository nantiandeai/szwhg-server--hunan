package com.creatoo.szwhg.core.model;

import io.swagger.annotations.ApiModel;

/**
 * 在线状态，物品从编辑到发布的状态常量
 * Created by wuxiangliang on 2017/10/14.
 */
@ApiModel(value = "非遗类型")
public enum HeritageType {
    Directory("名录"),Successor("传承人");

    private String desc;

    private HeritageType(String _desc){
        this.desc=_desc;
    }
}
