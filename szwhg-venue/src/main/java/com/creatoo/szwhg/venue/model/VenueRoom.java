package com.creatoo.szwhg.venue.model;

import com.creatoo.szwhg.base.model.Unit;
import com.creatoo.szwhg.core.model.AuditEntity;
import com.creatoo.szwhg.core.model.FlowLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by yunyan on 2017/8/17.
 */
@Data
@Document(collection = "CG_ROOM")
@ApiModel(value = "场馆活动室")
public class VenueRoom extends AuditEntity{
    @NotNull  @ApiModelProperty(value = "活动室名称")
    private String name;
    @DBRef  @NotNull  @ApiModelProperty(value = "所属场馆")
    private Venue venue;
    @DBRef  @NotNull  @ApiModelProperty(value = "所属文化馆")
    private Unit unit;
    @NotNull @ApiModelProperty(value = "图片")
    private String coverPic;
    @NotNull @ApiModelProperty(value = "类别")
    private String type;
    @NotNull @ApiModelProperty(value = "活动室设施")
    private String facilities;
    @ApiModelProperty(value = "活动室面积")
    private Float  area;
    @ApiModelProperty(value = "容纳人数")
    private Integer totalPeoples;
    @NotNull @ApiModelProperty(value = "场地描述")
    private String desc;
    @NotNull @ApiModelProperty(value = "联系电话")
    private String telephone;
    @NotNull @ApiModelProperty(value = "是否实名")
    private Boolean isAuthenticate;
    @ApiModelProperty(value = "是否有座位")
    private Boolean hasSeat;
    @ApiModelProperty(value = "座位模板")
    private SeatTemplate seatTemplate;
    @ApiModelProperty(value = "活动室上线状态")
    private String onlineStatus;
    @ApiModelProperty(value = "是否推荐")
    private Boolean isRecommend;
    @ApiModelProperty(value = "活动室场次定义")
    private RoomItmDef itmDef;
    @ApiModelProperty(value = "每人每次预定的场次允许数量")
    private Integer perAllow;
    @ApiModelProperty(value = "流程信息列表")
    private List<FlowLog> flowLogs;
    @ApiModelProperty(value = "简介")
    private String brief;
}
