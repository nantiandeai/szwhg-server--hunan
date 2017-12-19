package com.creatoo.szwhg.base.model;

import com.creatoo.szwhg.core.model.IdEntity;
import com.creatoo.szwhg.core.model.MyDateTimeDeserializer;
import com.creatoo.szwhg.core.model.MytDateTimeSerializer;
import com.creatoo.szwhg.core.model.ResourceType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * 评论
 * Created by wangxl on 2017/9/7.
 */
@Data
@Document(collection = "BS_COMMENT")
@ApiModel("评论")
public class Comment  extends IdEntity{
    @ApiModelProperty(value = "资源类型", required = true)
    private ResourceType type;
    @ApiModelProperty(value = "评论对象Id")
    private String objId;
    @ApiModelProperty(value = "用户id", required = true)
    private String userId;

    @ApiModelProperty(value = "用户昵称", required = true)
    private String nickname;

    @ApiModelProperty(value = "评论时间")
    @JsonDeserialize(using =MyDateTimeDeserializer.class)
    @JsonSerialize(using = MytDateTimeSerializer.class)
    private LocalDateTime time;

    @ApiModelProperty(value = "评论内容", required = true)
    private String content;

    @ApiModelProperty(value = "评论状态(Wait-待审核; Pass-通过; Refuse-拒绝)", required = true)
    private CommentStatus status;

    @ApiModelProperty(value = "回复ID,字段为空时表示是评论")
    private String refid;
}
