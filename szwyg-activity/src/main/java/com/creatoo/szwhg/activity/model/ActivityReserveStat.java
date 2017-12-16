package com.creatoo.szwhg.activity.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by yunyan on 2017/9/29.
 */
@Data
@ApiModel(value = "活动预定统计")
public class ActivityReserveStat {
    @ApiModelProperty(value = "活动总票数")
    private Integer total;
    @ApiModelProperty(value = "已预定票数")
    private Integer reserveSum;
    @ApiModelProperty(value = "已出票数")
    private Integer checkSum;
}
