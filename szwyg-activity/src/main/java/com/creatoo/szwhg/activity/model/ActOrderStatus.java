package com.creatoo.szwhg.activity.model;

import io.swagger.annotations.ApiModel;

/**
 * Created by yunyan on 2017/9/12.
 */
@ApiModel(value = "活动订单状态")
public enum ActOrderStatus {
    reserved,drawn,canceled
}
