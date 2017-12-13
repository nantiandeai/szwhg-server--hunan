package com.creatoo.szwhg.base.model;

import com.creatoo.szwhg.core.model.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色
 * Created by yunyan on 2017/8/3.
 */
@Data
@Document(collection = "BS_ROLE")
@ApiModel(value = "角色")
public class Role extends IdEntity{
    @NotNull
    @ApiModelProperty(value = "编码")
    private String code;
    @NotNull @ApiModelProperty(value = "名称")
    private String name;
    @DBRef  @NotNull @ApiModelProperty(value = "文化馆")
    private Unit unit;
    @NotNull @ApiModelProperty(value = "角色类型")
    private RoleType type;
    @ApiModelProperty(value = "权限")
    private List<Grant> grants;

}
