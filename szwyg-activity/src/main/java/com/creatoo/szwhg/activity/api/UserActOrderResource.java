package com.creatoo.szwhg.activity.api;

import com.creatoo.szwhg.activity.model.ActivityOrder;
import com.creatoo.szwhg.activity.service.ActivityService;
import com.creatoo.szwhg.core.rest.AbstractResource;
import com.creatoo.szwhg.core.rest.Pagination;
import com.creatoo.szwhg.user.service.UserInfoService;
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
@Api(value = "会员活动订单管理", produces = "application/json")
public class UserActOrderResource extends AbstractResource {
    @Autowired
    private UserInfoService userService;
    @Autowired
    private ActivityService activityService;



    @GET  @Path("/{userid}/orders/activity/current")
    @ApiOperation("查询用户当前的活动订单列表")
    public List<ActivityOrder> findAll4UserCurrent(@PathParam("userid") String userid){
        return this.activityService.getUserOrders(userid);
    }

    @GET  @Path("/{userid}/orders/activity/history")
    @ApiOperation("查询用户历史的活动订单列表")
    public Page<ActivityOrder> findAll4UserHistory(@PathParam("userid") String userid, @Pagination Pageable pageable){
        return this.activityService.getUserOrdersHistory(userid,pageable);
    }
}

