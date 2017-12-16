package com.creatoo.szwhg.train.model;

import com.creatoo.szwhg.core.model.MyDateDeserializer;
import com.creatoo.szwhg.core.model.MytDateSerializer;
import com.creatoo.szwhg.core.model.TrainOrderStatus;
import com.creatoo.szwhg.core.model.UserOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 培训订单
 * Created by yunyan on 2017/9/3.
 */
@Data
@Document(collection = "PX_ORDERA")
@ApiModel(value = "培训订单")
public class TrainOrder extends UserOrder {
    @ApiModelProperty(value = "培训id", required = true)
    private String trainId;

    @ApiModelProperty(value = "培训名称")
    private String trainName;

    @ApiModelProperty(value = "是否参加面试")
    private Boolean isInterview;

    @NotNull
    @ApiModelProperty(value = "培训地址", required = true)
    private String address;

    @ApiModelProperty(value = "培训结束日期")
    @JsonDeserialize(using =MyDateDeserializer.class)
    @JsonSerialize(using = MytDateSerializer.class)
    private LocalDate trainEndTime;

    @ApiModelProperty(value = "培训开始日期")
    @JsonDeserialize(using =MyDateDeserializer.class)
    @JsonSerialize(using = MytDateSerializer.class)
    private LocalDate trainStartTime;

    @ApiModelProperty(value = "附件")
    private String attachment;

    @ApiModelProperty(value = "订单中的报名名额")
    private Integer peoples;

    @ApiModelProperty(value = "订单中的用户信息", required = true)
    List<TrainOrderUser> enrolUsers;

    @ApiModelProperty(value = "订单状态")
    private TrainOrderStatus orderStatus;

    @ApiModelProperty(value = "签到培训课时")
    private List<TrainItm> trainItms;


    /**
     * 是否已经全部签到
     * @return
     */
    @JsonIgnore
    public boolean isAllChecked() {
        if (trainItms == null) {
            return false;
        }
        boolean checked = true;
        for (TrainItm itm : trainItms) {
            if (!Optional.ofNullable(itm.getIsSign()).orElse(false)) {
                checked = false;
                break;
            }
        }
        return checked;
    }

}
