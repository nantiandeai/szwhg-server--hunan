package com.creatoo.szwhg.base.api;

import com.creatoo.szwhg.base.model.DeviceClient;
import com.creatoo.szwhg.base.service.DeviceService;
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
 * Created by yunyan on 2017/11/29.
 */
@Component
@Path("/sys/devices")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "设备管理", produces = "application/json")
public class DeviceResource extends AbstractResource {
    @Autowired
    private DeviceService deviceService;

    @GET @ApiOperation("查询所有设备")
    public List<DeviceClient> getAll(){
        return deviceService.getDevices();
    }

    @POST @ApiOperation("查询所有设备")
    public Response create(DeviceClient device){
        return this.successCreate(deviceService.createDevice(device));
    }
    @GET @Path("/{id}")
    @ApiOperation("查看设备详情")
    public DeviceClient get(@PathParam("id")String id){
        return deviceService.getDevice(id);
    }
    @PUT @Path("/{id}")
    @ApiOperation("编辑设备")
    public Response edit(@PathParam("id")String id,DeviceClient device){
        deviceService.editDevice(id,device);
        return this.successUpdate();
    }
    @DELETE @Path("/{id}")
    @ApiOperation("删除设备")
    public Response delete(@PathParam("id")String id){
        deviceService.deleteDevice(id);
        return this.successDelete();
    }
}
