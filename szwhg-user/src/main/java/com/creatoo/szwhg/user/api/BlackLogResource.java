package com.creatoo.szwhg.user.api;

import com.creatoo.szwhg.core.rest.AbstractResource;
import com.creatoo.szwhg.core.rest.Pagination;
import com.creatoo.szwhg.user.model.BlackLog;
import com.creatoo.szwhg.user.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Created by yunyan on 2017/12/5.
 */
@Component
@Path("/user/blacklogs")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "黑名单日志管理", produces = "application/json")
public class BlackLogResource extends AbstractResource{
    @Autowired
    private UserInfoService userInfoService;

    @GET @ApiOperation("查询所有黑名单日志")
    public Page<BlackLog> getAll(@QueryParam("search") @ApiParam("查询条件") String search, @Pagination Pageable pageable){
        return userInfoService.findAllBlackLog(search, pageable);
    }
}
