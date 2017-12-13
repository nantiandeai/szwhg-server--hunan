package com.creatoo.szwhg.base.model;

import com.creatoo.szwhg.core.model.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 系统参数
 * Created by yunyan on 2017/8/8.
 */
@Data
@Document(collection = "BS_PARAM")
@ApiModel(value = "系统参数")
public class SysParam  extends IdEntity {
    public static final String AUTH_ADMIN_NAME="auth.admin.username";   //超级管理员对应账号
    public static final String AUTH_PASSWORD="auth.defaultPwd";   //创建管理员时的默认密码

    @ApiModelProperty(value = "类型")
    private String type;
    @ApiModelProperty(value = "参数名")
    private String name;
    @ApiModelProperty(value = "描述")
    private String remark;
    @ApiModelProperty(value = "参数值")
    private String value;
}
