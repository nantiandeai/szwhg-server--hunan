package com.creatoo.szwhg.activity.api;

import com.creatoo.szwhg.activity.model.ActivityOrder;
import com.creatoo.szwhg.activity.service.ActivityService;
import com.creatoo.szwhg.core.exception.BsException;
import com.creatoo.szwhg.core.rest.AbstractResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by wuxiangliang on 2017/11/10.
 */
@Component
@Path("/activity")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "活动取验票管理", produces = "application/json")
public class ActivityTicketResource extends AbstractResource {
    @Autowired
    private ActivityService actService;

    @GET  @Path("/drawncodes/{code}")
    @ApiOperation("查询出票码订单信息")
    public ActivityOrder getOrderByDrawn(@PathParam("code") String code) {
        ActivityOrder order= actService.findValidOrderByDrawncode(code);
        if(order==null) throw new BsException("找不到订单信息");
        return order;
    }
    @GET  @Path("/seatcodes/{code}")
    @ApiOperation("查询座位码订单信息")
    public ActivityOrder getOrderBySeatCode(@PathParam("code") String code){
        ActivityOrder order=actService.findValidOrderBySeatCode(code);
        if(order==null) throw new BsException("找不到订单信息");
        return order;
    }

    @PUT @Path("/seatcodes/{code}/check")
    @ApiOperation("验票")
    public ActivityOrder checkSeat(@PathParam("code") String code) {
        return actService.checkSeat(code);
    }

}
