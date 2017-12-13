package com.creatoo.szwhg.core.model;

import io.swagger.annotations.ApiModel;

/**
 * Created by yunyan on 2017/9/3.
 */
@ApiModel(value = "订单业务类型")
public enum OrderType {
    activity("AC"),train("TR"),venueroom("VR");
    private String code;

    private OrderType(String _code){
        this.code=_code;
    }

    public String getCode(){
        return code;
    }
}
