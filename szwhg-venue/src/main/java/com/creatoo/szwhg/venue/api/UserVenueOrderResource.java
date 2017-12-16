package com.creatoo.szwhg.venue.api;

import com.creatoo.szwhg.core.rest.AbstractResource;
import com.creatoo.szwhg.core.rest.Pagination;
import com.creatoo.szwhg.user.service.UserInfoService;
import com.creatoo.szwhg.venue.model.RoomOrder;
import com.creatoo.szwhg.venue.model.VenueRoom;
import com.creatoo.szwhg.venue.service.VenueService;
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

/**
 * Created by yunyan on 2017/10/29.
 */
@Component
@Path("/user/users")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "会员场馆订单管理", produces = "application/json")
public class UserVenueOrderResource extends AbstractResource {

    @Autowired
    private UserInfoService userService;
    @Autowired
    private VenueService venueService;

    @GET @Path("/{id}/orders/venueroom")
    @ApiOperation("查询会员场馆活动室订单")
    public Page<RoomOrder> getUserVenueOrders(@PathParam("id")String id, @Pagination Pageable pageable){
        String search="userId:"+id;
        Page<RoomOrder> roomOrders = venueService.findAllOrder(search,pageable);
        roomOrders.forEach(roomOrder -> {
            VenueRoom venueRoom = venueService.getRoomById(roomOrder.getRoomId());
            if (venueRoom != null) roomOrder.setTelePhone(venueRoom.getTelephone());
        });
        return roomOrders;
    }

    @GET @Path("/{id}/orders/venueroom/history")
    @ApiOperation("查询会员场馆活动室历史订单")
    public Page<RoomOrder> getUserVenueROrdersHistory(@PathParam("id")String id, @Pagination Pageable pageable){
        return venueService.findHistoryOrder(id,pageable);
    }

    @GET @Path("/{id}/orders/venueroom/current")
    @ApiOperation("查询会员场馆活动室当前订单")
    public Page<RoomOrder> getUserVenueROrdersCurrent(@PathParam("id")String id, @Pagination Pageable pageable){
        return venueService.findCurrentOrder(id,pageable);
    }

}

