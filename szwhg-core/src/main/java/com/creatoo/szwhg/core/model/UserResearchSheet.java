package com.creatoo.szwhg.core.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by yunyan on 2017/12/6.
 */
@Data
@ApiModel(value = "会员调查表")
public class UserResearchSheet {
    @ApiModelProperty(value = "提交时间")
    @JsonDeserialize(using = MyDateTimeDeserializer.class)
    @JsonSerialize(using = MytDateTimeSerializer.class)
    private LocalDateTime createTime;
    @ApiModelProperty(value = "会员id")
    private String userId;
    @ApiModelProperty(value = "标题列表")
    private List<String> titles;
    @ApiModelProperty(value = "结果列表，选择题的结果存放序号，如多选：‘1,3,4’，问答题直接存储答案")
    private List<String> results;
}
