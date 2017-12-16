package com.creatoo.szwhg.user.api;

import com.creatoo.szwhg.base.service.MessageService;
import com.creatoo.szwhg.core.rest.AbstractResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yunyan on 2017/11/13.
 */
@Component
@Path("/user/vcodes")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "会员验证码服务", produces = "application/json")
public class UserVerifyCodeResource extends AbstractResource{
    @Autowired
    private MessageService msgService;

    @POST @ApiOperation("发送验证码")
    public Response createCode(@QueryParam("mobile")String mobile){
        msgService.sendVerifyCodeSms(mobile);
        return this.successCreate();
    }

    @GET @Path("/verify")
    @ApiOperation("校验验证码")
    public Map<String, Boolean> validateCode(@QueryParam("mobile")String mobile,@QueryParam("code")String code){
        boolean success=msgService.isValidVerifyCode(code,mobile);
        Map<String,Boolean> result=new HashMap<>();
        result.put("success",success);
        return result;
    }
}
