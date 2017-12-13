package com.creatoo.szwhg.base.model;

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
 * Created by yunyan on 2017/11/30.
 */
@Data
@Document(collection = "BS_OPERLOG")
@ApiModel(value = "操作日志")
public class OperLog extends IdEntity{
    @ApiModelProperty(value = "文化馆id")
    private String unitId;
    @ApiModelProperty(value = "文化管名称")
    private String unitName;
    @ApiModelProperty(value = "管理员账号")
    private String username;
    @ApiModelProperty(value = "管理员姓名")
    private String cname;
    @ApiModelProperty(value = "操作时间")
    @JsonDeserialize(using =MyDateTimeDeserializer.class)
    @JsonSerialize(using = MytDateTimeSerializer.class)
    private LocalDateTime operTime;
    @ApiModelProperty(value = "业务模块")
    private String module;
    @ApiModelProperty(value = "操作类型")
    private String operType;
    @ApiModelProperty(value = "操作资源")
    private String operObj;
    @ApiModelProperty(value = "详情")
    private String remark;
}
