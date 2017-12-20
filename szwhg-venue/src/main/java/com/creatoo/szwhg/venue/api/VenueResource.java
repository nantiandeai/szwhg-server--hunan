package com.creatoo.szwhg.venue.api;

import com.creatoo.szwhg.base.service.CommentService;
import com.creatoo.szwhg.core.exception.BsException;
import com.creatoo.szwhg.base.model.Comment;
import com.creatoo.szwhg.base.model.CommentStatus;
import com.creatoo.szwhg.core.model.DigitInfo;
import com.creatoo.szwhg.core.model.ResourceType;
import com.creatoo.szwhg.core.rest.AbstractResource;
import com.creatoo.szwhg.core.rest.Pagination;
import com.creatoo.szwhg.venue.model.Venue;
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
 * Created by yunyan on 2017/8/4.
 */
@Component
@Path("/venue/venues")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "场馆管理")
public class VenueResource  extends AbstractResource {
    @Autowired
    private VenueService venueService;
    @Autowired
    private CommentService commentService ;

    @POST
    @ApiOperation(value = "创建场馆")
    public Response createVenue(@Valid @NotNull Venue venue) {
        String id = venueService.createVenue(venue);
        return this.successCreate(id);
    }

    @GET
    @ApiOperation(value = "查询场馆列表")
    public Page<Venue> findAll(@QueryParam("search") String search, @Pagination Pageable pageable){
        return venueService.findAll(search, pageable);
    }

    @PUT @Path("/{id}") @ApiOperation(value = "修改场馆")
    public Response modifyVenue(@PathParam("id")String id,Venue venue){
        venueService.modifyVenue(id,venue);
        return this.successUpdate();
    }
    @DELETE @Path("/{id}") @ApiOperation(value = "删除场馆")
    public Response deleteVenue(@PathParam("id")String id){
        venueService.deleteVenue(id);
        return this.successDelete();
    }

    @GET @Path("/{id}")
    @ApiOperation(value = "查看场馆信息")
    public Venue getVenue(@PathParam("id") String id){
        return venueService.getById(id);
    }

    @PUT @Path("/{id}/top/true") @ApiOperation(value = "置顶场馆")
    public Response recommend(@PathParam("id")String id){
        Venue venue = venueService.getById(id);
        if (venue == null) throw new BsException("场馆不存在");
        venue.setIsRecommend(true);
        venueService.modifyVenue(id,venue);
        return this.successUpdate();
    }

    @PUT @Path("/{id}/top/false") @ApiOperation(value = "取消置顶")
    public Response unRecommend(@PathParam("id")String id){
        Venue venue = venueService.getById(id);
        if (venue == null) throw new BsException("场馆不存在");
        venue.setIsRecommend(false);
        venueService.modifyVenue(id,venue);
        return this.successUpdate();
    }

    @PUT @Path("/{id}/publish/true") @ApiOperation(value = "场馆发布")
    public Response publish(@PathParam("id")String id){
        Venue venue = venueService.getById(id);
        if (venue == null) throw new BsException("场馆不存在");
         venue.setIsPublish(true);
        venueService.modifyVenue(id,venue);
        return this.successUpdate();
    }

    @PUT @Path("/{id}/publish/false") @ApiOperation(value = "取消发布")
    public Response unPublish(@PathParam("id")String id){
        Venue venue = venueService.getById(id);
        if (venue == null) throw new BsException("场馆不存在");
        venue.setIsPublish(false);
        venueService.modifyVenue(id,venue);
        return this.successUpdate();
    }

    @POST
    @Path("/{id}/comments")
    @ApiOperation("添加评论")
    public Response addComment(@PathParam("id")String venId,Comment comment){
        comment.setObjId(venId);
        comment.setType(ResourceType.Venue);
        String commentid=commentService.addComment(comment);
        return this.successCreate(commentid);
    }

    @DELETE
    @Path("/{id}/comments/{commentid}")
    @ApiOperation("删除评论")
    public Response deleteComment(@PathParam("id")String venId,@PathParam("commentid") String commentid){
        commentService.deleteComment(commentid);
        return this.successDelete();
    }

    @GET
    @Path("/{id}/comments")
    @ApiOperation("获取评论列表")
    public Page<Comment> getComments(@PathParam("id")String trainid,@Pagination Pageable pageable){
        String search = "objId:"+trainid+",status:"+CommentStatus.Pass;
        return commentService.findAll(search,pageable);
    }

    @POST @Path("/{id}/digitinfos")
    @ApiOperation("添加活动纪实")
    public Response createDigitalInfo(@PathParam("id") String id,@Valid @NotNull DigitInfo digitInfo){
        return this.successCreate(venueService.createDigital(id,digitInfo));
    }

    @DELETE @Path("/{id}/digitinfos/{did}")
    @ApiOperation("删除活动纪实")
    public Response deleteDigitalInfo(@PathParam("id") String id,@PathParam("did") String did){
        venueService.deleteDigitInfo(id,did);
        return this.successDelete();
    }

    @GET @Path("/{id}/digitinfos")
    @ApiOperation("查询活动纪实列表")
    public List<DigitInfo> getDigitalInfos(@PathParam("id") String id){
        return venueService.getDigitInfos(id);
    }

    @GET @Path("/{id}/digitinfos/{did}")
    @ApiOperation("查询活动纪实详情")
    public DigitInfo getOneDigitInfo(@PathParam("id") String id,@PathParam("did") String did){
        return venueService.getOneDigitInfo(id,did);
    }

    @PUT @Path("/{id}/digitinfos/{did}")
    @ApiOperation("编辑活动纪实")
    public Response modifyDigitInfo(@PathParam("id") String id,@PathParam("did") String did,DigitInfo digitInfo){
        venueService.editDigitalInfo(id,did,digitInfo);
        return this.successUpdate();
    }
}
