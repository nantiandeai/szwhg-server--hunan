package com.creatoo.szwhg.train.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by yunyan on 2017/11/28.
 */
@Data
@ApiModel(value = "面试信息")
public class Interview {
    @ApiModelProperty(value = "面试时间")
    private LocalDateTime time;
    @ApiModelProperty(value = "面试地址")
    private String address;
}
