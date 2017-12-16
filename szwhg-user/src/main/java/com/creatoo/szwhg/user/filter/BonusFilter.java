package com.creatoo.szwhg.user.filter;

import com.creatoo.szwhg.core.rest.Bonus;
import com.creatoo.szwhg.user.model.User;
import com.creatoo.szwhg.user.service.UserActionService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Map;

/**
 * 积分处理过滤器
 * Created by yunyan on 2017/11/18.
 */
@Bonus
@Provider  //声明为面向切面组件
@Priority(Priorities.USER)  //声明过滤器的顺序
public class BonusFilter implements ContainerResponseFilter {
    @Autowired
    private UserActionService userActionService;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        int ret=responseContext.getStatus();
        if(ret!=200) return;
        String path=requestContext.getUriInfo().getPath();
        if(path.matches(".*/login/.*")) {
            User user = (User) responseContext.getEntity();
            userActionService.addBonus(user.getId(),  10);
        }else if(path.matches("user/users") && requestContext.getMethod().equals("POST")){
            String userid= (String)((Map)responseContext.getEntity()).get("id");
            userActionService.addBonus(userid,200);
        }
    }
}
