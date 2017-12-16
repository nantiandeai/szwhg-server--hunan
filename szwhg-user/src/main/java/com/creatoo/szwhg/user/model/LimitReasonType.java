package com.creatoo.szwhg.user.model;

import io.swagger.annotations.ApiModel;

/**
 * Created by yunyan on 2017/11/26.
 */
@ApiModel(value = "会员限制原因类型")
public enum LimitReasonType {
    activityNotSign,trainNotSign,venueNotSign
}
