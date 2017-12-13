package com.creatoo.szwhg.core.rest;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Created by yunyan on 2017/7/23.
 */
@Value
@AllArgsConstructor
@ApiModel(value="出错信息")
public class ClientErrorMsg {
    private int status;
    private String message;

}
