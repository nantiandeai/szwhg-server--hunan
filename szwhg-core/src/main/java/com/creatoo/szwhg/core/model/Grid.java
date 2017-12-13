package com.creatoo.szwhg.core.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by yunyan on 2017/9/13.
 */
@ApiModel(value = "网格")
@Data
public class Grid {
    @ApiModelProperty(value = "行")
    private int row;
    @ApiModelProperty(value = "列")
    private int column;
    @ApiModelProperty(value = "类型")
    private GridType type;
    @ApiModelProperty(value = "座位号")
    private String seatNo;
}
