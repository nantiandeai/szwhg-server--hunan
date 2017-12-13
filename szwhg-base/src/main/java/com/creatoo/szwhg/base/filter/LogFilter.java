package com.creatoo.szwhg.base.filter;

import com.creatoo.szwhg.base.model.Manager;
import com.creatoo.szwhg.base.model.OperLog;
import com.creatoo.szwhg.base.service.ManagerService;
import com.creatoo.szwhg.base.service.OperLogService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 日志处理过滤器
 * Created by yunyan on 2017/11/30.
 */
@Provider  //声明为面向切面组件
@Priority(Priorities.USER)  //声明过滤器的顺序
public class LogFilter implements ContainerRequestFilter, ContainerResponseFilter {
    @Autowired
    private OperLogService logService;
    @Autowired
    private ManagerService managerService;

    private static ThreadLocal<String> data = new ThreadLocal<>();

    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        int status=responseContext.getStatus();
        if(status==200){
            UriInfo uri=requestContext.getUriInfo();
            String path=uri.getPath();
            SecurityContext securityContext=requestContext.getSecurityContext();
            if(securityContext.getUserPrincipal()==null) return;
            String method=requestContext.getMethod();
            if(method.equals("GET")) return; //查询操作不记录
            String loginId=securityContext.getUserPrincipal().getName();
            Manager manager=managerService.getManagerByUsername(loginId);
            if(manager==null) return;
            OperLog log=new OperLog();
            log.setOperTime(LocalDateTime.now());
            log.setUsername(loginId);
            log.setCname(manager.getName());
            log.setUnitId(manager.getUnit().getId());
            log.setUnitName(manager.getUnit().getName());
            String operType="新增";
            if(method.equals("PUT")) operType="修改";
            log.setOperType(operType);
            log.setOperObj(path);
           // String data= IOUtils.toString(requestContext.getEntityStream(),"utf-8");
            log.setRemark(data.get());
            if(path.matches("sys/\\w+$")){
                log.setModule("系统管理");
            }else if(path.matches("activity/\\w+$")){
                log.setModule("活动预定");
            }else if(path.matches("train/\\w+$")){
                log.setModule("培训报名");
            }else if(path.matches("venue/\\w+$")){
                log.setModule("场馆预定");
            }
            logService.createLog(log);
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        UriInfo uri=requestContext.getUriInfo();
        String path=uri.getPath();
         if (requestContext.hasEntity() && !path.matches("upload/\\w+$") && !path.matches("ueditor/\\w+$")){
                 String data1 = IOUtils.toString(requestContext.getEntityStream(), "utf-8");
                 data.set(data1);
                 requestContext.setEntityStream(new ByteArrayInputStream(data1.getBytes("UTF-8")));
         }
    }
}
