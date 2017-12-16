package com.creatoo.szwhg.base.model;

import com.creatoo.szwhg.core.model.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * Created by yunyan on 2017/11/29.
 */
@Data
@Document(collection = "BS_DEVICE")
@ApiModel(value = "外部设备")
public class DeviceClient extends IdEntity {
    @NotNull @ApiModelProperty(value = "设备名称")
    private String name;
    @NotNull @ApiModelProperty(value = "客户端id")
    private String clinetId;
    @NotNull @ApiModelProperty(value = "授权类型")
    private String[] grantTypes;
    @ApiModelProperty(value = "权限范围")
    private String[] scopes;
    @NotNull @ApiModelProperty(value = "密码")
    private String secret;
    @DBRef  @NotNull @ApiModelProperty(value = "所属文化馆")
    private Unit unit;
}
