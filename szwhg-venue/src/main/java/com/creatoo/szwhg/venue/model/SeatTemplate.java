package com.creatoo.szwhg.venue.model;

import com.creatoo.szwhg.core.model.Grid;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 座位模板
 * Created by yunyan on 2017/9/2.
 */
@Data
public class SeatTemplate {
    @ApiModelProperty(value = "行总数")
    private int rows;
    @ApiModelProperty(value = "列总数")
    private int columns;
    @ApiModelProperty(value = "网格列表")
    private List<Grid> grids;
}
