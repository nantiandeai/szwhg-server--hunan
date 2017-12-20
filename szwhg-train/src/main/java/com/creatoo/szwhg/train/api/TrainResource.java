package com.creatoo.szwhg.train.api;

import com.creatoo.szwhg.base.model.Comment;
import com.creatoo.szwhg.base.model.CommentStatus;
import com.creatoo.szwhg.base.service.CommentService;
import com.creatoo.szwhg.core.model.FlowLog;
import com.creatoo.szwhg.core.rest.AbstractResource;
import com.creatoo.szwhg.core.rest.Pagination;
import com.creatoo.szwhg.train.model.Train;
import com.creatoo.szwhg.train.service.TrainService;
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
 * 培训管理
 * Created by yunyan on 2017/9/3.
 */
@Component
@Path("/train/trains")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "培训管理", produces = "application/json")
public class TrainResource extends AbstractResource {
    /**
     * 培训服务
     */
    @Autowired
    private TrainService trainService;
    @Autowired
    private CommentService commentService ;


    @GET
    @ApiOperation("查询培训列表")
    public Page<Train> findAll(@QueryParam("search") String search, @Pagination Pageable pageable){
        return this.trainService.findAll(search, pageable);
    }

    @GET
    @Path("/{id}")
    @ApiOperation("查询培训详情")
    public Train findAct(@PathParam("id") String id){
        return this.trainService.findById(id);
    }

//    @GET
//    @Path("/{id}/itms")
//    @ApiOperation("查询培训课程信息")
//    public List<TrainItm> getItms(@PathParam("id") String id){
//        Train train=this.trainService.findById(id);
//        return train.getItms();
//    }

    @POST
    @ApiOperation("添加培训")
    public Response addTrain(Train train){
        String id = this.trainService.add(train);
        return this.successCreate(id);
    }

    @PUT
    @Path("/{id}")
    @ApiOperation("编辑培训")
    public Response editTrain(@PathParam("id")String id, Train train){
        this.trainService.edit(id, train);
        return this.successUpdate();
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation("删除培训")
    public Response deleteTrain(@PathParam("id")String id){
        this.trainService.delete(id);
        return this.successUpdate();
    }

    @PUT
    @Path("/{id}/onlineStatus")
    @ApiOperation("变更培训在线状态")
    public Response changeStatus(@PathParam("id")String id, FlowLog flowLog){
        this.trainService.changeOnlineStatus(id, flowLog);
        return this.successUpdate();
    }

    @PUT
    @Path("/{id}/top/true")
    @ApiOperation("置顶培训")
    public Response recommendTrain(@PathParam("id")String id){
        this.trainService.doRecommend(id, true);
        return this.successUpdate();
    }

    @PUT
    @Path("/{id}/top/false")
    @ApiOperation("取消培训置顶")
    public Response unRecommendTrain(@PathParam("id")String id){
        this.trainService.doRecommend(id, false);
        return this.successUpdate();
    }
    @POST
    @Path("/{id}/comments")
    @ApiOperation("添加评论")
    public Response addComment(@PathParam("id")String trainid,Comment comment){
        comment.setObjId(trainid);
        String commentid=commentService.addComment(comment);
        return this.successCreate(commentid);
    }

    @DELETE
    @Path("/{id}/comments/{commentid}")
    @ApiOperation("删除评论")
    public Response deleteComment(@PathParam("id")String trainid,@PathParam("commentid") String commentid){
        commentService.deleteComment(trainid);
        return this.successDelete();
    }

    @GET
    @Path("/{id}/comments")
    @ApiOperation("获取评论列表")
    public Page<Comment> getComments(@PathParam("id")String trainid,@Pagination Pageable pageable){
        String serach = "objId:"+trainid+",status:"+CommentStatus.Pass;
        return commentService.findAll(serach,pageable);
    }

}
