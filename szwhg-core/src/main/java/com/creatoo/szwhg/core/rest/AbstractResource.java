package com.creatoo.szwhg.core.rest;


import com.creatoo.szwhg.core.exception.BsException;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yunyan on 2017/3/25.
 */
public class AbstractResource {

    protected Response successCreate(String id){
        Map<String,String> idmap=new HashMap<>();
        idmap.put("id",id);
        return Response.ok(idmap).build();
    }
    protected Response successCreate(){
        return Response.ok().build();
    }

    protected Response successDelete(){
        return Response.ok().build();
    }

    protected Response successUpdate(){
        return Response.ok().build();
    }

    protected  Response success(Map<String,Object> map){
        return Response.ok(map).build();
    }

    protected <T> T successFindOne(T obj){
        if(obj==null) throw new BsException("资源不存在");
        else return obj;
    }
}
