package com.creatoo.szwhg.core.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by yunyan on 2017/9/2.
 */
@Data
@ApiModel(value = "流程处理日志")
public class FlowLog {
    @ApiModelProperty(value = "来源状态")
    private String fromStatus;
    @ApiModelProperty(value = "目的状态")
    private String toStatus;
    @ApiModelProperty(value = "操作描述")
    private String operDesc;
    @ApiModelProperty(value = "操作时间")
    @JsonDeserialize(using = MyDateTimeDeserializer.class)
    @JsonSerialize(using = MytDateTimeSerializer.class)
    private LocalDateTime operateTime;
    @ApiModelProperty(value = "操作人部门")
    private String operatorDept;
    @ApiModelProperty(value = "操作人姓名")
    private String operatorName;
    @ApiModelProperty(value = "操作人备注")
    private String operatorDesc;
}
