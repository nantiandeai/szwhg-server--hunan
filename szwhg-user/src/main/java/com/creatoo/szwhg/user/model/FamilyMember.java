package com.creatoo.szwhg.user.model;

import com.creatoo.szwhg.core.model.IdentifyStatus;
import com.creatoo.szwhg.core.model.SexType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Created by yunyan on 2017/9/28.
 */
@Data @ApiModel(value = "常用联系人")
public class FamilyMember {
    @NotNull @ApiModelProperty(value = "姓名")
    private String name;
    @NotNull @ApiModelProperty(value = "身份证号码")
    private String idNumber;
    @NotNull @ApiModelProperty
    private SexType sex;
    @NotNull @ApiModelProperty(value = "出生年月")
    private LocalDate birthday;
    @ApiModelProperty(value = "认证状态")
    private IdentifyStatus identifyStatus;
    @ApiModelProperty(value = "手机号码")
    private String mobile;
    @ApiModelProperty(value = "与会员关系")
    private RelationType relation;
    @JsonIgnore
    public int getAge(){
        return LocalDate.now().getYear()-birthday.getYear();
    }
}
