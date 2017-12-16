package com.creatoo.szwhg.train.model;

import com.creatoo.szwhg.base.model.Unit;
import com.creatoo.szwhg.core.model.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 培训老师
 * Created by yunyan on 2017/9/3.
 */
@Data
@Document(collection = "PX_TEACHER")
@ApiModel(value = "培训老师")
public class TrainTeacher extends IdEntity {
    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "照片")
    private String pic;

    @ApiModelProperty(value = "性别。0-女；1-男。")
    private Integer sex;

    @ApiModelProperty(value = "身高")
    private Integer bodyHeight;

    @ApiModelProperty(value = "出生日期")
    private Date birthDay;

    @ApiModelProperty(value = "联系手机")
    private String contactNumber;

    @ApiModelProperty(value = "联系地址")
    private String contactAddress;

    @ApiModelProperty(value = "头衔")
    private String userRank;

    @ApiModelProperty(value = "兴趣爱好")
    private String hobby;

    @ApiModelProperty(value = "政治面貌(字典)")
    private String politicalType;

    @ApiModelProperty(value = "职业(字典)")
    private String occupationType;

    @ApiModelProperty(value = "学历(字典)")
    private String educationType;

    @ApiModelProperty(value = "艺术分类(字典)")
    private String artType;

    @ApiModelProperty(value = "注册日期")
    private String regDate;

    @ApiModelProperty(value = "老师介绍")
    private String introduce;

    @DBRef
    @ApiModelProperty(value = "所属分馆")
    private Unit unit;
}
