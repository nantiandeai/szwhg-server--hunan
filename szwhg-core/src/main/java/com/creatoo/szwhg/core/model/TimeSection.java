package com.creatoo.szwhg.core.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalTime;

/**
 * Created by yunyan on 2017/9/11.
 */
@Data
@ApiModel(value = "时间段")
public class TimeSection {
    @ApiModelProperty(value = "开始时间")
    @JsonDeserialize(using =MyTimeDeserializer.class)
    @JsonSerialize(using = MytTimeSerializer.class)
    private LocalTime startTime;
    @ApiModelProperty(value = "结束时间")
    @JsonDeserialize(using =MyTimeDeserializer.class)
    @JsonSerialize(using = MytTimeSerializer.class)
    private LocalTime endTime;
}
