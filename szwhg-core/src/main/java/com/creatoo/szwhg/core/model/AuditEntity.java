package com.creatoo.szwhg.core.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

/**
 * Created by yunyan on 2017/8/3.
 */
@Data
@ApiModel(value = "审计基础类")
public abstract class AuditEntity extends IdEntity {
    @CreatedBy  @ApiModelProperty(value = "创建人")
    private AuditUser creator;
    @CreatedDate
    @ApiModelProperty(value = "创建时间")
    @JsonDeserialize(using = MyDateTimeDeserializer.class)
    @JsonSerialize(using = MytDateTimeSerializer.class)
    private LocalDateTime createTime;
    @LastModifiedBy   @ApiModelProperty(value = "最后修改人")
    private AuditUser lastModifier;
    @LastModifiedDate
    @ApiModelProperty(value = "最后修改时间")
    @JsonDeserialize(using = MyDateTimeDeserializer.class)
    @JsonSerialize(using = MytDateTimeSerializer.class)
    private LocalDateTime lastModifiedTime;

}
