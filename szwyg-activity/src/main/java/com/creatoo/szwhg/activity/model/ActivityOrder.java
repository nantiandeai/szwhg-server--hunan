package com.creatoo.szwhg.activity.model;

import com.creatoo.szwhg.core.model.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by yunyan on 2017/8/18.
 */
@Data
@Document(collection = "HD_ORDER")
@ApiModel(value = "活动订单")
public class ActivityOrder extends UserOrder {
    @NotNull @ApiModelProperty(value = "活动id")
    private String activityId;
    @NotNull  @ApiModelProperty(value = "活动名称")
    private String activityName;
    @ApiModelProperty(value = "活动地点")
    private String activityAddress;
    @NotNull @ApiModelProperty(value = "场次id",required = true)
    private String itmId;
    @NotNull @ApiModelProperty(value = "预定类型",required = true)
    private ReserveType reserveType;
    @NotNull @ApiModelProperty(value = "场次日期",required = true)
    @JsonDeserialize(using =MyDateDeserializer.class)
    @JsonSerialize(using = MytDateSerializer.class)
    private LocalDate itmDate;
    @NotNull  @ApiModelProperty(value = "场次开始时间",required = true)
    @JsonDeserialize(using =MyTimeDeserializer.class)
    @JsonSerialize(using = MytTimeSerializer.class)
    private LocalTime itmStarttime;
    @NotNull  @ApiModelProperty(value = "场次结束时间",required = true)
    @JsonDeserialize(using =MyTimeDeserializer.class)
    @JsonSerialize(using = MytTimeSerializer.class)
    private LocalTime itmEndtime;
    @NotNull @ApiModelProperty(value = "预定数量")
    private Integer reserveSum;
    @ApiModelProperty(value = "座位列表")
    private List<Seat> seats;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)  @ApiModelProperty(value = "订单状态")
    private ActOrderStatus orderStatus;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) @ApiModelProperty(value = "出票码")
    private String drawnCode;

    @JsonIgnore
    public boolean isOutDate(){
        LocalDateTime now=LocalDateTime.now();
        LocalDateTime itmTime=itmDate.atTime(itmEndtime);
        if(now.isBefore(itmTime)) return false;
        else return true;
    }

    /**
     * 是否已经全部检票
     * @return
     */
    @JsonIgnore
    public boolean isAllChecked(){
        if(seats==null) return false;
        boolean checked=true;
        for(Seat seat:seats){
            if(!Optional.ofNullable(seat.getUsed()).orElse(false)){
                checked=false;
                break;
            }
        }
        return checked;
    }
}
