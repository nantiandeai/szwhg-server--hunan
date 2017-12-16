package com.creatoo.szwhg.venue.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by yunyan on 2017/9/10.
 */
@Data
@ApiModel(value = "活动室场次定义")
public class RoomItmDef {
    @ApiModelProperty(value = "不可预定")
    private Boolean disEnable;
    @ApiModelProperty(value = "周场次定义")
    private List<WeekRule> rules;
    @ApiModelProperty(value = "例外场次定义")
    private List<ExceptDayDef> exceptItms;
}
