package com.creatoo.szwhg.train.model;

import com.creatoo.szwhg.core.model.MyTimeDeserializer;
import com.creatoo.szwhg.core.model.MytTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

/**
 * 培训课时
 * Created by wangxl on 2017/9/28.
 */
@Data
@ApiModel(value = "培训课时")
public class TrainTimes {
    @NotNull @ApiModelProperty(value = "开始时间(HH:mm)")
    @JsonDeserialize(using =MyTimeDeserializer.class)
    @JsonSerialize(using = MytTimeSerializer.class)
    private LocalTime startTime;

    @NotNull @ApiModelProperty(value = "结束时间(HH:mm)")
    @JsonDeserialize(using =MyTimeDeserializer.class)
    @JsonSerialize(using = MytTimeSerializer.class)
    private LocalTime endTime;
}
