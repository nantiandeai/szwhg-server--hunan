package com.creatoo.szwhg.venue.model;

import com.creatoo.szwhg.core.model.UserOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

/**
 * Created by yunyan on 2017/9/12.
 */
@Data
@Document(collection = "CG_ORDER")
@ApiModel(value = "场馆订单")
public class RoomOrder extends UserOrder {
    @ApiModelProperty(value = "场馆地址")
    private String address;
    @ApiModelProperty(value = "场馆名称")
    private String venueName;
    @NotNull
    @ApiModelProperty(value = "活动室id",required = true)
    private String roomId;
    @NotNull
    @ApiModelProperty(value = "活动室名称",required = true)
    private String roomName;
    @NotNull
    @ApiModelProperty(value = "预定场次",required = true)
    private List<OrderItm> itms;
    @ApiModelProperty(value = "订单状态")
    private RoomOrderStatus status;
    @ApiModelProperty(value = "联系电话")
    private String telePhone;
    @ApiModelProperty(value = "用途")
    private String use;
    @ApiModelProperty(value = "验票码")
    private String code;
    @ApiModelProperty(value = "是否已验票")
    private Boolean hasChecked;

    @JsonIgnore
    public Boolean getHistoryRoomOrder(){
        if (itms != null && itms.size()>0) {
            itms.sort((Comparator.comparing(OrderItm::getItmDate)));
            OrderItm roomItm = itms.get(itms.size() - 1);
            if (roomItm.getItmDate().isBefore(LocalDate.now())) return true;
            if (roomItm.getItmDate().equals(LocalDate.now())){
                if (roomItm.getItmEndtime().isBefore(LocalTime.now())) return true;
            }
        }
        return false;
    }

    @JsonIgnore
    public boolean isOutDate(){
        if(itms==null || itms.size()==0) return false;
        itms.get(0).getItmDate();
        LocalDateTime now=LocalDateTime.now();
        LocalDateTime itmTime=itms.get(0).getItmDate().atTime(itms.get(0).getItmEndtime());
        if(now.isBefore(itmTime)) return false;
        else return true;
    }
}
