package com.creatoo.szwhg.user.service;

import com.creatoo.szwhg.base.service.FileService;
import com.creatoo.szwhg.core.exception.BsException;
import com.creatoo.szwhg.core.model.ClientType;
import com.creatoo.szwhg.core.model.IdentifyStatus;
import com.creatoo.szwhg.core.util.CommonUtil;
import com.creatoo.szwhg.core.util.GeneralPredicatesBuilder;
import com.creatoo.szwhg.user.dao.BlackLogDao;
import com.creatoo.szwhg.user.dao.IdentifyApplyDao;
import com.creatoo.szwhg.user.dao.UserDao;
import com.creatoo.szwhg.user.dao.UserDaoCustom;
import com.creatoo.szwhg.user.model.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yunyan on 2017/8/15.
 */
@Service
public class UserInfoService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private IdentifyApplyDao applyDao;
    @Autowired
    private FileService fileService;
    @Autowired
    private UserDaoCustom userDaoCustom;
    @Autowired
    private BlackLogDao blackDao;

    public String registUser(User user){
        User user1 = userDao.findFirstByMobile(user.getMobile());
        if (user1 != null) {
            throw new BsException("该手机号已注册");
        }
        user.setIdentifyStatus(IdentifyStatus.Not);
        if(StringUtils.isNotBlank(user.getPassword())){
            BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
        }
        user.setRegistTime(LocalDateTime.now());
        userDao.save(user);
        return user.getId();
    }

    public Page<User> findAll(String search, Pageable pageable){
        GeneralPredicatesBuilder builder=new GeneralPredicatesBuilder(User.class);
        BooleanExpression exp=builder.build(search);
        return userDao.findAll(exp,pageable);
    }

    public void modifyUser(String id,User user){
        User puser=userDao.findOne(id);
        if(puser==null) throw new BsException("找不到对应账号");
        try {
            CommonUtil.copyNullProperties(user,puser);
        } catch (Exception e) {
            throw new BsException("处理出错",e);
        }
        userDao.save(puser);
    }

    public void deleteUser(String id){
       userDao.delete(id);
    }
    public User getUserById(String id){
        return userDao.findOne(id);
    }

    public User authticateUser(String mobile,String password){
        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
        User user=userDao.findFirstByMobile(mobile);
        if(user==null) return null;
        if(encoder.matches(password,user.getPassword())) return user;
        else return null;
    }

    public User findByBind(BindType bindType, String account){
        AccountBind bind=new AccountBind();
        bind.setAccount(account);
        bind.setType(bindType);
        User user=userDao.findFistByBindsContains(bind);
        return user;
    }


    public void bindAccount(String id, AccountBind bind){
        User puser=userDao.findOne(id);
        if(puser==null) throw new BsException("找不到对应账号");
        List<AccountBind> binds=puser.getBinds();
        if(binds==null) binds=new ArrayList<>();
        puser.setBinds(binds);
        for (AccountBind accountBind:binds){
            if (accountBind.getAccount().equals(bind.getAccount()) && accountBind.getType().equals(bind.getType())) throw new BsException("该账号已绑定");
        }
        binds.add(bind);
        userDao.save(puser);
    }

    public void deleteBind(String id,String type){
        User puser=userDao.findOne(id);
        if(puser==null) throw new BsException("找不到对应账号");
        List<AccountBind> binds=puser.getBinds();
        if(binds!=null){
            AccountBind target=null;
            for(AccountBind bind:binds){
                if(bind.getType().equals(type)){
                    target=bind;
                    break;
                }
            }
            if(target!=null) binds.remove(target);
        }
        userDao.save(puser);
    }

    public List<AccountBind> getUserBinds(String id){
        User puser=userDao.findOne(id);
        if(puser==null) throw new BsException("找不到对应账号");
        return puser.getBinds();
    }



    /**
     * 用户根据旧密码修改为新密码
     * @param mobilePhone
     * @param newPwd
     */
    public void changePwdWithOld(String mobilePhone, String newPwd) {
        User user = userDao.findFirstByMobile(mobilePhone);
       if (user == null) throw new BsException("用户信息不存在");
        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
//        if(!encoder.matches(oldpwd,user.getPassword())) throw new BsException("旧密码不正确");
        if (StringUtils.isBlank(newPwd.trim())) throw new BsException("新密码不能为空");
        user.setPassword(encoder.encode(newPwd));
        userDao.save(user);
    }

    public void addMember(String userid,FamilyMember member){
        User user=userDao.findOne(userid);
        if(user==null)  return;
        user.addFamilyMember(member);
        userDao.save(user);
    }

    public void removeMember(String userid,String idnumber){
        User user=userDao.findOne(userid);
        if(user==null)  return;
        user.removeFamilyMember(idnumber);
        userDao.save(user);
    }

    /**
     * 会员基本统计数据
     * @return
     */
    public UserGeneralStat statGeneral(){
        QUser quser=QUser.user;
        long total=userDao.count(); //注册用户总数
        long identifyCount=userDao.count(quser.identifyStatus.eq(IdentifyStatus.Yes)); //认证会员总数
        long pcRegistSum=userDao.count(quser.registerMode.eq(ClientType.pcweb)); //pc端注册用户总数
        long mobileRegistSum=userDao.count(quser.registerMode.eq(ClientType.mobileweb)); //微信端注册用户总数
        UserGeneralStat stat=new UserGeneralStat();
        stat.setTotal(total);
        stat.setIdentifyCount(identifyCount);
        stat.setPcRegistSum(pcRegistSum);
        stat.setMobileRegistSum(mobileRegistSum);
        return stat;
    }

    /**
     * 会员月注册统计
     * @param startMonth
     * @param endMonth
     * @return
     */
    public List<UserRegistMonthStat> statRegistMonth(String startMonth, String endMonth){
        return userDaoCustom.statRegistByMonth(startMonth,endMonth);
    }

    /**
     * 会员日注册统计
     * @param startDay
     * @param endDay
     * @return
     */
    public List<UserRegistDayStat> statRegistDay(LocalDate startDay, LocalDate endDay){
        return userDaoCustom.statRegistByDay(startDay,endDay);
    }

    /**
     * 查询黑名单日志
     * @param search
     * @param pageable
     * @return
     */
    public Page<BlackLog> findAllBlackLog(String search, Pageable pageable){
        GeneralPredicatesBuilder builder=new GeneralPredicatesBuilder(BlackLog.class);
        BooleanExpression exp=builder.build(search);
        return blackDao.findAll(exp,pageable);
    }

}
