package com.creatoo.szwhg.base.api;

import com.creatoo.szwhg.base.model.Region;
import com.creatoo.szwhg.base.service.RegionService;
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
@Path("/sys/regions")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "区域管理", produces = "application/json")
@ApiResponses(value = {@ApiResponse(code=400,message = "处理出错",response = ClientErrorMsg.class),@ApiResponse(code=200,message="成功")})
public class RegionResource extends AbstractResource {
    @Autowired
    private RegionService regionService;

    @GET @ApiOperation(value = "查询所有下级区域")
    @Path("/{code}/allchilds")
    public List<Region> getAllRegions(@PathParam("code") String code){
        return regionService.getAllChilds(code);
    }

    @GET @ApiOperation(value = "查询直接下级区域",notes = "code=root 查询省级区域")
    @Path("/{code}/directchilds")
    public List<Region> getDirectRegions(@PathParam("code") String code){
        return regionService.getDirecChilds(code);
    }
    @GET @ApiOperation(value = "查询区域详情")
    @Path("/{code}")
    public Region getRegion(@PathParam("code") String code){
        return regionService.getRegion(code);
    }

    @POST
    @ApiOperation(value = "创建区域")
    public Response createRegions(Region region){
        return this.successCreate(regionService.createRegion(region));
    }
}