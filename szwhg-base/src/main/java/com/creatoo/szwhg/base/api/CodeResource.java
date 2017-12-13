package com.creatoo.szwhg.base.api;

import com.creatoo.szwhg.base.model.Code;
import com.creatoo.szwhg.base.model.CodeType;
import com.creatoo.szwhg.base.service.CodeService;
import com.creatoo.szwhg.core.model.DictsortType;
import com.creatoo.szwhg.core.rest.AbstractResource;
import com.creatoo.szwhg.core.rest.ClientErrorMsg;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by yunyan on 2017/8/9.
 */
@Component
@Path("/sys/codetypes")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "字典管理", produces = "application/json")
@ApiResponses(value = {@ApiResponse(code=400,message = "处理出错",response = ClientErrorMsg.class),@ApiResponse(code=200,message="成功")})
public class CodeResource extends AbstractResource {
    @Autowired
    private CodeService codeService;

    @GET @ApiOperation(value = "查询所有代码类型")
    public List<CodeType> getTypes(){
        return codeService.getTypes();
    }

    @GET
    @Path("/{typeCode}")
    @ApiOperation(value = "查询代码项")
    public List<Code> getCodes(@PathParam("typeCode") @ApiParam("类型编码") String typeCode){
        return codeService.getTypeCodes(typeCode);
    }

    @PUT
    @Path("/{typeCode}")
    @ApiOperation(value = "保存代码项",code=200)
    public Response saveCodes(@PathParam("typeCode") @ApiParam("类型编码") String typeCode,@ApiParam("代码项列表") List<Code> codes){
        codeService.saveCodes(typeCode,codes);
        return this.successUpdate();
    }

    @POST
    @ApiOperation(value = "保存字典",code=200)
    public Response saveCodeType(CodeType codeType){
        return this.successCreate(codeService.saveCodeType(codeType));
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "删除字典",code=200)
    public Response delCodeType(@PathParam("id") String id){
        codeService.deleteCodeType(id);
        return this.successUpdate();
    }

    @GET
    @Path("/{type}/list")
    @ApiOperation(value = "字典分类查询列表",code=200)
    public List<CodeType> findByType(@PathParam("type") DictsortType type){
        return codeService.findTypeList(type.name());
    }
}