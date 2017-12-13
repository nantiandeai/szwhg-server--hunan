package com.creatoo.szwhg.core.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by wuxiangliang on 2017/10/22.
 */
@Data
@ApiModel(value = "文件信息")
public class FileInformation {
    @ApiModelProperty(value = "文件类型(pic,video,audio,attch)")
    private FileType fileType;
    @ApiModelProperty(value = "文件路径")
    private String filePath;
}
