package com.creatoo.szwhg.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by yunyan on 2017/12/3.
 */
@Data
@ApiModel(value = "会员基本统计类")
public class UserGeneralStat {
    @ApiModelProperty(value = "会员总数")
    private Long total;
    @ApiModelProperty(value = "认证会员总数")
    private Long identifyCount;
    @ApiModelProperty(value = "PC端注册会员总数")
    private Long pcRegistSum;
    @ApiModelProperty(value = "微信端注册会员总数")
    private Long mobileRegistSum;
}
