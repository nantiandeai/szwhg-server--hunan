package com.creatoo.szwhg.base.api;

import com.creatoo.szwhg.base.model.CultureBrand;
import com.creatoo.szwhg.base.service.CultureBrandService;
import com.creatoo.szwhg.core.exception.BsException;
import com.creatoo.szwhg.core.rest.AbstractResource;
import com.creatoo.szwhg.core.rest.Pagination;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by lzh on 2017/11/15.
 */
@Component
@Path("/brand/brands")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "文化品牌", produces = "application/json")
public class CultureBrandResource extends AbstractResource {

    @Autowired
    private CultureBrandService brandService;

    @GET
    @ApiOperation("查询品牌列表")
    public Page<CultureBrand> findAllBrand(@QueryParam("search") @ApiParam("查询条件") String search, @Pagination @ApiParam("分页") Pageable pageable){
        return brandService.findAll(search,pageable);
    }

    @POST  @ApiOperation("创建文化品牌")
    public Response createBrand(@Valid @NotNull CultureBrand brand){
        return this.successCreate(brandService.createBrand(brand));
    }

    @PUT @Path("/{id}")
    @ApiOperation("修改文化品牌")
    public Response modifyBrand(@PathParam("id")String id,CultureBrand brand ){
        this.brandService.modifyBrand(id,brand);
        return successUpdate();
    }

    @PUT @Path("/{id}/publish/true")
    @ApiOperation("上架品牌")
    public Response publishBrand(@PathParam("id")String id){
        CultureBrand brand = this.brandService.getOne(id);
        if(brand == null){
            throw new BsException("品牌不存在");
        }
        brand.setIsPublish(true);
        this.brandService.modifyBrand(id,brand);
        return successUpdate();
    }

    @PUT @Path("/{id}/publish/false")
    @ApiOperation("下架品牌")
    public Response unPublishBrand(@PathParam("id")String id){
        CultureBrand brand = this.brandService.getOne(id);
        if(brand == null){
            throw new BsException("品牌不存在");
        }
        brand.setIsPublish(false);
        this.brandService.modifyBrand(id,brand);
        return successUpdate();
    }

    @GET @Path("/{id}")
    @ApiOperation("查询品牌详情")
    public CultureBrand getBrand(@PathParam("id")String id){
        return this.brandService.getOne(id);
    }

    @DELETE @Path("/{id}")
    @ApiOperation("删除品牌")
    public Response delBrand(@PathParam("id")String id){
        this.brandService.delete(id);
        return successDelete();
    }


}
