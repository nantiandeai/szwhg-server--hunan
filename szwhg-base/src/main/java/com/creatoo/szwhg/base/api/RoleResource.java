package com.creatoo.szwhg.base.api;

import com.creatoo.szwhg.base.model.Manager;
import com.creatoo.szwhg.base.model.Role;
import com.creatoo.szwhg.base.model.Unit;
import com.creatoo.szwhg.base.service.RoleService;
import com.creatoo.szwhg.core.rest.AbstractResource;
import com.creatoo.szwhg.core.rest.ClientErrorMsg;
import com.creatoo.szwhg.core.util.SpringContextUtil;
import io.swagger.annotations.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by yunyan on 2017/8/4.
 */
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "角色管理", produces = "application/json")
@ApiResponses(value = {@ApiResponse(code=400,message = "处理出错",response = ClientErrorMsg.class),@ApiResponse(code=200,message="成功")})
public class RoleResource extends AbstractResource {
    private String unitId;
    private RoleService roleService;

    public RoleResource(String _unitId){
        this.unitId=_unitId;
        this.roleService= SpringContextUtil.getBean(RoleService.class);
    }

    @POST
    @ApiOperation(value = "创建角色")
    public Response createRole(@Valid @NotNull Role role){
        Unit unit=new Unit();
        unit.setId(unitId);
        role.setUnit(unit);
        String id=roleService.createRole(role);
        return this.successCreate(id);
    }

    @GET
    @ApiOperation(value = "查询所有角色")
    public List<Role> getAll(){
        return roleService.getRolesByUnit(unitId);
    }
    @GET @Path("/{roleid}")
    @ApiOperation(value = "获取角色详情")
    public Role getRole(@PathParam("roleid") String roleid){
        return roleService.getRole(roleid);
    }
    @PUT  @Path("/{roleid}")
    @ApiOperation(value = "修改角色基本信息")
    public Response modifyRole(@PathParam("roleid")String roleid,Role role){
        roleService.editRole(roleid,role);
        return this.successUpdate();
    }
    @DELETE @Path("/{roleid}")
    @ApiOperation(value = "删除角色")
    public Response deleteRole(@PathParam("roleid") String roleid){
        roleService.deleteRole(roleid);
        return this.successDelete();
    }
    @GET @Path("/{roleid}/managers")
    @ApiOperation(value = "查询角色的管理员列表")
    public List<Manager> getRoleManagers(@PathParam("roleid")String roleid){
        return roleService.getManagers(roleid);
    }
    @PUT @Path("/{roleid}/managers")
    @ApiOperation(value = "重新设置角色的管理员")
    public Response resetRoleManager(@PathParam("roleid")String roleid,String[] managerIds){
        roleService.resetManagers(roleid,managerIds);
        return this.successUpdate();
    }

    @POST @Path("/{roleid}/managers/{managerId}")
    @ApiOperation(value = "增加角色的管理员")
    public Response addRoleManager(@PathParam("roleid")String roleid,@PathParam("managerId") String managerId){
        roleService.addManager(roleid,managerId);
        return this.successUpdate();
    }
    @DELETE @Path("/{roleid}/managers/{managerId}")
    @ApiOperation(value = "删除角色的管理员")
    public Response deleteRoleManager(@PathParam("roleid")String roleid,@PathParam("managerId") String managerId){
        roleService.deleteManager(roleid,managerId);
        return this.successDelete();
    }

    @PUT  @Path("/{roleid}/grants")
    @ApiOperation(value = "修改角色菜单权限")
    public Response modifyGrants(@PathParam("roleid")@ApiParam("角色id") String roleid,@ApiParam("菜单编码") List<String> menuCodes){
        roleService.grantMenus(roleid,menuCodes);
        return this.successUpdate();
    }
    @GET  @Path("/{roleid}/grants")
    @ApiOperation(value = "查询角色菜单权限")
    public List<String> getGrants(@PathParam("roleid")@ApiParam("角色id") String roleid){
        return roleService.getMenus(roleid);
    }

}
