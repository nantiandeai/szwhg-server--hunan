package com.creatoo.szwhg.venue.model;

import com.creatoo.szwhg.core.model.MyDateDeserializer;
import com.creatoo.szwhg.core.model.MyTimeDeserializer;
import com.creatoo.szwhg.core.model.MytDateSerializer;
import com.creatoo.szwhg.core.model.MytTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by yunyan on 2017/11/28.
 */
@Data
@ApiModel(value = "订单场次信息")
public class OrderItm {
    @ApiModelProperty(value = "场次id",required = true)
    private String id;
    @NotNull @ApiModelProperty(value = "场次日期",required = true)
    @JsonDeserialize(using =MyDateDeserializer.class)
    @JsonSerialize(using = MytDateSerializer.class)
    private LocalDate itmDate;
    @NotNull  @ApiModelProperty(value = "场次开始时间",required = true)
    @JsonDeserialize(using =MyTimeDeserializer.class)
    @JsonSerialize(using = MytTimeSerializer.class)
    private LocalTime itmStarttime;
    @NotNull  @ApiModelProperty(value = "场次结束时间",required = true)
    @JsonDeserialize(using =MyTimeDeserializer.class)
    @JsonSerialize(using = MytTimeSerializer.class)
    private LocalTime itmEndtime;
}

