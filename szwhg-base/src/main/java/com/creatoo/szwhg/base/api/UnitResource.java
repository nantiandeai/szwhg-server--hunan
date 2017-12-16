package com.creatoo.szwhg.base.api;

import com.creatoo.szwhg.base.model.Unit;
import com.creatoo.szwhg.base.service.UnitService;
import com.creatoo.szwhg.core.exception.BsException;
import com.creatoo.szwhg.core.rest.AbstractResource;
import com.creatoo.szwhg.core.rest.ClientErrorMsg;
import com.creatoo.szwhg.core.rest.Pagination;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
 * 组织资源服务
 * Created by yunyan on 2017/8/4.
 */

@Component
@Path("/sys/units")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "组织管理", produces = "application/json")
@ApiResponses(value = {@ApiResponse(code=400,message = "处理出错",response = ClientErrorMsg.class),@ApiResponse(code=200,message="成功")})
public class UnitResource extends AbstractResource {
    @Autowired
    private UnitService unitService;


    @POST
    @Path("/{id}/children")
    @ApiOperation(value = "创建子组织")
    public Response createChild(@PathParam("id") String id,@Valid @NotNull  Unit unit){
        Unit parent=unitService.getOne(id);
        if(parent==null) throw new BsException("父组织不存在");
        unit.setParent(parent);
        return this.successCreate(unitService.createUnit(unit));
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "获取组织信息", notes = "如果是根组织，id=root",position = 1)
    public Unit getOne(@PathParam("id") String id){
        Unit unit=null;
        if(id.equals("root")) unit=unitService.getRootUnit();
        else unit=unitService.getOne(id);
        if(unit==null) throw new BsException("组织不存在");
        return unit;
    }

    @GET
    @ApiOperation(value = "查询组织机构列表")
    public Page<Unit> findAll(@QueryParam("search") String search,@Pagination Pageable pageable){
        return unitService.findAll(search,pageable);
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "删除组织",position = 3)
    public Response delete(@PathParam("id") String id){
        unitService.deleteUnit(id);
        return this.successDelete();
    }

    @PUT
    @Path("/{id}")
    @ApiOperation(value = "修改组织信息",position = 2)
    public Response modify(@PathParam("id") String id, Unit unit){
        unitService.modifyUnit(id,unit);
        return this.successUpdate();
    }

    @GET
    @Path("/{id}/children")
    @ApiOperation(value = "获取直接下级组织列表",position = 6)
    public List<Unit> getChildren(@PathParam("id") String parentId){
        return unitService.getChildren(parentId);
    }
    @GET
    @Path("/{id}/children/org")
    @ApiOperation(value = "获取直接下级机构列表",position = 7)
    public List<Unit> getChildrenOrg(@PathParam("id") String parentId){
        return unitService.getOrgChildren(parentId);
    }
    @GET
    @Path("/{id}/children/dep")
    @ApiOperation(value = "获取下级部门列表",position = 7)
    public List<Unit> getChildrenDep(@PathParam("id") String parentId){
        return unitService.getDepChildren(parentId);
    }

    @Path("{id}/roles")
    public RoleResource getRoleResource(@PathParam("id") String unitId){
        return new RoleResource(unitId);
    }

    @Path("{id}/managers")
    public ManagerResource getManagerResource(@PathParam("id") String unitId){
        return new ManagerResource(unitId);
    }
}
