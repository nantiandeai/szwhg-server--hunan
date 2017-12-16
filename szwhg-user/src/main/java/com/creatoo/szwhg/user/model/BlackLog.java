package com.creatoo.szwhg.user.model;

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
 * Created by yunyan on 2017/11/26.
 */
@Data
@Document(collection = "HY_BLACK_LOG")
@ApiModel(value = "黑名单日志")
public class BlackLog extends IdEntity {
    @ApiModelProperty(value = "会员id")
    private String userId;
    @ApiModelProperty(value = "昵称")
    private String nickname;
    @ApiModelProperty(value = "手机号码")
    private String mobile;
    @ApiModelProperty(value = "限制类型")
    private PermitLimitType limitType;
    @ApiModelProperty(value = "发生时间")
    @JsonDeserialize(using =MyDateTimeDeserializer.class)
    @JsonSerialize(using = MytDateTimeSerializer.class)
    private LocalDateTime happenTime;
    @ApiModelProperty(value = "加入黑名单原因")
    private LimitReasonType reasonType;
    @ApiModelProperty(value = "限制天数")
    private Integer limitDays;

}
