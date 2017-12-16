package com.creatoo.szwhg.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by yunyan on 2017/8/15.
 */
@Data
@ApiModel(value = "绑定账号")
public class AccountBind {
    @ApiModelProperty(value = "绑定类型")
    private BindType type;
    @ApiModelProperty(value = "绑定值")
    private String account;
}
