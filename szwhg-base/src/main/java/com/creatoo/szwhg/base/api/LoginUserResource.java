package com.creatoo.szwhg.base.api;

import com.creatoo.szwhg.base.model.Manager;
import com.creatoo.szwhg.base.service.ManagerService;
import com.creatoo.szwhg.core.rest.AbstractResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by yunyan on 2017/9/5.
 */
@Path("/sys/loginuser")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "查询登录用户信息", produces = "application/json")
public class LoginUserResource extends AbstractResource {
    @Autowired
    private ManagerService managerService;

    @GET @ApiOperation("查询登录用户信息")
    public Object getInfo(@Context SecurityContext securityContext){
        String username=securityContext.getUserPrincipal().getName();
        Manager manager=managerService.getManagerByUsername(username);
        return manager;
    }
}
