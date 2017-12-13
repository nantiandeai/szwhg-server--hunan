package com.creatoo.szwhg.core.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by yunyan on 2017/10/15.
 */
@ApiModel(value = "数字资源")
@Data
public class DigitInfo extends AuditEntity{
    @ApiModelProperty(value = "资源id")
    private String id;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "类型")
    private DigitType type;
    @ApiModelProperty(value = "封面图片")
    private String pic;
    @ApiModelProperty(value = "资源大小")
    private String fileSize;
    @ApiModelProperty(value = "资源文件")
    private String file;
    @ApiModelProperty(value = "资源文件名称")
    private String fileName;
    @ApiModelProperty(value = "排序顺序")
    private Integer seqno=1;
}
