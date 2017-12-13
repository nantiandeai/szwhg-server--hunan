package com.creatoo.szwhg.base.model;

import com.creatoo.szwhg.core.model.AuditEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * Created by lzh on 2017/11/15
 */
@Data
@Document(collection = "BS_CULTURE_BRAND")
@ApiModel(value = "文化品牌")
public class CultureBrand extends AuditEntity {
    @NotNull @ApiModelProperty(value = "名称")
    private String name;
    @NotNull @ApiModelProperty(value = "封面")
    private String coverPic;
    @NotNull @ApiModelProperty(value = "简介")
    private String brief;
    @NotNull @ApiModelProperty(value = "描述")
    private String describe;
    @ApiModelProperty(value = "是否上架")
    private Boolean isPublish;
}
