package com.creatoo.szwhg.core.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by yunyan on 2017/11/23.
 */
@ApiModel(value = "风采")
@Data
public class Spirit {
    @ApiModelProperty(value = "资源id")
    private String id;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "图片列表")
    private List<String> picList;
}
