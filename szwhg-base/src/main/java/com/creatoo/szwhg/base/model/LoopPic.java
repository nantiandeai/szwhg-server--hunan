package com.creatoo.szwhg.base.model;

import com.creatoo.szwhg.core.model.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by wuxiangliang on 2017/10/31.
 */
@Data
@Document(collection = "LP_PIC")
@ApiModel(value = "轮播图")
public class LoopPic extends IdEntity {
    @DBRef
    @ApiModelProperty(value = "所属文化馆",required = true)
    private Unit unit;
    @ApiModelProperty(value = "轮播图类型",required = true)
    private LoopType type;
    @ApiModelProperty(value = "轮播图名称",required = true)
    private String name;
    @ApiModelProperty(value = "轮播图内容")
    private List<LoopContent> contents;
}
