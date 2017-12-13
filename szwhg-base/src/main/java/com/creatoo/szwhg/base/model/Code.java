package com.creatoo.szwhg.base.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by yunyan on 2017/8/8.
 */
@Data
@ApiModel(value = "代码项")
public class Code {
    @ApiModelProperty(value = "代码",required = true)
    private String code;
    @ApiModelProperty(value = "代码值",required = true)
    private String value;
    @ApiModelProperty(value = "显示顺序")
    private Integer seqno=1;
}
