package com.creatoo.szwhg.base.api;

import com.creatoo.szwhg.base.model.Manager;
import com.creatoo.szwhg.base.model.Menu;
import com.creatoo.szwhg.base.model.Unit;
import com.creatoo.szwhg.base.service.ManagerService;
import com.creatoo.szwhg.core.exception.BsException;
import com.creatoo.szwhg.core.rest.AbstractResource;
import com.creatoo.szwhg.core.rest.ClientErrorMsg;
import com.creatoo.szwhg.core.rest.Pagination;
import com.creatoo.szwhg.core.util.SpringContextUtil;
import io.swagger.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 * Created by yunyan on 2017/8/8.
 */
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "管理员管理", produces = "application/json")
@ApiResponses(value = {@ApiResponse(code=400,message = "处理出错",response = ClientErrorMsg.class),@ApiResponse(code=200,message="成功")})
public class ManagerResource extends AbstractResource {
    private String unitId;
    private ManagerService managerService;

    public ManagerResource(String _unitid){
        this.unitId=_unitid;
        this.managerService= SpringContextUtil.getBean(ManagerService.class);
    }

    @GET @ApiOperation(value = "查询管理员列表")
    public Page<Manager> findAll(@QueryParam("search")@ApiParam("查询条件") String search, @Pagination @ApiParam("分页")  Pageable pageable){
        return managerService.findAll(unitId,search,pageable);
    }

    @POST
    @ApiOperation(value = "创建管理员账号")
    public Response create(@Valid @NotNull Manager manager){
        Unit unit=new Unit();
        unit.setId(this.unitId);
        manager.setUnit(unit);
        return this.successCreate(managerService.createManager(manager));
    }
    @PUT @ApiOperation(value = "修改管理员账号")
    @Path("/{username}")
    public Response update(@PathParam("username") String username,Manager manager){
        managerService.modifyManager(username,manager);
        return this.successUpdate();
    }

    @GET @ApiOperation(value = "查询管理员详情")
    @Path("/{username}")
    public Manager get(@PathParam("username")String username){
        return managerService.getManagerByUsername(username);
    }

    @DELETE @Path("/{username}") @ApiOperation("删除管理员")
    public Response delete(@PathParam("username")String username){
        managerService.deleteManager(username);
        return this.successDelete();
    }
    @PUT @ApiOperation(value = "锁定管理员账号")
    @Path("/{username}/lock")
    public Response lock(@PathParam("username") String username){
        Manager mag=managerService.getManagerByUsername(username);
        if(mag==null) throw new BsException("不存在该账户");
        mag.setIsLocked(true);
        managerService.modifyManager(username,mag);
        return this.successUpdate();
    }

    @PUT @ApiOperation(value = "解锁管理员账号")
    @Path("/{username}/unlock")
    public Response unlock(@PathParam("username") String username){
        Manager mag=managerService.getManagerByUsername(username);
        if(mag==null) throw new BsException("不存在该账户");
        mag.setIsLocked(false);
        managerService.modifyManager(username,mag);
        return this.successUpdate();
    }
    @PUT @ApiOperation(value = "修改管理员角色")
    @Path("/{username}/roles")
    public Response changeRoles(@PathParam("username")String username,List<String> roleIds){
        managerService.modifyRoles(username,roleIds);
        return this.successUpdate();
    }

    @GET @Path("/{username}/menus")
    @ApiOperation(value = "查询管理员菜单")
    public List<Menu> getMenuCodes(@PathParam("username")String username){
        return managerService.getManagerMenuCodes(username);
    }

    @PUT
    @Path("/{username}/pwd")
    @ApiOperation(value = "管理员更改密码",notes = "newPwd- 新密码")
    public Response changePwdWithOld(@PathParam("username") String username,Map<String,String> data){
//        String oldpwd=data.get("oldPwd");
        String newPwd=data.get("newPwd");
        managerService.changePwdWithOld(username,newPwd);
        return this.successUpdate();
    }
}
