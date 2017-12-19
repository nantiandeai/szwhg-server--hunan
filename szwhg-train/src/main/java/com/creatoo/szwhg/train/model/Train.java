package com.creatoo.szwhg.train.model;

import com.creatoo.szwhg.base.model.Comment;
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
 * 培训
 * Created by yunyan on 2017/8/17.
 */
@Data
@Document(collection = "PX_TRAIN")
@ApiModel(value = "培训")
public class Train extends AuditEntity {
    @NotNull @ApiModelProperty(value = "标题", required = true)
    private String title;

    @NotNull @ApiModelProperty(value = "封面图", required = true)
    private String picture;

    @NotNull @ApiModelProperty(value = "艺术分类", required = true)
    private String[] artType;

    @NotNull @ApiModelProperty(value = "培训形势", required = true)
    private String[] traType;

    @NotNull @ApiModelProperty(value = "培训地址", required = true)
    private String address;

    @NotNull @ApiModelProperty(value = "坐标", required = true)
    private Coordinate coordinate;

    @NotNull @ApiModelProperty(value = "联系电话", required = true)
    private String contactNumber;

    @NotNull @ApiModelProperty(value = "报名开始时间(yyyy-MM-dd HH:mm:ss)", required = true)
    @JsonDeserialize(using =MyDateTimeDeserializer.class)
    @JsonSerialize(using = MytDateTimeSerializer.class)
    private LocalDateTime enrolStartTime;

    @NotNull @ApiModelProperty(value = "报名结束时间(yyyy-MM-dd HH:mm:ss)", required = true)
    @JsonDeserialize(using =MyDateTimeDeserializer.class)
    @JsonSerialize(using = MytDateTimeSerializer.class)
    private LocalDateTime enrolEndTime;

    @NotNull @ApiModelProperty(value = "培训开始日期(yyyy-MM-dd)")
    @JsonDeserialize(using =MyDateDeserializer.class)
    @JsonSerialize(using = MytDateSerializer.class)
    private LocalDate startDate;

    @NotNull @ApiModelProperty(value = "培训结束日期(yyyy-MM-dd)")
    @JsonDeserialize(using =MyDateDeserializer.class)
    @JsonSerialize(using = MytDateSerializer.class)
    private LocalDate endDate;

    @ApiModelProperty(value = "培训周期时间")
    private List<TrainWeekTimes> weekTimes;

    @ApiModelProperty(value = "培训周期例外时间")
    private List<TrainExceptTimes> exceptTimes;

    @ApiModelProperty(value = "总限制报名人数", required = true)
    private Integer allLimitPeoples;

    @ApiModelProperty(value = "会员限制报名人数", required = true)
    private Integer userLimitPeoples;

    @ApiModelProperty(value = "是否收费。true-收费; false-不收费")
    private Boolean isCharge;

    @ApiModelProperty(value = "报名是否必须实名。true-必须实名; false-不须实名")
    private Boolean isRealName;

    @ApiModelProperty(value = "是否推荐。true-推荐; false-不推荐")
    private Boolean isRecommend;

    @ApiModelProperty(value = "培训介绍")
    private String introduce;

    @ApiModelProperty(value = "附件")
    private String attachment;

    @ApiModelProperty(value = "附件名称")
    private String attachmentName;

    @ApiModelProperty(value = "培训上线状态。（待提交，待审核，已审核，已发布，已下架，已回收）", required = true)
    private OnlineStatus onlineStatus;

    @DBRef
    @ApiModelProperty(value = "归属机构")
    private Unit unit;

    @ApiModelProperty(value = "老师")
    private String trainTeacher;

    @DBRef
    @ApiModelProperty(value = "活动室")
    private VenueRoom venueRoom;

    @ApiModelProperty(value = "培训评论列表")
    private List<Comment> comments;

    @ApiModelProperty(value = "流程信息列表")
    private List<FlowLog> flowLogs;

    @ApiModelProperty(value = "简介")
    private String brief;

    @ApiModelProperty(value = "已报名人数")
    private Integer enrollSum=0;

    @DBRef @ApiModelProperty(value = "所属文化品牌")
    private CultureBrand brand;

    @ApiModelProperty(value = "数据归属部门id")
    private String dataDeptId;
    @JsonIgnore
    public List<TrainItm> items() {
        List<TrainItm> trainItms = new ArrayList<>();
        // 培训周期
        if (weekTimes != null && weekTimes.size() > 0 && (LocalDate.now().isBefore(endDate) || LocalDate.now().equals(endDate))) {
            LocalDate wdate = startDate;
            for (TrainWeekTimes trainWeekTimes : weekTimes) {
                while (wdate.isBefore(endDate) || wdate.equals(endDate)) {
                    if (wdate.getDayOfWeek().toString().equals(trainWeekTimes.getWeekDay().toString().toUpperCase())
                            && trainWeekTimes.getTrainTimes() != null && trainWeekTimes.getTrainTimes().size() > 0) {
                        for (TrainTimes trainTimes : trainWeekTimes.getTrainTimes()) {
                            TrainItm trainItm = new TrainItm();
                            trainItm.setItmDate(wdate);
                            trainItm.setStartTime(trainTimes.getStartTime());
                            trainItm.setEndTime(trainTimes.getEndTime());
                            trainItms.add(trainItm);
                        }
                    }
                    wdate = wdate.plusDays(1);
                }
            }
        }

        // 培训例外周期
        if (exceptTimes != null && exceptTimes.size() > 0) {
            List<TrainItm> trainItms1 = new ArrayList<>();
            for (TrainExceptTimes trainExceptTimes : exceptTimes) {
                if (trainItms.size() > 0) {
                    trainItms.forEach(trainItm -> {
                        if (trainItm.getItmDate().equals(trainExceptTimes.getTrainDate())) {
                            trainItms1.add(trainItm);
                        }
                    });
                }
            }
            if (trainItms1.size() > 0) {
                for (TrainItm trainItm : trainItms1) {
                    trainItms.remove(trainItm);
                }
            }

            for (TrainExceptTimes trainExceptTimes : exceptTimes) {
                if (trainExceptTimes.getTrainDate().isBefore(endDate) || trainExceptTimes.getTrainDate().equals(endDate)) {
                    if (trainExceptTimes.getTrainTimes() != null && trainExceptTimes.getTrainTimes().size() > 0) {
                        for (TrainTimes itm : trainExceptTimes.getTrainTimes()) {
                            TrainItm trainItm = new TrainItm();
                            trainItm.setItmDate(trainExceptTimes.getTrainDate());
                            trainItm.setStartTime(itm.getStartTime());
                            trainItm.setEndTime(itm.getEndTime());
                            trainItms.add(trainItm);
                        }
                    }
                }
            }
        }
        return trainItms;
    }
}
