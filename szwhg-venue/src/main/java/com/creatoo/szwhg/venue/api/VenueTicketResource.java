package com.creatoo.szwhg.venue.api;

import com.creatoo.szwhg.core.exception.BsException;
import com.creatoo.szwhg.core.rest.AbstractResource;
import com.creatoo.szwhg.venue.model.RoomOrder;
import com.creatoo.szwhg.venue.service.VenueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by yunyan on 2017/11/27.
 */
@Component
@Path("/venue")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "场馆验票码管理", produces = "application/json")
public class VenueTicketResource extends AbstractResource {
    @Autowired
    private VenueService venueService;

    @GET  @Path("/codes/{code}")
    @ApiOperation("查询验票码订单信息")
    public RoomOrder getOrderByCode(@PathParam("code")String code){
        RoomOrder order=venueService.findOrderByValidCode(code);
        if(order==null) throw new BsException("找不到订单");
        return order;
    }

    @PUT  @Path("/codes/{code}/check")
    @ApiOperation("验票")
    public Response checkByCode(@PathParam("code")String code){
        venueService.checkCode(code);
        return this.successUpdate();
    }
}
