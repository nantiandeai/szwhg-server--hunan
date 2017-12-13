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
 * Created by yunyan on 2017/8/21.
 */
@Data
@Document(collection = "BS_MENU")
@ApiModel(value = "菜单")
public class Menu extends IdEntity implements TreeNode<Menu> {
    @ApiModelProperty(value = "菜单编码")
    private String code;
    @ApiModelProperty(value = "菜单名称")
    private String name;
    @ApiModelProperty(value = "菜单URL")
    private String url;
    @ApiModelProperty(value = "菜单图标")
    private String icon;
    @ApiModelProperty(value = "显示顺序")
    private int seqno;
    @DBRef @ApiModelProperty(value = "父节点")
    private Menu parent;
    @DBRef @JsonIgnore
    private List<Menu> ancestors;
    @Transient
    @ApiModelProperty(value = "子节点")
    private List<Menu> children=new ArrayList<>();
}
