package com.creatoo.szwhg.core.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by yunyan on 2017/9/7.
 */
@ApiModel(value = "坐标")
@Data
public class Coordinate {
    @ApiModelProperty(value = "经度")
    private String longitude;
    @ApiModelProperty(value = "纬度")
    private String latitude;
}
