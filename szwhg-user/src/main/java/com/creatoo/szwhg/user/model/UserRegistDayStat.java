package com.creatoo.szwhg.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by yunyan on 2017/12/1.
 */
@Data
@ApiModel(value = "统计日注册数据类")
public class UserRegistDayStat {
    @ApiModelProperty(value = "年份")
    private String year;
    @ApiModelProperty(value = "月份")
    private String month;
    @ApiModelProperty(value = "天")
    private String day;
    @ApiModelProperty(value = "总数")
    private long count;
}
