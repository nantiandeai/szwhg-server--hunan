package com.creatoo.szwhg.core.model;

import io.swagger.annotations.ApiModel;

/**
 * 培训订单状
 * Created by wangxl on 2017/9/12.
 */
@ApiModel("培训订单状态")
public enum TrainOrderStatus {
    WaitAudit("等待审核"), Success("报名成功"), Rejected("报名拒绝"), Canceled("取消报名");

    private String desc;

    private TrainOrderStatus(String _desc){
        this.desc=_desc;
    }

    public String desc(){
        return this.desc;
    }
}
