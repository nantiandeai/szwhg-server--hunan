package com.creatoo.szwhg.user.model;

import io.swagger.annotations.ApiModel;

/**
 * Created by yunyan on 2017/11/29.
 */
@ApiModel(value = "关系类型")
public enum RelationType {
    children,parent,mate,friend
}
