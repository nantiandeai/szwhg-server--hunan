package com.creatoo.szwhg.venue.model;

import com.creatoo.szwhg.base.model.Unit;
import com.creatoo.szwhg.core.model.AuditEntity;
import com.creatoo.szwhg.base.model.Comment;
import com.creatoo.szwhg.core.model.Coordinate;
import com.creatoo.szwhg.core.model.DigitInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by yunyan on 2017/8/16.
 */
@Data
@Document(collection = "CG_VENUE")
@ApiModel(value = "场馆")
public class Venue extends AuditEntity {
    @NotNull @ApiModelProperty(value = "场馆名称")
    private String name;
    @NotNull @ApiModelProperty(value = "图片")
    private String pic;
    @NotNull @ApiModelProperty(value = "类型")
    private String type;
    @DBRef @NotNull
    @ApiModelProperty(value = "归属分馆")
    private Unit unit;
    @NotNull @ApiModelProperty(value = "场馆地址")
    private String address;
    @ApiModelProperty(value = "联系人")
    private String contact;
    @NotNull @ApiModelProperty(value = "联系电话")
    private String contactMobile;
    @NotNull @ApiModelProperty(value = "场馆坐标")
    private Coordinate coordinate;
    @ApiModelProperty(value = "场馆描述")
    private String desc;
    @ApiModelProperty(value = "场馆开放时间")
    private String openDateTime;
    @ApiModelProperty(value = "是否推荐")
    private Boolean isRecommend;
    @ApiModelProperty(value = "是否发布")
    private Boolean isPublish;
    @NotNull @ApiModelProperty(value = "简介")
    private String brief;
    @ApiModelProperty(value = "数字资源")
    private List<DigitInfo> digitInfos;
}
