package com.creatoo.szwhg.base.api;

import com.creatoo.szwhg.core.rest.AbstractResource;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Administrator
 */
@Component
@Path("/ueditor")
public class UeditorResource extends AbstractResource {

    @Path("/index")
    @GET
    public Response showUeditor(@Context HttpServletRequest req, @Context HttpServletResponse res) throws IOException {
        String rootPath = req.getRealPath("/");
        res.setHeader("X-Frame-Options", "SAMEORIGIN");
        String actionEnter = new ActionEnter(null,null,req, rootPath).exec();
        return Response.ok(actionEnter).build();
    }

    @Path("/index")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response postUeditor(@FormDataParam("upfile")InputStream stream, @FormDataParam("filename") String filename, @Context HttpServletRequest req, @Context HttpServletResponse res) throws IOException {
        String rootPath = req.getRealPath("/") ;
        res.setHeader("X-Frame-Options", "SAMEORIGIN");
        String actionEnter = new ActionEnter(stream,filename,req,rootPath).exec() ;
        return Response.ok(actionEnter).build();
    }

}
