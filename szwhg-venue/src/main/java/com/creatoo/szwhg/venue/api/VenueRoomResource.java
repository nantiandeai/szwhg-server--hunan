package com.creatoo.szwhg.venue.api;

import com.creatoo.szwhg.core.model.FlowLog;
import com.creatoo.szwhg.core.rest.AbstractResource;
import com.creatoo.szwhg.core.rest.Pagination;
import com.creatoo.szwhg.venue.model.RoomItm;
import com.creatoo.szwhg.venue.model.RoomItmDef;
import com.creatoo.szwhg.venue.model.VenueRoom;
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
import java.util.List;

/**
 * Created by yunyan on 2017/9/2.
 */
@Component
@Path("/venue/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "场馆活动室管理", produces = "application/json")
public class VenueRoomResource extends AbstractResource{
    @Autowired
    private VenueService venueService;

    @POST @ApiOperation("创建活动室")
    public Response createRoom(@Valid @NotNull VenueRoom room){
        String id=venueService.createRoom(room);
        return this.successCreate(id);
    }
    @GET
    @ApiOperation(value = "查询活动室列表")
    public Page<VenueRoom> findAll(@QueryParam("search") String search, @Pagination Pageable pageable){
        return venueService.findAllRooms(search, pageable);
    }

    @PUT @Path("/{roomid}")
    @ApiOperation("修改活动室")
    public Response editRoom(@PathParam("roomid")String roomid,VenueRoom room){
        venueService.modifyRoom(roomid,room);
        return this.successUpdate();
    }
    @GET @Path("/{roomid}")
    @ApiOperation("查看活动室详情")
    public VenueRoom getRoom(@PathParam("roomid")String roomid){
        return venueService.getRoomById(roomid);
    }

    @DELETE @Path("/{roomid}")
    @ApiOperation("删除活动室")
    public Response deleteRoom(@PathParam("roomid")String roomid){
        venueService.deleteRoom(roomid);
        return this.successDelete();
    }

    @PUT @Path("/{roomid}/itmDef")
    public Response modifyItm(@PathParam("roomid")String roomid, RoomItmDef def){
        venueService.modifyRommItm(roomid,def);
        return this.successUpdate();
    }

    @PUT @Path("/{roomid}/onlineStatus")
    @ApiOperation("变更活动室在线状态")
    public Response auditPassRoom(@PathParam("roomid")String roomid, FlowLog oper){
        venueService.changeRoomOnlineStatus(roomid,oper);
        return this.successUpdate();
    }

    @PUT @Path("/{roomid}/top/true")
    @ApiOperation("置顶活动室")
    public Response recommend(@PathParam("roomid")String roomid){
        VenueRoom room=venueService.getRoomById(roomid);
        room.setIsRecommend(true);
        venueService.modifyRoom(roomid,room);
        return this.successUpdate();
    }

    @PUT @Path("/{roomid}/top/false")
    @ApiOperation("取消置顶")
    public Response unRecommend(@PathParam("roomid")String roomid){
        VenueRoom room=venueService.getRoomById(roomid);
        room.setIsRecommend(false);
        venueService.modifyRoom(roomid,room);
        return this.successUpdate();
    }

    @GET @Path("/{roomid}/itms") @ApiOperation("查询活动室场次预定信息")
    public List<RoomItm> getItms(@PathParam("roomid") String roomid){
        return venueService.getRoomItms(roomid);
    }
}
