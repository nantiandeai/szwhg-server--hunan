package com.creatoo.szwhg.venue.model;

import com.creatoo.szwhg.core.model.MyDateDeserializer;
import com.creatoo.szwhg.core.model.MytDateSerializer;
import com.creatoo.szwhg.core.model.TimeSection;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by yunyan on 2017/9/9.
 */
@Data
@ApiModel(value = "例外场次定义")
public  class ExceptDayDef {
    @ApiModelProperty(value = "例外日期")
    @JsonDeserialize(using =MyDateDeserializer.class)
    @JsonSerialize(using = MytDateSerializer.class)
    private LocalDate date;
    @ApiModelProperty(value = "时间段列表")
    private List<TimeSection> times;

}
