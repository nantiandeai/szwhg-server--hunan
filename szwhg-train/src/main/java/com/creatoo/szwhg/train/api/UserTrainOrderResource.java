package com.creatoo.szwhg.train.api;

import com.creatoo.szwhg.core.rest.AbstractResource;
import com.creatoo.szwhg.core.rest.Pagination;
import com.creatoo.szwhg.train.model.TrainOrder;
import com.creatoo.szwhg.train.service.TrainOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by yunyan on 2017/10/29.
 */
@Component
@Path("/user/users")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "会员培训订单管理", produces = "application/json")
public class UserTrainOrderResource  extends AbstractResource {
    @Autowired
    private TrainOrderService trainOrderService;

    @GET  @Path("/{userid}/orders/train/current")
    @ApiOperation("查询用户当前的培训订单列表")
    public List<TrainOrder> findAll4UserCurrent(@PathParam("userid") String userid){
        return this.trainOrderService.getUserOrders(userid);
    }

    @GET  @Path("/{userid}/orders/train/history")
    @ApiOperation("查询用户历史的培训订单列表")
    public Page<TrainOrder> findAll4UserHistory(@PathParam("userid") String userid, @Pagination Pageable pageable){
        return this.trainOrderService.findUserHistory(userid, pageable);
    }
}
