package com.creatoo.szwhg.train.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 培训订单用户
 * Created by wangxl on 2017/9/7.
 */
@Data
@ApiModel(value = "培训订单下的用户")
public class TrainOrderUser {
    @ApiModelProperty(value = "用户真实姓名", required = true)
    private String userName;

    @ApiModelProperty(value = "用户手机号", required = true)
    private String phoneNo;

    @ApiModelProperty(value = "用户身份证号码", required = true)
    private String idnumber;

    @ApiModelProperty(value = "用户票务信息")
    private String ticketid;
}
