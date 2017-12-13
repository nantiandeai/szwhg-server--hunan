package com.creatoo.szwhg.base.api;

import com.creatoo.szwhg.base.model.Column;
import com.creatoo.szwhg.base.service.ColumnService;
import com.creatoo.szwhg.core.exception.BsException;
import com.creatoo.szwhg.core.rest.AbstractResource;
import com.creatoo.szwhg.core.rest.ClientErrorMsg;
import com.creatoo.szwhg.core.rest.Pagination;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * 栏目资源服务
 * Created by wuxiangliang on 2017/10/11.
 */

@Component
@Path("/sys/columns")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "栏目管理", produces = "application/json")
@ApiResponses(value = {@ApiResponse(code=400,message = "处理出错",response = ClientErrorMsg.class),@ApiResponse(code=200,message="成功")})
public class ColumnResource extends AbstractResource {

     @Autowired
    private ColumnService columnService;

    @POST
    @ApiOperation(value = "创建栏目")
    public Response createChild(@Valid @NotNull Column column){
       return this.successCreate(columnService.createColumn(column));
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "获取栏目信息",position = 1)
    public Column getOne(@PathParam("id") String id){
        Column column = columnService.getOne(id);
        if (column == null) throw new BsException("栏目不存在");
      return column;
    }

//    @GET
//    @Path("/{id}/children")
//    @ApiOperation(value = "获取直接下级栏目列表",position = 6)
//    public List<Column> getChildren(@PathParam("id") String parentId){
//        return columnService.getChildren(parentId);
//    }

    @GET
    @Path("/tops")
    @ApiOperation(value = "获取上级栏目列表",position = 7)
    public List<Column> getParent(){
        return columnService.getRootUnit();
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "删除栏目",position = 3)
    public Response delete(@PathParam("id") String id){
        columnService.deleteColumn(id);
        return this.successDelete();
    }

    @PUT
    @Path("/{id}")
    @ApiOperation(value = "修改栏目信息",position = 2)
    public Response modify(@PathParam("id") String id, Column column){
        columnService.modifyColumn(id,column);
        return this.successUpdate();
    }

    @PUT
    @Path("/{id}/enable/true")
    @ApiOperation(value = "启用栏目",position = 4)
    public Response enable(@PathParam("id") String id){
        Column column = columnService.getOne(id);
        if (column == null) throw new BsException("栏目不存在");
        column.setEnable(true);
        columnService.modifyColumn(id,column);
        return this.successUpdate();
    }

    @PUT
    @Path("/{id}/enable/false")
    @ApiOperation(value = "关闭栏目",position = 4)
    public Response disenable(@PathParam("id") String id){
        Column column = columnService.getOne(id);
        if (column == null) throw new BsException("栏目不存在");
        column.setEnable(false);
        columnService.modifyColumn(id,column);
        return this.successUpdate();
    }

    @GET
    @ApiOperation("查询栏目列表")
    public Page<Column> findAll(@QueryParam("search")@ApiParam("查询条件") String search, @Pagination @ApiParam("分页") Pageable pageable){
        return columnService.findAll(search,pageable);
    }
}
