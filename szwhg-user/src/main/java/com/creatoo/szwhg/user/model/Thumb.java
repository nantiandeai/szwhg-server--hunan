package com.creatoo.szwhg.user.model;

import com.creatoo.szwhg.core.model.IdEntity;
import com.creatoo.szwhg.core.model.MyDateTimeDeserializer;
import com.creatoo.szwhg.core.model.MytDateTimeSerializer;
import com.creatoo.szwhg.core.model.ResourceType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Created by yunyan on 2017/9/7.
 */
@ApiModel(value = "点赞记录")
@Data
@Document(collection = "HY_USER_THUMB")
public class Thumb extends IdEntity{
    @ApiModelProperty(value = "会员id")
    private String userId;
    @ApiModelProperty(value = "点赞对象类型")
    private ResourceType type;
    @ApiModelProperty(value = "点赞对象Id")
    private String objectId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) @ApiModelProperty(value = "点赞时间")
    @JsonDeserialize(using = MyDateTimeDeserializer.class)
    @JsonSerialize(using = MytDateTimeSerializer.class)
    private LocalDateTime time;
}
