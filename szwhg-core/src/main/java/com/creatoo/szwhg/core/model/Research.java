package com.creatoo.szwhg.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 *  问卷
 * Created by wuxiangliang on 2017/10/14.
 */
@Data
@ApiModel(value = "问卷")
public class Research {
    @ApiModelProperty(value = "是否发布")
    private Boolean isPublished;
    @NotNull @ApiModelProperty(value = "调查题目列表")
    private List<ResearchItem> items;
    @ApiModelProperty(value = "参加总人数")
    private Long peoples;
    @JsonIgnore  @ApiModelProperty(value = "会员调查表")
    private List<UserResearchSheet> sheets;


    public void addResearchSheet(UserResearchSheet sheet){
        if(sheets==null) sheets=new ArrayList<>();
        sheets.add(sheet);
        if(peoples==null) peoples=0l;
        peoples=peoples+1;  //统计总人数加1
        for(ResearchItem item:items){
            String title=item.getTitle();
            List<String> titles=sheet.getTitles();
            int index=titles.indexOf(title);  //标题序号
            if(index==-1) continue;
            if(item.getType()==ResearchItemType.question) continue;
            String result=sheet.getResults().get(index); //问题结果
            item.increase(result);
        }
    }

}