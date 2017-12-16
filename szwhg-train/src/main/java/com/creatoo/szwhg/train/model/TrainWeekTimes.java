package com.creatoo.szwhg.train.model;

import com.creatoo.szwhg.core.model.WeekDay;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 培训周期
 * Created by wangxl on 2017/9/28.
 */
@Data
@ApiModel(value = "培训周期")
public class TrainWeekTimes {
    @ApiModelProperty(value = "星期")
    private WeekDay weekDay;

    @ApiModelProperty(value = "培训课时")
    private List<TrainTimes> trainTimes;
}
