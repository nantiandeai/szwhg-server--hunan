package com.creatoo.szwhg.train.model;

import com.creatoo.szwhg.core.model.MyDateDeserializer;
import com.creatoo.szwhg.core.model.MyTimeDeserializer;
import com.creatoo.szwhg.core.model.MytDateSerializer;
import com.creatoo.szwhg.core.model.MytTimeSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/**
 * 培训场次
 * Created by yunyan on 2017/9/3.
 */
@Data
@ApiModel(value = "培训课时")
public class TrainItm {
    public TrainItm(){
        id= UUID.randomUUID().toString();
    }
    @ApiModelProperty(value = "课时id")
    private String id;

    @ApiModelProperty(value = "课时日期(yyyy-MM-dd)")
    @JsonDeserialize(using =MyDateDeserializer.class)
    @JsonSerialize(using = MytDateSerializer.class)
    private LocalDate itmDate;

    @ApiModelProperty(value = "课时开始时间(HH:mm)")
    @JsonDeserialize(using =MyTimeDeserializer.class)
    @JsonSerialize(using = MytTimeSerializer.class)
    private LocalTime startTime;

    @ApiModelProperty(value = "课时结束时间(HH:mm)")
    @JsonDeserialize(using =MyTimeDeserializer.class)
    @JsonSerialize(using = MytTimeSerializer.class)
    private LocalTime endTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(value = "是否已签到")
    private Boolean isSign = false;
}
