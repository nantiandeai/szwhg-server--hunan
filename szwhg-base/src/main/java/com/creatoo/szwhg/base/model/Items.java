package com.creatoo.szwhg.base.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by wuxiangliang on 2017/10/14.
 */
@Data
public class Items {
    @ApiModelProperty(value = "题目")
    private String title;
    @ApiModelProperty(value = "类型")
    private String itemType;
    private String itemClass;
    private String answer;
    @ApiModelProperty(value = "内容")
    private List<Map<String,String>> content=new ArrayList<>();
}
