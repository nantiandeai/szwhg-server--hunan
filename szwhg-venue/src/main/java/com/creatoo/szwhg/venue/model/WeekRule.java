package com.creatoo.szwhg.venue.model;

import com.creatoo.szwhg.core.model.MyDateDeserializer;
import com.creatoo.szwhg.core.model.MytDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by yunyan on 2017/9/11.
 */
@Data
@ApiModel(value = "场次周定义规则")
public class WeekRule {
    @ApiModelProperty(value = "生效日期")
    @JsonDeserialize(using =MyDateDeserializer.class)
    @JsonSerialize(using = MytDateSerializer.class)
    private LocalDate effectiveDate;
    @ApiModelProperty(value = "场次列表")
    private List<WeekDayDef> dayItms;
}
