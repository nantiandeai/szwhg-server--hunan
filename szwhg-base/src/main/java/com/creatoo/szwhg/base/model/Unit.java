package com.creatoo.szwhg.base.model;

import com.creatoo.szwhg.core.model.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 组织
 * Created by yunyan on 2017/8/4.
 */
@Data
@Document(collection = "BS_UNIT")
@ApiModel(value = "组织")
public class Unit extends IdEntity {
    public static final String Org_Type="org";
    public static final String Dep_Type="dep";
    @NotNull
    @ApiModelProperty(value = "组织名称",required = true)
    private String name;
    @NotNull @ApiModelProperty(value = "组织类型，org-机构，dep-部门",required = true)
    private String type=Org_Type;
    @ApiModelProperty(value = "联系人姓名")
    private String contactName;
    @ApiModelProperty(value = "联系人电话")
    private String contactPhone;
    @ApiModelProperty(value = "LOGO图片")
    private String logoPic;
    @ApiModelProperty(value = "封面图片")
    private String coverPic;
    @ApiModelProperty(value = "背景图片")
    private String backPic;
    @NotNull @ApiModelProperty(value = "管理区域，行政代码")
    private String region;
    @ApiModelProperty(value = "地址")
    private String address;
    @ApiModelProperty(value = "站点URL")
    private String site;
    @ApiModelProperty(value = "简介")
    private String remark;
    @DBRef @NotNull
    @ApiModelProperty(value = "父亲")
    private Unit parent;
    @DBRef
    @ApiModelProperty(value = "上级管理组织")
    private Unit upper;
    @DBRef   @JsonIgnore
    private List<Unit> ancestors;
    @ApiModelProperty(value = "显示顺序号")
    private int seqno;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) @ApiModelProperty(value = "是否有机构子节点")
    private boolean hasOrgChildren;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) @ApiModelProperty(value = "是否有部门子节点")
    private boolean hasDepChildren;
}
