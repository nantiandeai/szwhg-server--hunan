package com.creatoo.szwhg.user.model;

import com.creatoo.szwhg.core.model.ResourceType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by yunyan on 2017/9/7.
 */
@ApiModel(value = "用户收藏")
@Data
public class UserFavorite {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    @ApiModelProperty(value = "收藏对象类型")
    private ResourceType type;
    @ApiModelProperty(value = "收藏对象Id")
    private String objectId;
    @ApiModelProperty(value = "收藏对象标题")
    private String title;
    @ApiModelProperty(value = "收藏对象图片地址")
    private String picAddress;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) @ApiModelProperty(value = "收藏时间")
    private String time;
    @ApiModelProperty(value = "顺序号")
    private int seqno;
    @ApiModelProperty(value = "收藏对象时间")
    private String objectTime;
    @ApiModelProperty(value = "收藏对象地址")
    private String objectAddress;
}
