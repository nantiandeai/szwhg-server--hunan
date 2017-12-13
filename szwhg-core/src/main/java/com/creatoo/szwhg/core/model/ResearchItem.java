package com.creatoo.szwhg.core.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yunyan on 2017/12/6.
 */
@Data
@ApiModel(value = "问卷调查项")
public class ResearchItem {
    @NotNull @ApiModelProperty(value = "标题")
    private String title;
    @NotNull @ApiModelProperty(value = "题目类型")
    private ResearchItemType type;
    @NotNull @ApiModelProperty(value = "选择项内容列表")
    private List<String> contents;
    @ApiModelProperty(value = "每个选择项的人数统计")
    private List<Long> counts;

    /**
     * 增加问卷统计值
     * @param result
     */
    public void increase(String result){
        if(counts==null) {
            counts=new ArrayList<>();
            for(int i=0;i<contents.size();i++){
                counts.add(0l);
            }
        }
        String[] seqs=StringUtils.split(result,","); //答案序号
        for(String seq:seqs){
            int index=Integer.parseInt(seq);
            counts.set(index,counts.get(index)+1l);
        }
    }
}
