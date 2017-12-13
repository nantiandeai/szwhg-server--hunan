package com.creatoo.szwhg.base.api;

import com.creatoo.szwhg.base.model.SysParam;
import com.creatoo.szwhg.base.service.SysParamService;
import com.creatoo.szwhg.core.rest.AbstractResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by yunyan on 2017/10/31.
 */
@Component
@Path("/sys/params")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "系统参数管理", produces = "application/json")
public class SysParamResource  extends AbstractResource {
    @Autowired
    private SysParamService paramService;

    @GET @ApiOperation("查询所有系统参数")
    public List<SysParam> getAllParams(){
        return paramService.getAllParams();
    }
    @PUT @Path("/{paramName}/{paramValue}")
    @ApiOperation("修改系统参数")
    public Response modifyParam(@PathParam("paramName")String paramName,@PathParam("paramValue")String paramValue){
        paramService.updateSysParam(paramName,paramValue);
        return this.successUpdate();
    }
}
