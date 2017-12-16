package com.creatoo.szwhg.user.model;

import com.creatoo.szwhg.core.model.IdEntity;
import com.creatoo.szwhg.core.model.IdentifyStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Created by yunyan on 2017/9/28.
 */
@Data
@Document(collection = "HY_USER_IDENTIFYAPPLY")
@ApiModel(value = "认证申请")
public class IdentifyApply extends IdEntity{
    @NotNull  @ApiModelProperty(value = "会员ID")
    private String userId;
    @NotNull  @ApiModelProperty(value = "会员昵称")
    private String nickname;

    @NotNull @ApiModelProperty(value = "是否会员自己")
    private Boolean isSelf;

    @NotNull  @ApiModelProperty(value = "真实姓名", required = true)
    private String realname;

    @NotNull @ApiModelProperty(value = "身份证号码", required = true)
    private String idnumber;

    @ApiModelProperty(value = "会员手机号码", required = true)
    private String mobile;

    @NotNull @ApiModelProperty(value = "身份证手持照片")
    private String handpic;

    @ApiModelProperty(value = "认证状态")
    private IdentifyStatus identifyStatus;

    @ApiModelProperty(value = "审核信息")
    private String auditComment;

    @ApiModelProperty(value = "审核管理员ID")
    private String auditUser;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime auditTime;

    @ApiModelProperty(value = "认证时间")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "与会员关系")
    private RelationType relation;
}
