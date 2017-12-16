package com.creatoo.szwhg.activity.model;

import com.creatoo.szwhg.base.model.CultureBrand;
import com.creatoo.szwhg.base.model.Unit;
import com.creatoo.szwhg.core.model.*;
import com.creatoo.szwhg.venue.model.VenueRoom;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yunyan on 2017/8/17.
 */
@Data
@Document(collection = "HD_ACTIVITY")
@ApiModel(value = "预定活动")
public class Activity extends AuditEntity {
    @NotNull  @ApiModelProperty(value = "活动名称")
    private String name;
    @NotNull @ApiModelProperty(value = "活动类型")
    private String[] activityType;
    @NotNull @ApiModelProperty(value = "艺术类型")
    private String[] artType;
    @ApiModelProperty(value = "封面图片")
    private String coverPic;
    @ApiModelProperty(value = "活动上线状态")
    private OnlineStatus onlineStatus;
    @ApiModelProperty(value = "是否推荐")
    private Boolean isRecommend;
    @ApiModelProperty(value = "活动举行开始日期")
    @JsonDeserialize(using =MyDateDeserializer.class)
    @JsonSerialize(using = MytDateSerializer.class)
    private LocalDate holdStartDate;
    @ApiModelProperty(value = "活动举行结束日期")
    @JsonDeserialize(using =MyDateDeserializer.class)
    @JsonSerialize(using = MytDateSerializer.class)
    private LocalDate holdEndDate;
    @ApiModelProperty(value = "预约开始时间")
    @JsonDeserialize(using =MyDateTimeDeserializer.class)
    @JsonSerialize(using = MytDateTimeSerializer.class)
    private LocalDateTime signStartTime;
    @ApiModelProperty(value = "预约结束时间")
    @JsonDeserialize(using =MyDateTimeDeserializer.class)
    @JsonSerialize(using = MytDateTimeSerializer.class)
    private LocalDateTime signEndTime;
    @ApiModelProperty(value = "活动地址")
    private String address;
    @ApiModelProperty(value = "是否收费")
    private Boolean isCharge;
    @ApiModelProperty(value = "是否实名")
    private Boolean isAuthenticate;
    @NotNull @ApiModelProperty(value = "预定类型")
    private ReserveType reserveType;
    @ApiModelProperty(value = "每人允许预定数量")
    private Integer perAllow;
    @ApiModelProperty(value = "每场允许预定数量")
    private Integer totalAllow;
    @ApiModelProperty(value = "保留座位")
    private List<Grid> retainSeats;
    @ApiModelProperty(value = "活动描述")
    private String desc;
    @ApiModelProperty(value = "联系电话")
    private String contactPhone;
    @ApiModelProperty(value = "附件文件名")
    private String attachName;
    @ApiModelProperty(value = "附件")
    private String attach;
    @ApiModelProperty(value = "坐标", required = true)
    private Coordinate coordinate;
    @DBRef  @NotNull @ApiModelProperty(value = "归属机构")
    private Unit unit;
    @ApiModelProperty(value = "数据归属部门id")
    private String dataDeptId;
    @DBRef @ApiModelProperty(value = "活动室")
    private VenueRoom venueRoom;
    @ApiModelProperty(value = "场次列表")
    private List<ActivityItm> itms;
    @JsonIgnore @ApiModelProperty(value = "数字资源列表")
    private List<DigitInfo> digits;
    @JsonIgnore @ApiModelProperty(value = "流程信息列表")
    private List<FlowLog> flowLogs;
    @JsonIgnore @ApiModelProperty(value = "调查问卷")
    private Research research;
    @JsonIgnore @ApiModelProperty(value = "评论")
    private List<Comment> comments;
    @ApiModelProperty(value = "简介")
    private String brief;
    @DBRef @ApiModelProperty(value = "所属文化品牌")
    private CultureBrand brand;

    @JsonIgnore
    public ActivityItm getItmById(String itmId){
        ActivityItm itm=null;
        for(ActivityItm one:itms){
            if(itmId.equals(one.getId())){
                itm=one;
                break;
            }
        }
        return itm;
    }

    @JsonIgnore
    public int totalSeats(){
        if(reserveType== ReserveType.free) return totalAllow;
        else if(reserveType== ReserveType.online) return allSeats().size();
        else return Integer.MAX_VALUE;
    }

    @JsonIgnore
    public List<Grid> allSeats(){
        List<Grid> seats=new ArrayList<>();
        List<Grid> grids=venueRoom.getSeatTemplate().getGrids();
        for(Grid grid:grids){
            if(grid.getType()== GridType.enable) seats.add(grid);
        }
        if(retainSeats!=null){//删除保留座位
            for(Grid retain:retainSeats){
                Grid del=null;
                for(Grid seat:seats){
                    if(retain.getColumn()==seat.getColumn() && retain.getRow()==seat.getRow()){
                        del=seat;
                        break;
                    }
                }
                if(del!=null) seats.remove(del);
            }
        }
        return seats;
    }

    @JsonIgnore
    public List<Grid> occupSeats(List<String> seatNos){
        List<Grid> seats=new ArrayList<>();
        List<Grid> grids=venueRoom.getSeatTemplate().getGrids();
        if (grids != null && grids.size()>0) {
            for (Grid grid : grids) {
                seatNos.forEach(seatNo -> {
                    if (grid.getSeatNo().equals(seatNo)) {
                        seats.add(grid);
                    }
                });
            }
        }
        return seats;
    }
}
