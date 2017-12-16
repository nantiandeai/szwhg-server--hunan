package com.creatoo.szwhg.train.model;

import com.creatoo.szwhg.core.model.MyDateDeserializer;
import com.creatoo.szwhg.core.model.MytDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;


/**
 * 培训例外时间
 * Created by wangxl on 2017/9/28.
 */
@Data
@ApiModel(value = "培训周期例外时间")
public class TrainExceptTimes {
    @NotNull @ApiModelProperty(value = "例外培训日期")
    @JsonDeserialize(using =MyDateDeserializer.class)
    @JsonSerialize(using = MytDateSerializer.class)
    private LocalDate trainDate;

    @ApiModelProperty(value = "例外培训日期的课时表")
    private List<TrainTimes> trainTimes;
}
