package com.creatoo.szwhg.user.api;

import com.creatoo.szwhg.core.rest.AbstractResource;
import com.creatoo.szwhg.core.rest.Pagination;
import com.creatoo.szwhg.user.model.IdentifyApply;
import com.creatoo.szwhg.user.service.UserActionService;
import com.creatoo.szwhg.user.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by yunyan on 2017/9/29.
 */
@Component
@Path("/user/identifys")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "会员认证", produces = "application/json")
public class IdentifyApplyResource extends AbstractResource{
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserActionService userActionService;

    @POST
    @ApiOperation("申请认证")
    public Response createApply(@NotNull @Valid IdentifyApply apply){
        String applyId= userActionService.createIdentifyApply(apply);
        return this.successCreate(applyId);
    }

    @GET
    @ApiOperation("查询认证列表")
    public Page<IdentifyApply> findAll(@QueryParam("search") @ApiParam("查询条件") String search, @Pagination Pageable pageable){
        return userActionService.findApplys(search, pageable);
    }

    @PUT @Path("/{id}/pass")
    @ApiOperation("通过认证")
    public Response passApply(@PathParam("id")String id){
        userActionService.passIdentifyApply(id);
        return this.successUpdate();
    }

    @PUT @Path("/{id}/reject")
    @ApiOperation("拒绝认证")
    public Response rejectApply(@PathParam("id")String id,String comment){
        userActionService.rejectIdentifyApply(id,comment);
        return this.successUpdate();
    }
}
