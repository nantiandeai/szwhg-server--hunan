package com.creatoo.szwhg.base.model;

import com.creatoo.szwhg.core.model.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 管理员
 * Created by yunyan on 2017/8/8.
 */
@Data
@Document(collection = "BS_MANAGER")
@ApiModel(value = "管理员")
public class Manager  extends IdEntity {
    @Indexed(unique = true) @NotNull
    @ApiModelProperty(value = "账号",required = true)
    private String username;
    @JsonIgnore @ApiModelProperty(value = "密码")
    private String password;
    @NotNull @ApiModelProperty(value = "姓名",required = true)
    private String name;
    @ApiModelProperty(value = "电话")
    private String phone;
    @ApiModelProperty(value = "归属组织")
    @DBRef  @NotNull
    private Unit unit;
    @Transient @JsonProperty(access = JsonProperty.Access.READ_ONLY) @ApiModelProperty(value = "归属文化馆")
    private Unit orgUnit;
    @DBRef @JsonProperty(access = JsonProperty.Access.READ_ONLY) @ApiModelProperty(value = "角色")
    private List<Role> roles;
    @ApiModelProperty(value = "是否停用")
    private Boolean isLocked=false;

    public void buildOtherProperties(){
        if(unit.getType().equals(Unit.Org_Type)) orgUnit=unit;
        else {
            Unit parent=unit.getParent();
            while(!parent.getType().equals(Unit.Org_Type)){
                parent=parent.getParent();
            }
            orgUnit=parent;
        }
    }
}
