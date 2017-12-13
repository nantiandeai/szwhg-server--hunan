package com.creatoo.szwhg.base.api;

import com.creatoo.szwhg.base.model.Menu;
import com.creatoo.szwhg.base.service.MenuService;
import com.creatoo.szwhg.core.rest.AbstractResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by yunyan on 2017/8/25.
 */
@Component
@Path("/sys/menus")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "菜单管理", produces = "application/json")
public class MenuResource extends AbstractResource {
    @Autowired
    private MenuService menuService;

    @POST @ApiOperation("创建菜单")
    public Response create(Menu menu){
        String id=menuService.createMenu(menu);
        return this.successCreate(id);
    }
    @GET @ApiOperation("获取所有菜单")
    public List<Menu> getAll(){
        return menuService.getAllMenus();
    }
    @GET @Path("/{id}")
    @ApiOperation("获取单个菜单")
    public Menu getMenu(@PathParam("id") String id){
        return menuService.getMenu(id);
    }
    @PUT @Path("{id}")
    @ApiOperation("修改菜单")
    public Response modify(@PathParam("id")String id,Menu menu){
        menuService.modifyMenu(id,menu);
        return this.successUpdate();
    }
    @DELETE @Path("{id}")
    @ApiOperation("删除菜单")
    public Response delete(@PathParam("id")String id){
        menuService.deleteMenu(id);
        return this.successDelete();
    }
    @GET @Path("/topmenus/tree")
    @ApiOperation("获取顶级菜单树")
    public  List<Menu> getTopMenuTree(){
        return menuService.getTopMenuTree();
    }
}
