package com.creatoo.szwhg.base.api;

import com.creatoo.szwhg.base.model.LoopContent;
import com.creatoo.szwhg.base.model.LoopPic;
import com.creatoo.szwhg.base.model.LoopType;
import com.creatoo.szwhg.base.service.LoopPicService;
import com.creatoo.szwhg.core.rest.AbstractResource;
import com.creatoo.szwhg.core.rest.ClientErrorMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by yunyan on 2017/8/9.
 */
@Component
@Path("/sys/looppics")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "轮播图管理", produces = "application/json")
@ApiResponses(value = {@ApiResponse(code=400,message = "处理出错",response = ClientErrorMsg.class),@ApiResponse(code=200,message="成功")})
public class LoopPicResource extends AbstractResource {
    @Autowired
    private LoopPicService picService;

    @POST
    @ApiOperation(value = "保存轮播图配置",code=200)
    public Response saveLooppic(LoopPic loopPic){
        return this.successCreate(picService.saveLoopPic(loopPic));
    }

    @GET @ApiOperation(value = "获取轮播图配置列表")
    public List<LoopPic> getLoopPics(){
        return picService.findAll();
    }

    @GET
    @Path("/{type}/contents")
    @ApiOperation(value = "获取轮播图内容列表")
    public List<LoopContent> getLoopContents(@PathParam("type") LoopType type){
        return picService.getContents(type);
    }

    @POST
    @Path("/{type}/contents")
    @ApiOperation(value = "保存轮播图内容",code=200)
    public Response saveLoopContent(@PathParam("type") LoopType type, LoopContent loopContent){
        return this.successCreate(picService.saveContent(type,loopContent));
    }

    @GET
    @Path("/{type}/contents/{cid}")
    @ApiOperation(value = "获取轮播图内容详情",code=200)
    public LoopContent getLoopContentDetail(@PathParam("type") LoopType type, @PathParam("cid") String cid){
        return picService.getLoopContent(type,cid);
    }

    @PUT
    @Path("/{type}/contents/{cid}")
    @ApiOperation(value = "编辑轮播图内容")
    public Response modifyLoopContents(@PathParam("type") LoopType type,@PathParam("cid") String cid,LoopContent loopContent){
        picService.modifyContent(type,cid,loopContent);
        return this.successUpdate();
    }

    @DELETE
    @Path("/{type}/contents/{cid}")
    @ApiOperation(value = "删除轮播图内容")
    public Response deleteLoopContents(@PathParam("type") LoopType type,@PathParam("cid") String cid){
        picService.deleteContent(type,cid);
        return this.successDelete();
    }

//    @PUT
//    @Path("/{id}/contents")
//    @ApiOperation(value = "编辑轮播图内容")
//    public Response editLoopContents(@PathParam("id") String id,List<LoopContent> loopContents){
//        picService.editContents(id,loopContents);
//        return this.successUpdate();
//    }

    @PUT
    @Path("/{type}/contents/{cid}/true")
    @ApiOperation(value = "启用轮播图内容")
    public Response modifyLoopContentTrue(@PathParam("type") LoopType type,@PathParam("cid") String cid){
        LoopContent loopContent = picService.getLoopContent(type,cid);
        loopContent.setEnable(true);
        picService.modifyContent(type,cid,loopContent);
        return this.successUpdate();
    }

    @PUT
    @Path("/{type}/contents/{cid}/false")
    @ApiOperation(value = "停用轮播图内容")
    public Response modifyLoopContentFalse(@PathParam("type") LoopType type,@PathParam("cid") String cid){
        LoopContent loopContent = picService.getLoopContent(type,cid);
        loopContent.setEnable(false);
        picService.modifyContent(type,cid,loopContent);
        return this.successUpdate();
    }
}