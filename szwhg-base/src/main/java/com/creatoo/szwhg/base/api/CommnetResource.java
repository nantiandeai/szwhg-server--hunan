package com.creatoo.szwhg.base.api;

import com.creatoo.szwhg.base.model.Comment;
import com.creatoo.szwhg.base.model.CommentStatus;
import com.creatoo.szwhg.base.model.Information;
import com.creatoo.szwhg.base.service.CommentService;
import com.creatoo.szwhg.core.rest.AbstractResource;
import com.creatoo.szwhg.core.rest.Pagination;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Path("/base/comment")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "评论管理", produces = "application/json")
public class CommnetResource extends AbstractResource{
    @Autowired
    private CommentService commentService;

    @GET
    @ApiOperation("查询评论列表")
    public Page<Comment> findAll(@QueryParam("search")@ApiParam("查询条件") String search, @Pagination @ApiParam("分页") Pageable pageable){
        return commentService.findAll(search,pageable);
    }

    @PUT @Path("/{id}/audit/{status}")
    @ApiOperation("审核评论")
    public Response auditComment(@PathParam("id")String id, @PathParam("status")CommentStatus status){
        commentService.auditComment(id,status);
        return this.successUpdate();
    }
}
