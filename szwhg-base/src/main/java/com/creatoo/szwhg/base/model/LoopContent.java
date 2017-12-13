package com.creatoo.szwhg.base.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by wuxiangliang on 2017/10/31.
 */
@Data
@ApiModel(value = "轮播图内容")
public class LoopContent {
    private String id;
    @ApiModelProperty(value = "标题",required = true)
    private String title;
    @ApiModelProperty(value = "图片",required = true)
    private String coverPic;
    @ApiModelProperty(value = "连接地址URl")
    private String url;
    @ApiModelProperty(value = "状态")
    private Boolean enable=true;
}
