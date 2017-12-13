package com.creatoo.szwhg.base.model;

import com.creatoo.szwhg.core.model.IdEntity;
import com.creatoo.szwhg.core.model.MyDateTimeDeserializer;
import com.creatoo.szwhg.core.model.MytDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Created by yunyan on 2017/11/13.
 */
@Data
@Document(collection = "BS_MESSAGE")
@ApiModel(value = "消息")
public class Message extends IdEntity{
    @ApiModelProperty(value = "发送方式")
    private MsgChannel channel=MsgChannel.sms;
    @ApiModelProperty(value = "接收人")
    private String receiver;
    @ApiModelProperty(value = "消息主体内容")
    private String content;
    @ApiModelProperty(value = "是否验证码")
    private Boolean isVerifyCode=false;
    @ApiModelProperty(value = "验证码")
    private String verifyCode;
    @ApiModelProperty(value = "消息发送状态")
    private MsgStatus status=MsgStatus.wait;
    @ApiModelProperty(value = "重试次数")
    private Integer retryCount=0;
    @ApiModelProperty(value = "最大重试次数")
    private Integer retryMax=3;
    @ApiModelProperty(value = "创建时间")
    @JsonDeserialize(using = MyDateTimeDeserializer.class)
    @JsonSerialize(using = MytDateTimeSerializer.class)
    private LocalDateTime createTime=LocalDateTime.now();
    @ApiModelProperty(value = "成功发送时间")
    @JsonDeserialize(using = MyDateTimeDeserializer.class)
    @JsonSerialize(using = MytDateTimeSerializer.class)
    private LocalDateTime sendTime;
    @ApiModelProperty(value = "短信平台id")
    private String smsId;
}
