package com.creatoo.szwhg.activity.api;

import com.creatoo.szwhg.activity.model.ActOrderStatus;
import com.creatoo.szwhg.activity.model.ActivityOrder;
import com.creatoo.szwhg.activity.service.ActivityService;
import com.creatoo.szwhg.core.model.CancelLog;
import com.creatoo.szwhg.core.rest.AbstractResource;
import com.creatoo.szwhg.core.rest.Pagination;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by yunyan on 2017/8/18.
 */
@Component
@Path("/activity/orders")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "活动订单管理", produces = "application/json")
public class ActivityOrderResource extends AbstractResource{
    @Autowired
    private ActivityService actService;

    @POST @ApiOperation("创建订单")
    public Response createOrder(@Valid @NotNull ActivityOrder order){
        String id=actService.createOrder(order);
        return this.successCreate(id);
    }
    @GET  @ApiOperation("查询活动订单列表")
    public Page<ActivityOrder> findAll(@QueryParam("search")String search, @Pagination Pageable pageable){
        return actService.findAllOrder(search,pageable);
    }
    @GET @Path("/{orderid}") @ApiOperation("查询活动订单详情")
    public ActivityOrder getOrder(@PathParam("orderid") String orderid){
        return actService.getOrderById(orderid);
    }

    @PUT @Path("/{orderid}/cancel")
    @ApiOperation("取消订单")
    public Response cancelOrder(@PathParam("orderid")String orderid, CancelLog log){
        actService.cancelOrder(orderid,log);
        return this.successUpdate();
    }

    @PUT @Path("/{orderid}/drawn")
    @ApiOperation("订单出票")
    public Response drawnOrder(@PathParam("orderid")String orderid){
        actService.changeOrderStatus(orderid, ActOrderStatus.drawn);
        return this.successUpdate();
    }

}
