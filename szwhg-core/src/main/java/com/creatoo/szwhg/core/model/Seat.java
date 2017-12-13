package com.creatoo.szwhg.core.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by yunyan on 2017/9/8.
 */
@Getter
@Setter
@ApiModel(value = "座位")
public class Seat {
    @ApiModelProperty(value = "验票码")
    private String code;
    @ApiModelProperty(value = "座位号")
    private String seatNo;
    @ApiModelProperty(value = "是否使用")
    private Boolean used;

    @Override
    public boolean equals(Object obj){
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        final Seat other = (Seat)obj;
        if(seatNo != other.seatNo){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + seatNo.hashCode();
        return result;
    }
}
