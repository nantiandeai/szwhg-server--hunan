package com.creatoo.szwhg.base.model;

import com.creatoo.szwhg.core.model.DictsortType;
import com.creatoo.szwhg.core.model.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by yunyan on 2017/8/8.
 */
@Data
@Document(collection = "BS_CODE")
@ApiModel(value = "代码类型")
public class CodeType  extends IdEntity {
    @ApiModelProperty(value = "字典类型")
    private DictsortType type;
    @Indexed(unique = true)
    @ApiModelProperty(value = "类型编码")
    private String typeCode;
    @ApiModelProperty(value = "类型名称")
    private String typeName;
    @ApiModelProperty(value = "代码项")
    private List<Code> codes;
}
