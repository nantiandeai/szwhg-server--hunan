package com.creatoo.szwhg.user.service;

import com.creatoo.szwhg.base.service.FileService;
import com.creatoo.szwhg.core.exception.BsException;
import com.creatoo.szwhg.core.model.IdentifyStatus;
import com.creatoo.szwhg.core.model.ResourceType;
import com.creatoo.szwhg.core.util.CommonUtil;
import com.creatoo.szwhg.core.util.GeneralPredicatesBuilder;
import com.creatoo.szwhg.user.dao.BlackLogDao;
import com.creatoo.szwhg.user.dao.IdentifyApplyDao;
import com.creatoo.szwhg.user.dao.ThumbDao;
import com.creatoo.szwhg.user.dao.UserDao;
import com.creatoo.szwhg.user.model.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 用户行为服务
 * Created by yunyan on 2017/11/18.
 */
@Service
public class UserActionService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private IdentifyApplyDao applyDao;
    @Autowired
    private FileService fileService;
    @Autowired
    private BlackLogDao blackLogDao;
    @Autowired
    private ThumbDao thumbDao;

    /**
     * 增加积分
     * @param userId
     * @param score
     */
    public void addBonus(String userId,int score){
        User user=userDao.findOne(userId);
        if(user==null) return;
        int bonus= Optional.ofNullable(user.getBonus()).orElse(0);
        user.setBonus(bonus+score);
        userDao.save(user);
    }

    public void thumbUp(String userId, ResourceType type,String objId){
        User puser=userDao.findOne(userId);
        if(puser==null) throw new BsException("找不到对应账号");
        Thumb exist=thumbDao.findFirstByUserIdAndTypeAndObjectId(userId,type,objId);
        if(exist!=null) throw new BsException("已经点赞");
        Thumb thumb=new Thumb();
        thumb.setUserId(userId);
        thumb.setType(type);
        thumb.setObjectId(objId);
        thumb.setTime(LocalDateTime.now());
        thumbDao.save(thumb);
    }

    public void thumbDown(String userId, ResourceType type,String objId){
        User puser=userDao.findOne(userId);
        if(puser==null) throw new BsException("找不到对应账号");
        Thumb thumb=thumbDao.findFirstByUserIdAndTypeAndObjectId(userId, type, objId);
        if(thumb!=null) thumbDao.delete(thumb);
    }

    public boolean isThumbUp(String userId, ResourceType type,String objId){
        Thumb thumb=thumbDao.findFirstByUserIdAndTypeAndObjectId(userId, type, objId);
        if(thumb!=null) return true;
        else return false;
    }

    public int sumOfThumb(ResourceType type,String objId){
        return thumbDao.findByTypeAndObjectId(type,objId).size();
    }

    public String addFavorite(String userid, UserFavorite favorite){
        User puser=userDao.findOne(userid);
        if(puser==null) throw new BsException("找不到对应账号");
        List<UserFavorite> favorites=puser.getFavorites();
        if(favorites==null){
            favorites=new ArrayList<>();
            puser.setFavorites(favorites);
        }
        String id= UUID.randomUUID().toString();
        favorite.setId(id);
        favorite.setTime(CommonUtil.getCurrentTime());
        favorites.add(favorite);
        userDao.save(puser);
        return id;
    }

    /**
     * 取消收藏
     * @param userid
     * @param favoriteId
     */
    public void deleteFavorite(String userid, String favoriteId) {
        User puser = userDao.findOne(userid);
        if (puser == null) {
            throw new BsException("找不到对应账号");
        }
        List<UserFavorite> favorites = puser.getFavorites();
        if (favorites == null) {
            return;
        }
        UserFavorite target = null;
        for (UserFavorite f : favorites) {
            if (StringUtils.isNotBlank(f.getObjectId()) && f.getObjectId().equals(favoriteId)) {
                target = f;
                break;
            }
        }
        if (target != null) {
            favorites.remove(target);
        }
        userDao.save(puser);
    }

    public List<UserFavorite> getFavorite(String userid,String type){
        User puser=userDao.findOne(userid);
        if(puser==null) throw new BsException("找不到对应账号");
        List<UserFavorite> favorites=puser.getFavorites();
        if(favorites==null) return null;
        List<UserFavorite> result=new ArrayList<>();
        for(UserFavorite favorite:favorites){
            if ((favorite.getType() != null) && favorite.getType().name().equals(type)) {
                result.add(favorite);
            }
        }
        result.sort((o1,o2)->o1.getSeqno()-o2.getSeqno());
        return result;
    }

    public String createIdentifyApply(IdentifyApply apply){
        apply.setIdentifyStatus(IdentifyStatus.Wait);
        apply.setCreateTime(LocalDateTime.now());
        applyDao.save(apply);
        User user=userDao.findOne(apply.getUserId());
        if(apply.getIsSelf()) user.setIdentifyStatus(IdentifyStatus.Wait);
        else{
            for(FamilyMember member:user.getMembers()){
                if(member.getIdNumber().equals(apply.getIdnumber())){
                    member.setIdentifyStatus(IdentifyStatus.Wait);
                }
            }
        }
        userDao.save(user);
        return apply.getId();
    }

    public Page<IdentifyApply> findApplys(String search, Pageable pageable){
        GeneralPredicatesBuilder builder=new GeneralPredicatesBuilder(IdentifyApply.class);
        BooleanExpression exp=builder.build(search);
        Page<IdentifyApply> applies = applyDao.findAll(exp,pageable);
        return applies.map(identifyApply -> {
            List<IdentifyApply> identifyApplies = applyDao.findByIdnumberAndIdentifyStatus(identifyApply.getIdnumber(),IdentifyStatus.Yes.name());
            if (identifyApplies != null && identifyApplies.size()>0 && identifyApply.getIdentifyStatus().equals(IdentifyStatus.Wait)) identifyApply.setRemark("建议通过");
            return identifyApply;
        });
    }

    public void passIdentifyApply(String applyId){
        IdentifyApply apply=applyDao.findOne(applyId);
        apply.setIdentifyStatus(IdentifyStatus.Yes);
        apply.setAuditTime(LocalDateTime.now());
        applyDao.save(apply);
        User user=userDao.findOne(apply.getUserId());
        if(apply.getIsSelf()) {
            user.setIdentifyStatus(IdentifyStatus.Yes);
            user.setName(apply.getRealname());
            user.setIdNumber(apply.getIdnumber());
        }else{
            for(FamilyMember member:user.getMembers()){
                if(member.getIdNumber().equals(apply.getIdnumber())){
                    member.setIdentifyStatus(IdentifyStatus.Yes);
                }
            }
        }
        userDao.save(user);
    }

    public void rejectIdentifyApply(String applyId,String comment){
        IdentifyApply apply=applyDao.findOne(applyId);
        apply.setIdentifyStatus(IdentifyStatus.Fail);
        apply.setAuditComment(comment);
        applyDao.save(apply);
        User user=userDao.findOne(apply.getUserId());
        if(apply.getIsSelf()) {
            user.setIdentifyStatus(IdentifyStatus.Fail);
        } else{
            for(FamilyMember member:user.getMembers()){
                if(member.getIdNumber().equals(apply.getIdnumber())){
                    member.setIdentifyStatus(IdentifyStatus.Fail);
                }
            }
        }
        userDao.save(user);
    }

    /**
     * 限制会员行为
     * @param limitType
     * @param userid
     * @param limitDays
     * @param reasonType
     */
    public void limitUser(PermitLimitType limitType,String userid,int limitDays,LimitReasonType reasonType){
        User user=userDao.findOne(userid);
        if(user==null)  return;
        BlackLog log=new BlackLog();
        log.setUserId(userid);
        log.setNickname(user.getNickname());
        log.setMobile(user.getMobile());
        LocalDateTime time=LocalDateTime.now();
        log.setHappenTime(time);
        log.setLimitType(limitType);
        log.setReasonType(reasonType);
        log.setLimitDays(limitDays);
        blackLogDao.save(log);
        user.addLimit(limitType,time.plusDays(limitDays));
        userDao.save(user);
    }



}
