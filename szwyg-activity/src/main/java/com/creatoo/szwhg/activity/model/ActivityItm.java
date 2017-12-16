package com.creatoo.szwhg.activity.model;

import com.creatoo.szwhg.core.model.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Created by yunyan on 2017/8/17.
 */
@Data
@ApiModel(value = "活动场次")
public class ActivityItm {
    @ApiModelProperty(value = "场次id")
    private String id;
    @ApiModelProperty(value = "场次日期")
    @JsonDeserialize(using =MyDateDeserializer.class)
    @JsonSerialize(using = MytDateSerializer.class)
    private LocalDate itmDate;
    @ApiModelProperty(value = "场次开始时间")
    @JsonDeserialize(using =MyTimeDeserializer.class)
    @JsonSerialize(using = MytTimeSerializer.class)
    private LocalTime startTime;
    @ApiModelProperty(value = "场次结束时间")
    @JsonDeserialize(using =MyTimeDeserializer.class)
    @JsonSerialize(using = MytTimeSerializer.class)
    private LocalTime endTime;
    @ApiModelProperty(value = "剩余票数")
    private Integer leftTicketSum;
    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) @ApiModelProperty(value = "已預定座位")
    private List<Grid> reserveSeats;
}
