package com.creatoo.szwhg.venue.model;

import com.creatoo.szwhg.core.model.MyTimeDeserializer;
import com.creatoo.szwhg.core.model.MytTimeSerializer;
import com.creatoo.szwhg.core.model.WeekDay;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalTime;

/**
 * Created by yunyan on 2017/9/10.
 */
@Data
@ApiModel(value = "周次场次定义")
public class WeekDayDef {
    @ApiModelProperty(value = "星期")
    private WeekDay weekDay;
    @ApiModelProperty(value = "开始时间")
    @JsonDeserialize(using =MyTimeDeserializer.class)
    @JsonSerialize(using = MytTimeSerializer.class)
    private LocalTime startTime;
    @ApiModelProperty(value = "结束时间")
    @JsonDeserialize(using =MyTimeDeserializer.class)
    @JsonSerialize(using = MytTimeSerializer.class)
    private LocalTime endTime;
}
