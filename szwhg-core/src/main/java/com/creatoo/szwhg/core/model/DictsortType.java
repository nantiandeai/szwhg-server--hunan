package com.creatoo.szwhg.core.model;

import io.swagger.annotations.ApiModel;

/**
 * Created by wuxiangliang on 2017/10/25.
 */
@ApiModel(value = "字典分类")
public enum DictsortType {
    label("标签"),dict("字典");
    private String desc;

    private DictsortType(String _desc){
        this.desc=_desc;
    }

    public String desc(){
        return this.desc;
    }
}
