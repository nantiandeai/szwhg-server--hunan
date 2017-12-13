package com.creatoo.szwhg.server.configure;

import com.creatoo.szwhg.core.exception.BsException;
import com.creatoo.szwhg.core.rest.ClientErrorMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by yunyan on 2017/3/17.
 */
public class DefaultExceptionMapperSupport implements ExceptionMapper<Exception> {
    private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionMapperSupport.class);


    @Override
    public Response toResponse(Exception exception) {

        if(exception instanceof WebApplicationException){
            int status=((WebApplicationException)exception).getResponse().getStatus();
            return Response.ok(new ClientErrorMsg(status,exception.getMessage()),
                    MediaType.APPLICATION_JSON).
                    status(status).build();
        }else if(exception instanceof ClientErrorException){
            int status=((ClientErrorException)exception).getResponse().getStatus();
            return Response.ok(new ClientErrorMsg(status,exception.getMessage()),
                    MediaType.APPLICATION_JSON).
                    status(status).build();
        }else if(exception instanceof BsException){
            return Response.ok(new ClientErrorMsg(((BsException)exception).getErrorCode(),exception.getMessage()),
                    MediaType.APPLICATION_JSON).
                    status(HttpServletResponse.SC_BAD_REQUEST).build();
        }else if(exception instanceof AuthenticationCredentialsNotFoundException){
            return Response.ok(new ClientErrorMsg(HttpServletResponse.SC_UNAUTHORIZED,exception.getMessage()),
                    MediaType.APPLICATION_JSON).
                    status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }else{
            logger.error("系统异常",exception);
            return Response.ok(new ClientErrorMsg(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,exception.getMessage()),
                    MediaType.APPLICATION_JSON).
                    status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).build();
        }
    }

}