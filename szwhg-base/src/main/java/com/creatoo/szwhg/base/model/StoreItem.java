package com.creatoo.szwhg.base.model;

import com.creatoo.szwhg.core.model.IdEntity;
import com.creatoo.szwhg.core.model.ResearchItemType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 *  题库持久类
 * Created by wuxiangliang on 2017/10/14.
 */
@Data
@Document(collection = "TK_STOREITEM")
@ApiModel(value = "题库")
public class StoreItem extends IdEntity {
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "题型")
    private ResearchItemType itemType;
    @ApiModelProperty(value = "问题内容")
    private List<String> contents;
}