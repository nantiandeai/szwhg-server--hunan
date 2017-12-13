package com.creatoo.szwhg.base.model;

import io.swagger.annotations.ApiModel;

/**
 * Created by yunyan on 2017/11/13.
 */
@ApiModel(value = "消息状态")
public enum MsgStatus {
    wait,starting,sent,error
}
