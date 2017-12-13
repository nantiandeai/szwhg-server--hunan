package com.creatoo.szwhg.core.model;

import io.swagger.annotations.ApiModel;

/**
 * Created by wangxl on 2017/9/8.
 */
@ApiModel("身份认证状态")
public enum IdentifyStatus {
    Not("未认证"),Wait("待认证"),Yes("已认证"),Fail("认证失败");

    private String desc;

    private IdentifyStatus(String _desc){
        this.desc=_desc;
    }

    public String desc(){
        return this.desc;
    }
}
