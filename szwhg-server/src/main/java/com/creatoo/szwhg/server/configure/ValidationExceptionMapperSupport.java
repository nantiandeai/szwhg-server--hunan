package com.creatoo.szwhg.server.configure;

import com.creatoo.szwhg.core.rest.ClientErrorMsg;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * 处理校验异常
 * Created by yunyan on 2017/7/23.
 */
public class ValidationExceptionMapperSupport implements ExceptionMapper<ValidationException> {

    @Override
    public Response toResponse(ValidationException e) {
        final StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> cv : ((ConstraintViolationException) e)
                .getConstraintViolations()) {
            strBuilder.append(cv.getPropertyPath().toString()+":"+cv.getMessage()).append(",");
        }
        return Response.ok(new ClientErrorMsg(HttpServletResponse.SC_BAD_REQUEST,strBuilder.toString()),
                MediaType.APPLICATION_JSON).
                status(HttpServletResponse.SC_BAD_REQUEST).build();

    }
}
