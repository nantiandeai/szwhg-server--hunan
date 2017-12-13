package com.creatoo.szwhg.base.api;

import com.creatoo.szwhg.base.model.StoreItem;
import com.creatoo.szwhg.base.service.StoreItemService;
import com.creatoo.szwhg.core.rest.AbstractResource;
import com.creatoo.szwhg.core.rest.Pagination;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 *  题库资源
 * Created by wuxiangliang on 2017/4/7.
 */
@Component
@Path("/sys/storeitems")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "题库管理", produces = "application/json")
public class StoreItemResource extends AbstractResource {
    @Autowired
   private StoreItemService storeItemService;

    @POST
    @ApiOperation(value = "创建题库")
    public String saveStoreItem(@Valid StoreItem storeItem){
        return storeItemService.saveStoreItem(storeItem);
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "删除题库")
    public void deleteById(@PathParam("id") String id){
        storeItemService.deleteById(id);
    }

    @PUT
    @Path("/{id}")
    @ApiOperation(value = "更新题库")
    public void updateStoreItem(@PathParam("id") String id, StoreItem storeItem){
        storeItemService.updateStoreItem(id,storeItem);
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "获取单个题库")
    public StoreItem findById(@PathParam("id") String id){
        return storeItemService.findById(id);
    }

    @GET
    @ApiOperation(value = "获取题库列表")
    public Page<StoreItem> findAll(@QueryParam("search") String search, @Pagination Pageable pageable){
        return storeItemService.findAll(search,pageable);
    }

}
