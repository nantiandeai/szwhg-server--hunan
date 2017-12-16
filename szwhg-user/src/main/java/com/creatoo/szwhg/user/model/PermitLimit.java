package com.creatoo.szwhg.user.model;

import com.creatoo.szwhg.core.model.MyDateTimeDeserializer;
import com.creatoo.szwhg.core.model.MytDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by yunyan on 2017/11/26.
 */
@Data
@ApiModel(value = "权限限制")
public class PermitLimit {
    @ApiModelProperty(value = "限制类型")
    private PermitLimitType type;
    @ApiModelProperty(value = "限制时间点，该时间点之后解除限制")
    @JsonDeserialize(using =MyDateTimeDeserializer.class)
    @JsonSerialize(using = MytDateTimeSerializer.class)
    private LocalDateTime limitTime;
}
