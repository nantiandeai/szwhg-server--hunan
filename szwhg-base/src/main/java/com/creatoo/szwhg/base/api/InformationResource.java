package com.creatoo.szwhg.base.api;

import com.creatoo.szwhg.base.model.Column;
import com.creatoo.szwhg.base.model.Information;
import com.creatoo.szwhg.base.service.InformationService;
import com.creatoo.szwhg.core.exception.BsException;
import com.creatoo.szwhg.core.model.Comment;
import com.creatoo.szwhg.core.model.CommentStatus;
import com.creatoo.szwhg.core.rest.AbstractResource;
import com.creatoo.szwhg.core.rest.ClientErrorMsg;
import com.creatoo.szwhg.core.rest.Pagination;
import io.swagger.annotations.*;
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
 * 资讯资源服务
 * Created by yunyan on 2017/8/4.
 */
@Component
@Path("/sys/information")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "资讯管理", produces = "application/json")
@ApiResponses(value = {@ApiResponse(code=400,message = "处理出错",response = ClientErrorMsg.class),@ApiResponse(code=200,message="成功")})
public class InformationResource extends AbstractResource {
    @Autowired
    private InformationService informationService;

    @POST
    @ApiOperation(value = "创建资讯")
    public Response createInformation(@Valid @NotNull Information information){
      return this.successCreate(informationService.createInformation(information));
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "删除资讯",position = 3)
    public Response delete(@PathParam("id") String id){
        informationService.deleteInformation(id);
        return this.successDelete();
    }

    @PUT
    @Path("/{id}")
    @ApiOperation(value = "修改资讯信息",position = 2)
    public Response modify(@PathParam("id") String id, @Valid @NotNull Information information){
        informationService.modifyInformation(id,information);
        return this.successUpdate();
    }

    @GET
    @ApiOperation("查询资讯列表")
    public Page<Information> findAll(@QueryParam("search")@ApiParam("查询条件") String search, @Pagination @ApiParam("分页")  Pageable pageable){
        return informationService.findAll(search,pageable);
    }

    @GET
    @Path("/{id}")
    @ApiOperation("查询资讯详情")
    public Information getById(@PathParam("id") String id){
        return informationService.findById(id);
    }

    @PUT
    @Path("/{id}/top/true")
    @ApiOperation(value = "置顶资讯",position = 4)
    public Response recommend(@PathParam("id") String id){
        Information information = informationService.getOne(id);
        if (information == null) throw new BsException("资讯不存在");
        information.setIsRecommend(true);
        informationService.modifyInformation(id,information);
        return this.successUpdate();
    }

    @PUT
    @Path("/{id}/top/false")
    @ApiOperation(value = "取消置顶",position = 5)
    public Response unRecommend(@PathParam("id") String id){
        Information information = informationService.getOne(id);
        if (information == null) throw new BsException("资讯不存在");
        information.setIsRecommend(false);
        informationService.modifyInformation(id,information);
        return this.successUpdate();
    }

    @PUT
    @Path("/{id}/publish/true")
    @ApiOperation(value = "发布资讯",position = 6)
    public Response publish(@PathParam("id") String id){
        Information information = informationService.getOne(id);
        if (information == null) throw new BsException("资讯不存在");
        information.setIsPublish(true);
        informationService.modifyInformation(id,information);
        return this.successUpdate();
    }

    @PUT
    @Path("/{id}/publish/false")
    @ApiOperation(value = "取消发布",position = 7)
    public Response unPublish(@PathParam("id") String id){
        Information information = informationService.getOne(id);
        if (information == null) throw new BsException("资讯不存在");
        information.setIsPublish(false);
        informationService.modifyInformation(id,information);
        return this.successUpdate();
    }

    @GET
    @Path("/heritages")
    @ApiOperation("查询非遗资讯列表")
    public Page<Information> findHeritageInformation(@Pagination @ApiParam("分页")  Pageable pageable){
        return informationService.findAll(null,pageable);
    }

    @POST
    @Path("/{id}/comments")
    @ApiOperation("添加评论")
    public Response addComment(@PathParam("id")String trainid,Comment comment){
        String commentid=informationService.addComment(trainid, comment);
        return this.successCreate(commentid);
    }

    @DELETE
    @Path("/{id}/comments/{commentid}")
    @ApiOperation("删除评论")
    public Response deleteComment(@PathParam("id")String trainid,@PathParam("commentid") String commentid){
        informationService.deleteComment(trainid, commentid);
        return this.successDelete();
    }

    @GET
    @Path("/{id}/comments")
    @ApiOperation("获取评论列表")
    public Page<Comment> getComments(@PathParam("id")String trainid, @Pagination Pageable pageable){
        return informationService.findAllComments(trainid,pageable);
    }

    @PUT
    @Path("/{id}/comments/{commentid}/{commentStatus}")
    @ApiOperation("审核评论")
    public Response auditComment(@PathParam("id")String trainid, @PathParam("commentid") String commentid, @PathParam("commentStatus")CommentStatus commentStatus){
        informationService.auditComment(trainid, commentid, commentStatus);
        return this.successUpdate();
    }

    @GET
    @Path("/heritage/columns")
    @ApiOperation("查询非遗资讯栏目列表")
    public List<Column> getHeritageColumns(){
        return informationService.findColumns();
    }
}
