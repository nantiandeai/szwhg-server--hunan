package com.creatoo.szwhg.base.filter;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import java.io.IOException;

/**
 * Created by yunyan on 2017/10/16.
 */
public class RequestFilter implements ContainerRequestFilter {
    @Context
    HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String creatorId = requestContext.getHeaderString("creatorId");
        if (StringUtils.isNotEmpty(creatorId)) {
           AuditAware.setUserId(creatorId);
        }
    }
}
