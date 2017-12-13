package com.creatoo.szwhg.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 订单基础类
 * Created by yunyan on 2017/9/3.
 */
@Data
public abstract class UserOrder extends IdEntity{
    public static final String Order_Drown="drown";  //已出票状态
    public static final String Order_Cancel="cancel"; //已取消状态
    public static final String Cancel_User="user";  //用户取消
    public static final String Cancel_Manager="manager";  //管理员取消

    @Indexed(unique = true) @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(value = "订单号")
    private String orderCode;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) @ApiModelProperty(value = "业务类型")
    private String bsnType;
    @NotNull  @ApiModelProperty(value = "用户id",required = true)
    private String userId;
    @NotNull @ApiModelProperty(value = "用户昵称",required = true)
    private String nickname;
    @ApiModelProperty(value = "用户姓名")
    private String cname;
    @ApiModelProperty(value = "用户身份证号")
    private String idNumber;
    @NotNull @ApiModelProperty(value = "用户手机号",required = true)
    private String mobile;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) @ApiModelProperty(value = "下单时间")
    @JsonDeserialize(using =MyDateTimeDeserializer.class)
    @JsonSerialize(using = MytDateTimeSerializer.class)
    private LocalDateTime createTime;
    @ApiModelProperty(value = "订单取消信息")
    private CancelLog cancelLog;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) @ApiModelProperty(value = "所属分馆id")
    private String unitId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) @ApiModelProperty(value = "所属分馆名称")
    private String unitName;
}
