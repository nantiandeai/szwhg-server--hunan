package com.creatoo.szwhg.base.model;

import com.creatoo.szwhg.core.model.IdEntity;
import com.creatoo.szwhg.core.model.TreeNode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yunyan on 2017/10/7.
 */
@Data
@Document(collection = "BS_REGION")
@ApiModel(value = "全国行政区域")
public class Region extends IdEntity implements TreeNode<Region> {
    @ApiModelProperty(value = "行政代码")
    private String code;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "全称")
    private String fullName;
    @ApiModelProperty(value = "行政级别")
    private String level;
    @ApiModelProperty(value = "显示顺序")
    private int seqno;
    @DBRef
    @ApiModelProperty(value = "父节点")
    private Region parent;
    @DBRef @JsonIgnore
    private List<Region> ancestors;
    @Transient
    @ApiModelProperty(value = "子节点")
    private List<Region> children=new ArrayList<>();
}
