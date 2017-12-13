package com.creatoo.szwhg.core.model;

import io.swagger.annotations.ApiModel;

/**
 * 评论状态
 * Created by wangxl on 2017/9/7.
 */
@ApiModel(value = "各种评论的状态")
public enum CommentStatus {
    Wait("待审核"),Pass("通过"),Refuse("拒绝");

    private String desc;

    private CommentStatus(String _desc){
        this.desc=_desc;
    }

    public String desc(){
        return this.desc;
    }
}
