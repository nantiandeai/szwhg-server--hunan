package com.creatoo.szwhg.base.model;

import com.creatoo.szwhg.core.model.IdEntity;
import com.creatoo.szwhg.core.model.MyDateTimeDeserializer;
import com.creatoo.szwhg.core.model.MytDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by yunyan on 2017/10/10.
 */
@Data
@Document(collection = "BS_INFORMATION")
@ApiModel(value = "资讯")
public class Information extends IdEntity {
    @NotNull  @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "封面")
    private String coverPic;
    @DBRef @ApiModelProperty(value = "所属栏目")
    private List<Column> column;
    @ApiModelProperty(value = "来源")
    private String source;
    @ApiModelProperty(value = "作者")
    private String author;
    @ApiModelProperty(value = "发布时间")
    @JsonDeserialize(using =MyDateTimeDeserializer.class)
    @JsonSerialize(using = MytDateTimeSerializer.class)
    private LocalDateTime publishTime;
    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "附件")
    private String attach;
    @ApiModelProperty(value = "附件文件名")
    private String attachName;
    @ApiModelProperty(value = "是否推荐")
    private Boolean isRecommend;
    @ApiModelProperty(value = "是否发布")
    private Boolean isPublish;
    @ApiModelProperty(value = "简介")
    private String brief;
}
