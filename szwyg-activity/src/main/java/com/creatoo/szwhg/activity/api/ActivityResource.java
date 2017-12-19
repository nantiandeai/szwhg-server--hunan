package com.creatoo.szwhg.activity.api;

import com.creatoo.szwhg.activity.model.Activity;
import com.creatoo.szwhg.activity.model.ActivityItm;
import com.creatoo.szwhg.activity.model.ActivityReserveStat;
import com.creatoo.szwhg.activity.service.ActivityService;
import com.creatoo.szwhg.base.model.Comment;
import com.creatoo.szwhg.base.model.CommentStatus;
import com.creatoo.szwhg.core.exception.BsException;
import com.creatoo.szwhg.core.model.*;
import com.creatoo.szwhg.core.rest.AbstractResource;
import com.creatoo.szwhg.core.rest.Pagination;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * Created by yunyan on 2017/8/17.
 */
@Component
@Path("/activity/activitys")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "活动管理", produces = "application/json")
//@CacheConfig(cacheNames = "activity")
public class ActivityResource extends AbstractResource {
    @Autowired
    private ActivityService activityService;

    @POST
    @ApiOperation("创建活动")
    public Response createActivity(@Valid @NotNull Activity activity) {
        String id = activityService.createActivity(activity);
        return this.successCreate(id);
    }

    @GET
    @ApiOperation("查询活动列表")
    public Page<Activity> findAll(@QueryParam("search") String search, @Pagination Pageable pageable) {
        return activityService.findAll(search, pageable);
    }

    @PUT
    @Path("/{id}")
    @ApiOperation("编辑活动")
//    @CacheEvict(key = "#id")
    public Response modifyActivity(@PathParam("id") String id, @Valid @NotNull Activity activity) {
        activityService.modifyAct(id, activity);
        return this.successUpdate();
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation("删除活动")
//    @CacheEvict(key = "#id")
    public Response deleteActivity(@PathParam("id") String id) {
        activityService.deleteAct(id);
        return this.successDelete();
    }

    @GET
    @Path("/{id}")
    // @Cacheable(key="#id")
    @ApiOperation("查看活动详情")
    public Activity getAct(@PathParam("id") String id) {
        return activityService.getById(id);
    }

    @POST
    @Path("/{id}/itms")
    @ApiOperation("创建单个活动场次")
    public Response addActivityItm(@PathParam("id") String id, ActivityItm itm) {
        String itmid = activityService.addActItm(id, itm);
        return this.successCreate(itmid);
    }

    @POST
    @Path("/{id}/itms/batch")
    @ApiOperation("批量创建活动场次")
    public Response addActivityItm(@PathParam("id") String id, List<ActivityItm> itms) {
        activityService.addActItms(id, itms);
        return this.successUpdate();
    }

    @GET
    @Path("/{id}/itms")
    @ApiOperation("查询活动所有场次")
    public List<ActivityItm> getAllItms(@PathParam("id") String id) {
        Activity act = activityService.getById(id);
        return act.getItms();
    }

    @GET
    @Path("/{id}/itms/{itmid}")
    @ApiOperation("查询场次")
    public ActivityItm getItm(@PathParam("id") String activityId, @PathParam("itmid") String itmId) {
        Activity act = activityService.getById(activityId);
        List<ActivityItm> itms = act.getItms();
        if (itms == null) {
            return null;
        }
        ActivityItm target = null;
        for (ActivityItm itm : itms) {
            if (itm.getId().equals(itmId)) {
                target = itm;
                break;
            }
        }
        return target;
    }

    @DELETE
    @Path("/{id}/itms/{itmid}")
    @ApiOperation("删除单个活动场次")
    public Response deleteItm(@PathParam("id") String activityId, @PathParam("itmid") String itmId) {
        activityService.deleteItm(activityId, itmId);
        return this.successDelete();
    }

    @DELETE
    @Path("/{id}/itms")
    @ApiOperation("删除所有活动场次")
    public Response deleteAllItms(@PathParam("id") String activityId) {
        activityService.deleteAllItms(activityId);
        return this.successDelete();
    }

    @PUT
    @Path("/{id}/onlineStatus")
    @ApiOperation("变更活动在线状态")
    public Response changeStatus(@PathParam("id") String id, FlowLog oper) {
        activityService.changeActOnlineStatus(id, oper);
        return this.successUpdate();
    }

    @PUT
    @Path("/{id}/top/true")
    @ApiOperation("推荐活动")
    public Response recommendActivity(@PathParam("id") String id) {
        Activity act = activityService.getById(id);
        act.setIsRecommend(true);
        activityService.modifyAct(id, act);
        return this.successUpdate();
    }

    @PUT
    @Path("/{id}/top/false")
    @ApiOperation("取消推荐")
    public Response unRecommendActivity(@PathParam("id") String id) {
        Activity act = activityService.getById(id);
        act.setIsRecommend(false);
        activityService.modifyAct(id, act);
        return this.successUpdate();
    }

    @GET
    @Path("/{id}/stat")
    @ApiOperation("活动预定统计")
    public ActivityReserveStat getReserveStat(@PathParam("id") String id) {
        ActivityReserveStat stat = new ActivityReserveStat();
        stat.setCheckSum(10);
        stat.setTotal(60);
        stat.setReserveSum(20);
        return stat;
    }

    @POST
    @Path("/{id}/research")
    @ApiOperation("添加活动问卷")
    public Response addResearch(@PathParam("id") String id, Research research) {
        activityService.addResearch(id, research);
        return this.successUpdate();
    }

    @GET
    @Path("/{id}/research")
    @ApiOperation("获取活动问卷题目")
    public Research getResearch(@PathParam("id") String id) {
        Activity activity = activityService.getById(id);
        return activity.getResearch();
    }

    @DELETE
    @Path("/{id}/research")
    @ApiOperation("删除活动问卷")
    public Response deleteResearch(@PathParam("id") String id) {
        activityService.removeResearch(id);
        return this.successUpdate();
    }

    @PUT
    @Path("/{id}/research")
    @ApiOperation("编辑活动问卷")
    public Response editResearch(@PathParam("id") String id, Research research) {
        activityService.addResearch(id, research);
        return this.successUpdate();
    }

    @PUT
    @Path("/{id}/research/publish/true")
    @ApiOperation("发布活动问卷")
    public Response publishResearch(@PathParam("id") String id) {
        Activity activity = activityService.getById(id);
        Research research = activity.getResearch();
        if (research == null) {
            throw new BsException("活动问卷不存在");
        }
        research.setIsPublished(true);
        activityService.addResearch(id, research);
        return this.successUpdate();
    }

    @PUT
    @Path("/{id}/research/publish/false")
    @ApiOperation("取消发布活动问卷")
    public Response unPublishResearch(@PathParam("id") String id) {
        Activity activity = activityService.getById(id);
        Research research = activity.getResearch();
        if (research == null) {
            throw new BsException("活动问卷不存在");
        }
        research.setIsPublished(false);
        activityService.addResearch(id, research);
        return this.successUpdate();
    }

    @POST
    @Path("/{id}/digitinfos")
    @ApiOperation("添加活动纪实")
    public Response createDigitalInfo(@PathParam("id") String id, @Valid @NotNull DigitInfo digitInfo) {
        return this.successCreate(activityService.createDigital(id, digitInfo));
    }

    @DELETE
    @Path("/{id}/digitinfos/{did}")
    @ApiOperation("删除活动纪实")
    public Response deleteDigitalInfo(@PathParam("id") String id, @PathParam("did") String did) {
        activityService.deleteDigitInfo(id, did);
        return this.successDelete();
    }

    @GET
    @Path("/{id}/digitinfos")
    @ApiOperation("查询活动纪实")
    public List<DigitInfo> getDigitalInfos(@PathParam("id") String id) {
        return activityService.getDigitInfos(id);
    }

    @GET
    @Path("/{id}/digitinfos/{did}")
    @ApiOperation("查询活动纪实详情")
    public DigitInfo getOneDigitInfo(@PathParam("id") String id, @PathParam("did") String did) {
        return activityService.getOneDigitInfo(id, did);
    }

    @PUT
    @Path("/{id}/digitinfos/{did}")
    @ApiOperation("编辑活动纪实")
    public Response modifyDigitInfo(@PathParam("id") String id, @PathParam("did") String did, DigitInfo digitInfo) {
        activityService.editDigitalInfo(id, did, digitInfo);
        return this.successUpdate();
    }

    @POST
    @Path("/{id}/comments")
    @ApiOperation("添加评论")
    public Response addComment(@PathParam("id") String trainid, Comment comment) {
        String commentid = activityService.addComment(trainid, comment);
        return this.successCreate(commentid);
    }

    @DELETE
    @Path("/{id}/comments/{commentid}")
    @ApiOperation("删除评论")
    public Response deleteComment(@PathParam("id") String trainid, @PathParam("commentid") String commentid) {
        activityService.deleteComment(trainid, commentid);
        return this.successDelete();
    }

    @GET
    @Path("/{id}/comments")
    @ApiOperation("获取评论列表")
    public Page<Comment> getComments(@PathParam("id") String trainid, @Pagination Pageable pageable) {
        return activityService.findAllComments(trainid, pageable);
    }

    @POST
    @Path("/{id}/research/answers")
    @ApiOperation("提交问卷")
    public Response saveAnswers(@PathParam("id") String id, UserResearchSheet sheet) {
        activityService.submitResearchAnswer(id, sheet);
        return this.successCreate();
    }

    @GET @Path("/{id}/research/answers/{userid}")
    @ApiOperation("查询会员问卷内容")
    public UserResearchSheet getUserResearchSheet(@PathParam("id") String id,@PathParam("userid")String userid){
        return activityService.getUserResearchSheet(id, userid);
    }

    @GET
    @Path("/{id}/research/answers")
    @ApiOperation("获得提交问卷列表")
    public List<UserResearchSheet> getAnswers(@PathParam("id") String id) {
        return activityService.findAnswers(id);
    }

}
