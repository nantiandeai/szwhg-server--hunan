package com.creatoo.szwhg.base.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by yunyan on 2017/8/21.
 */
@Data
@AllArgsConstructor
@ApiModel(value = "权限")
public class Grant {
    public static  final String Menu_Type="menu";

    @ApiModelProperty(value = "资源类型")
    private String reousrceType;
    @ApiModelProperty(value = "资源编码")
    private String resourceCode;
}
