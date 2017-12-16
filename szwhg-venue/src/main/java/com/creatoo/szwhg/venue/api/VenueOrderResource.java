package com.creatoo.szwhg.venue.api;

import com.creatoo.szwhg.core.model.CancelLog;
import com.creatoo.szwhg.core.rest.AbstractResource;
import com.creatoo.szwhg.core.rest.Pagination;
import com.creatoo.szwhg.venue.model.RoomOrder;
import com.creatoo.szwhg.venue.service.VenueService;
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
 * Created by yunyan on 2017/11/27.
 */
@Component
@Path("/venue/orders")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "场馆订单管理")
public class VenueOrderResource extends AbstractResource {
    @Autowired
    private VenueService venueService;

    @POST  @ApiOperation("创建活动室订单")
    public Response createOrder(@Valid @NotNull RoomOrder order){
        String orderid=venueService.createRoomOrder(order);
        return this.successCreate(orderid);
    }

    @PUT  @Path("/{orderid}/cancel")
    @ApiOperation("取消订单")
    public Response cancelOrder(@PathParam("orderid")String orderid, CancelLog log){
        venueService.cancelOrder(orderid,log);
        return this.successUpdate();
    }

    @GET  @ApiOperation("查询订单列表")
    public Page<RoomOrder> findAll( @QueryParam("search")String search, @Pagination Pageable pageable){
        return venueService.findAllOrder(search,pageable);
    }
    @GET @Path("/{orderid}") @ApiOperation("查询订单详情")
    public RoomOrder getOrder(@PathParam("orderid") String orderid){
        return venueService.getOrderById(orderid);
    }

    @PUT @Path("/{orderid}/pass")
    @ApiOperation("通过订单")
    public Response passOrder(@PathParam("orderid") String orderid){
        venueService.passOrder(orderid);
        return this.successUpdate();
    }

}
