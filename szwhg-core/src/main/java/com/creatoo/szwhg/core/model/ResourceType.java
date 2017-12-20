package com.creatoo.szwhg.core.model;

import io.swagger.annotations.ApiModel;

/**
 * Created by yunyan on 2017/9/7.
 */
@ApiModel(value = "资源类型")
public enum ResourceType {
    Activity("活动"),Train("培训"),Information("资讯"),ArtTeam("文化团队"),ArtWorks("征集作品"),ArtTalent("文艺人才"),DigitalShow("数字展览"),CultureSupply("文化配送"),
    heritageDirectory("非遗名录"),heritageSuccessor("传承人"),Venue("场馆"),VolunteerRecruit("招募活动"),LiveVideos("直播"), Demands ("录播");
    private String desc;

    private ResourceType(String _desc){
        this.desc=_desc;
    }
}
