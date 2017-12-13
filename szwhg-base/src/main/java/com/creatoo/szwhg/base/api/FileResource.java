package com.creatoo.szwhg.base.api;

import com.creatoo.szwhg.base.service.FileService;
import com.creatoo.szwhg.core.model.FileType;
import com.creatoo.szwhg.core.rest.AbstractResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yunyan on 2017/9/2.
 */
@Path("/upload")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "文件上传管理", produces = "application/json")
public class FileResource extends AbstractResource {
    @Autowired
    private FileService fileService;


//    @GET @Path("/{id}")
//    @ApiOperation(value = "获取文件",produces = MediaType.APPLICATION_OCTET_STREAM)
//    public Response getFile(@PathParam("type") @ApiParam("文件类型") FileType type,@PathParam("id") String id){
//        InputStream input=null;
//        input=fileService.getFileStream(id);
//        if(input==null) return null;
//        Response.ResponseBuilder rb = Response.ok(input, MediaType.APPLICATION_OCTET_STREAM);
//        rb.header("Content-Disposition", "attachment;filename=" + "file");
//        return rb.build();
//    }

    @POST @Path("/{type}")  @Consumes(MediaType.MULTIPART_FORM_DATA)
    @ApiOperation(value = "上传文件")
    public Response uploaFile(@PathParam("type") @ApiParam("文件类型") FileType type,
                              @FormDataParam("filename") String filename,
                              @FormDataParam("file")InputStream stream){
        String url=fileService.saveFile(type,filename,stream);
        Map map=new HashMap<String,Object>();
        map.put("url",url);
        return this.success(map);
    }
}
