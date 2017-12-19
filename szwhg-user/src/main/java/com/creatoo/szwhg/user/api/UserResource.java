package com.creatoo.szwhg.user.api;

import com.creatoo.szwhg.core.exception.BsException;
import com.creatoo.szwhg.core.model.ResourceType;
import com.creatoo.szwhg.core.rest.AbstractResource;
import com.creatoo.szwhg.core.rest.Bonus;
import com.creatoo.szwhg.core.rest.Pagination;
import com.creatoo.szwhg.user.model.*;
import com.creatoo.szwhg.user.service.UserActionService;
import com.creatoo.szwhg.user.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yunyan on 2017/8/16.
 */
@Component
@Path("/user/users")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "会员管理", produces = "application/json")
public class UserResource extends AbstractResource{
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserActionService userActionService;

    @Bonus //积分注解
    @POST
    @ApiOperation("会员注册")
    public Response registUser(@Valid @NotNull User user){
        String id= userInfoService.registUser(user);
        return this.successCreate(id);
    }

    @GET @ApiOperation("查询会员列表")
    public Page<User> findAll(@QueryParam("search") @ApiParam("查询条件") String search, @Pagination Pageable pageable){
        return userInfoService.findAll(search, pageable);
    }

    @PUT @Path("/{id}")
    @ApiOperation("修改会员信息")
    public Response modifyUser(@PathParam("id")String id,User user){
        userInfoService.modifyUser(id,user);
        return this.successUpdate();
    }
    @DELETE @Path("/{id}")
    @ApiOperation("删除会员")
    public Response deleteUser(@PathParam("id")String id){
        userInfoService.deleteUser(id);
        return this.successDelete();
    }
    @PUT @Path("/{id}/lock")
    @ApiOperation("锁定会员")
    public Response lockUser(@PathParam("id")String id){
        User user= userInfoService.getUserById(id);
        user.setLocked(true);
        userInfoService.modifyUser(id,user);
        return this.successUpdate();
    }
    @PUT @Path("/{id}/unlock")
    @ApiOperation("解锁会员")
    public Response unlockUser(@PathParam("id")String id){
        User user= userInfoService.getUserById(id);
        user.setLocked(false);
        userInfoService.modifyUser(id,user);
        return this.successUpdate();
    }

    @GET @Path("/{id}")
    @ApiOperation("获取会员信息")
    public User getUser(@PathParam("id") String id){
        return userInfoService.getUserById(id);
    }

    @POST @Path("/{id}/binds")
    @ApiOperation("增加会员绑定")
    public Response bindAccount(@PathParam("id")String id, AccountBind bind){
        userInfoService.bindAccount(id,bind);
        return this.successUpdate();
    }
    @DELETE @Path("/{id}/binds/{type}")
    @ApiOperation("删除会员绑定")
    public Response deleteBind(@PathParam("id")String id,@PathParam("type")String type){
        userInfoService.deleteBind(id,type);
        return this.successUpdate();
    }
    @GET @Path("/{id}/binds")
    @ApiOperation("查询会员绑定列表")
    public List<AccountBind> getBinds(@PathParam("id")String id){
        return userInfoService.getUserBinds(id);
    }


    @Bonus //积分注解
    @POST @Path("/login/{type}")
    @ApiOperation("会员登录")
    public User login(@PathParam("type")LoginType loginType, Map<String,String> data){
        User user=null;
        if(loginType== LoginType.mobile){
            String mobile=data.get("mobile");
            String password=data.get("password");
            user=userInfoService.authticateUser(mobile,password);
        }else if(loginType== LoginType.weixin){
            String wenxinId=data.get("uid");
            user= userInfoService.findByBind(BindType.valueOf(loginType.name()),wenxinId);
        }
        if(user==null) throw new AuthenticationCredentialsNotFoundException("账号不存在");
        return user;
    }

    @POST @Path("/{id}/favorites")
    @ApiOperation("增加会员收藏")
    public Response addFavorite(@PathParam("id")String id, UserFavorite favorite){
        String fid= userActionService.addFavorite(id,favorite);
        return this.successCreate(fid);
    }
    @DELETE @Path("/{id}/favorites/{fid}")
    @ApiOperation("删除用户收藏")
    public Response deleteFavorite(@PathParam("id")String userid,@PathParam("fid")String fid){
        userActionService.deleteFavorite(userid,fid);
        return this.successDelete();
    }
    @GET @Path("/{id}/favorites/{type}")
    @ApiOperation("查询会员收藏")
    public List<UserFavorite> getUserFavorite(@PathParam("id")String userid,@PathParam("type")String type){
        return userActionService.getFavorite(userid,type);
    }
    @GET @Path("/{id}/favorites/{type}/{objectId}")
    @ApiOperation("查询会员是否收藏")
    public Map<String, Boolean> getUserFavoriteStatus(@PathParam("id") String userid, @PathParam("type") String type, @PathParam("objectId") String objectId) {
        List<UserFavorite> favorites = userActionService.getFavorite(userid, type);
        boolean exist = false;
        if (favorites != null && favorites.size() > 0) {
            for (UserFavorite favorite : favorites) {
                if (StringUtils.isNotBlank(favorite.getObjectId())&&favorite.getObjectId().equals(objectId)) {
                    exist = true;
                    break;
                }
            }
        }
        Map<String, Boolean> map = new HashMap<>();
        map.put("status", exist);
        return map;
    }
    @GET @Path("/{id}/thumbup/{type}/{objectId}")
    @ApiOperation("查询是否点赞")
    public Map<String,Boolean> isThumbUp(@PathParam("id") String userid,@PathParam("type")ResourceType type,@PathParam("objectId")String objectId){
        boolean status= userActionService.isThumbUp(userid,type,objectId);
        Map<String,Boolean> map=new HashMap<>();
        map.put("status",status);
        return map;
    }
    @POST @Path("/{id}/thumbup/{type}/{objectId}")
    @ApiOperation("点赞")
    public Response addThumbUp(@PathParam("id")String userid,@PathParam("type")ResourceType type,@PathParam("objectId") String objId){
        userActionService.thumbUp(userid,type,objId);
        return this.successCreate();
    }
    @DELETE @Path("/{id}/thumbup/{type}/{objectId}")
    @ApiOperation("取消点赞")
    public Response deleteThumbUp(@PathParam("id")String userid,@PathParam("type")ResourceType type,@PathParam("objectId") String objId){
        userActionService.thumbDown(userid,type,objId);
        return this.successCreate();
    }

    @PUT
    @Path("/{mobilePhone}/pwd")
    @ApiOperation(value = "用户更改密码",notes = "newPwd- 新密码")
    public Response changePwdWithOld(@PathParam("mobilePhone") String mobilePhone,Map<String,String> data){
//        String oldpwd=data.get("oldPwd");
        String newPwd=data.get("newPwd");
        userInfoService.changePwdWithOld(mobilePhone,newPwd);
        return this.successUpdate();
    }

    @GET @Path("/{id}/members")
    @ApiOperation("查看常用联系人列表")
    public List<FamilyMember> getMembers(@PathParam("id")String userid){
        User user=userInfoService.getUserById(userid);
        if(user==null) throw new BsException("会员不存在");
        return user.getMembers();
    }
    @POST @Path("/{id}/members")
    @ApiOperation("创建常用联系人")
    public Response createMember(@PathParam("id")String userid,@Valid @NotNull FamilyMember member){
        userInfoService.addMember(userid,member);
        return this.successCreate();
    }
    @DELETE @Path("/{id}/members/{idnumber}")
    @ApiOperation("删除常用联系人")
    public Response removeMember(@PathParam("id")String userid,@PathParam("idnumber")String idnumber){
        userInfoService.removeMember(userid, idnumber);
        return this.successDelete();
    }

}
