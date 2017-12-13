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

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by yunyan on 2017/10/10.
 */
@Data
@Document(collection = "BS_COLUMN")
@ApiModel(value = "栏目")
public class Column extends IdEntity implements TreeNode<Column> {
    @NotNull  @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "是否可用")
    private Boolean enable;
    @ApiModelProperty(value = "显示顺序")
    private int seqno;
    @DBRef
    @ApiModelProperty(value = "父节点")
    private Column parent;
    @DBRef @JsonIgnore
    private List<Column> ancestors;
    @Transient
    @ApiModelProperty(value = "子节点")
    private List<Column> children=new ArrayList<>();
    @ApiModelProperty(value = "简介")
    private String brief;
}
