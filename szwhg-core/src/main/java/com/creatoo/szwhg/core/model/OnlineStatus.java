package com.creatoo.szwhg.core.model;

import io.swagger.annotations.ApiModel;

/**
 * 在线状态，物品从编辑到发布的状态常量
 * Created by yunyan on 2017/9/2.
 */
@ApiModel(value = "物品在线状态")
public enum OnlineStatus {
    WaitCommit("待提交"),WaitAudit("待审核"),Audited("已审核"),Published("已上架"),Offline("已下架"),Recycled("已回收");

    private String desc;

    private OnlineStatus(String _desc){
        this.desc=_desc;
    }
}
