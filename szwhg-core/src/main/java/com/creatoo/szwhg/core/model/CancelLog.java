package com.creatoo.szwhg.core.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by yunyan on 2017/9/28.
 */
@Data
@ApiModel(value = "取消日志")
public class CancelLog {
    @ApiModelProperty(value = "取消类型")
    private CancelType type;
    @ApiModelProperty(value = "取消原因")
    private String reason;
    @ApiModelProperty(value = "取消时间")
    @JsonDeserialize(using = MyDateTimeDeserializer.class)
    @JsonSerialize(using = MytDateTimeSerializer.class)
    private LocalDateTime time;
}
