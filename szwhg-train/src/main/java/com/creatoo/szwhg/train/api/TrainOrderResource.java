package com.creatoo.szwhg.train.api;

import com.creatoo.szwhg.core.rest.AbstractResource;
import com.creatoo.szwhg.core.rest.Pagination;
import com.creatoo.szwhg.train.model.Interview;
import com.creatoo.szwhg.train.model.TrainOrder;
import com.creatoo.szwhg.train.service.TrainOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 培训订单管理
 * Created by wangxl on 2017/9/7.
 */
@Component
@Path("/train/orders")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "培训订单管理", produces = "application/json")
public class TrainOrderResource extends AbstractResource {
    @Autowired
    private TrainOrderService trainOrderService;

    @GET
    @ApiOperation("查询培训订单列表")
    public Page<TrainOrder> findAll( @QueryParam("search") String search, @Pagination Pageable pageable){
        return this.trainOrderService.findAll(search, pageable);
    }

    @Path("/{orderid}")
    @ApiOperation("查询培训订单详情")
    public TrainOrder findById(@PathParam("orderid") String orderid){
        return this.trainOrderService.findById(orderid);
    }

    @POST
    @ApiOperation("创建订单")
    public Response add( TrainOrder order){
        return this.successCreate(this.trainOrderService.add(order));
    }

    @PUT
    @Path("/{orderid}/cancel")
    @ApiOperation("取消订单")
    public Response cancel( @PathParam("orderid") String orderid){
        this.trainOrderService.cancelOrder(orderid);
        return this.successUpdate();
    }

    @PUT
    @Path("/{orderid}/pass")
    @ApiOperation("报名通过")
    public Response checkon( @PathParam("orderid") String orderid){
        this.trainOrderService.passOrder(orderid);
        return this.successUpdate();
    }

    @PUT
    @Path("/{orderid}/reject")
    @ApiOperation("报名拒绝")
    public Response checkoff( @PathParam("orderid") String orderid){
        this.trainOrderService.rejectOrder(orderid);
        return this.successUpdate();
    }



    @PUT @Path("/{orderid}/interview")
    @ApiOperation("邀请面试")
    public Response mianshiSMS(@PathParam("orderid") String orderid, Interview interview){
        trainOrderService.inviteInterview(orderid,interview);
        return this.successUpdate();
    }

    @GET
    @Path("/{orderid}/itms")
    @ApiOperation("培训课时签到列表")
    public Response getSign(@PathParam("id") String trainid, @PathParam("orderid") String orderid){
        trainOrderService.getTrainItm(orderid);
        return this.successUpdate();
    }

    @PUT @Path("/{orderid}/itms/{itmid}/sign")
    @ApiOperation("培训课时签到")
    public Response isSign(@PathParam("orderid") String orderid,@PathParam("itmid") String itmid){
        trainOrderService.signTrain(orderid,itmid);
        return this.successUpdate();
    }

}
