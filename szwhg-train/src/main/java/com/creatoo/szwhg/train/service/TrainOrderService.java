package com.creatoo.szwhg.train.service;

import com.creatoo.szwhg.base.service.FileService;
import com.creatoo.szwhg.core.exception.BsException;
import com.creatoo.szwhg.core.model.IdentifyStatus;
import com.creatoo.szwhg.core.model.TrainOrderStatus;
import com.creatoo.szwhg.core.util.CommonUtil;
import com.creatoo.szwhg.core.util.GeneralPredicatesBuilder;
import com.creatoo.szwhg.core.util.SpringContextUtil;
import com.creatoo.szwhg.train.dao.TrainDao;
import com.creatoo.szwhg.train.dao.TrainOrderDao;
import com.creatoo.szwhg.train.model.*;
import com.creatoo.szwhg.user.model.FamilyMember;
import com.creatoo.szwhg.user.model.User;
import com.creatoo.szwhg.user.service.UserInfoService;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 培训订单服务
 * Created by wangxl on 2017/9/5.
 */
@Service
public class TrainOrderService {
    @Autowired
    private UserInfoService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private TrainService trainService;

    @Autowired
    private TrainOrderDao trainOrderDao;
    @Autowired
    private TrainDao trainDao;

    /**
     * 根据培训和订单标识查询培训订单详情
     * @param orderid 订单ID
     * @return
     */
    public TrainOrder findById(String orderid){
        QTrainOrder qto = QTrainOrder.trainOrder;
        return this.trainOrderDao.findOne(orderid);
    }

    /**
     * 查询用户的当前培训订单
     * @param userid 会员ID
     * @return Page<TrainOrder>
     */
    public List<TrainOrder> getUserOrders(String userid){
        QTrainOrder qto = QTrainOrder.trainOrder;
        List<TrainOrder> list=new ArrayList<>();
        this.trainOrderDao.findAll(qto.userId.eq(userid).and(qto.trainEndTime.gt(LocalDate.now()))).forEach(order->list.add(order));
        return list;
    }

    /**
     * 查询用户的历史培训订单
     * @param userid 会员ID
     * @param pageable 分页信息
     * @return Page<TrainOrder>
     */
    public Page<TrainOrder> findUserHistory(String userid, Pageable pageable){
        QTrainOrder qto = QTrainOrder.trainOrder;
        return this.trainOrderDao.findAll(qto.userId.eq(userid).and(qto.trainEndTime.lt(LocalDate.now())), pageable);
    }

    /**
     * 查询培训订单
     * @param search 查询条件
     * @param pageable 分页信息
     * @return Page<TrainOrder>
     */
    public Page<TrainOrder> findAll(String search, Pageable pageable){
        GeneralPredicatesBuilder builder = new GeneralPredicatesBuilder(TrainOrder.class);
        BooleanExpression exp = builder.build(search);
        return this.trainOrderDao.findAll(exp, pageable);
    }

    /**
     * 创建订单-顺序执行
     * @param order 订单信息
     * @return 订单ID
     */
    public String add(TrainOrder order){
        //一个会员一个培训只能报一次
        String userId = order.getUserId();
        String trainId = order.getTrainId();
        QTrainOrder qOrder = QTrainOrder.trainOrder;
        long userOrders = this.trainOrderDao.count(qOrder.userId.eq(userId).and(qOrder.trainId.eq(trainId)).and(qOrder.orderStatus.eq(TrainOrderStatus.Canceled)));
        if(userOrders > 0){
            throw new BsException("已经报名，不能重复报名");
        }

        //培训ID
        Train train = this.trainService.findById(order.getTrainId());
        order.setTrainName(train.getTitle());
        order.setTrainEndTime(train.getEndDate());
        order.setTrainStartTime(train.getStartDate());
        order.setAddress(train.getAddress());

        //报名用户 enrolUsers
        List<TrainOrderUser> users = order.getEnrolUsers();
        order.setPeoples(users.size());

        //每人每培训报名限制
        Integer userLimitPeoples = train.getUserLimitPeoples();
        if(users.size() > userLimitPeoples){
            throw new BsException("提交报名人数("+users.size()+"人)超过培训会员限制报名人数("+userLimitPeoples+")");
        }

        //如果需要实名，每个报名的用户需要实名
        boolean isRealName = train.getIsRealName();//是否需要实名
        if(isRealName) {
            //会员实名成员列表
            Map<String, String> identifyUser = new HashMap<>();
            User _user = this.userService.getUserById(userId);
            List<FamilyMember> members = _user.getMembers();
            for(FamilyMember member : members){
                if(member.getIdentifyStatus()== IdentifyStatus.Yes){
                    identifyUser.put(member.getName(), member.getIdNumber());
                }
            }
            for (TrainOrderUser user : users) {
                if(identifyUser.containsKey(user.getIdnumber())){
                    throw new BsException("提交报名名单中，用户("+user.getUserName()+")没有经过实名认证");
                }
            }
        }
        int enrollSize=users.size();  //报名数量
        StringRedisTemplate redisTemplate= SpringContextUtil.getBean(StringRedisTemplate.class);
        String key="train-enroll-"+train.getId(); //redis键
        for(int i=0;i<enrollSize;i++){
            String enrollNo=redisTemplate.opsForList().rightPop(key); //获取报名号
            if(enrollNo==null) throw new BsException("报名已满");
        }

        //生成订单号
        order.setOrderCode(CommonUtil.generateOrderCode());
        order.setTrainItms(train.items());
        order.setOrderStatus(TrainOrderStatus.WaitAudit);
        this.trainOrderDao.save(order);
        int enrollSum=train.getEnrollSum();
        enrollSum=+order.getPeoples();
        train.setEnrollSum(enrollSum);
        trainDao.save(train);
        return order.getId();
    }

    /**
     * 通过报名
     * @param orderid
     */
    public void passOrder(String orderid){
        TrainOrder order = this.trainOrderDao.findOne(orderid);
        if(order != null){
            order.setOrderStatus(TrainOrderStatus.Success);
            this.trainOrderDao.save(order);
        }
    }

    /**
     * 拒绝报名
     * @param orderid
     */
    public void rejectOrder(String orderid){
        TrainOrder order = this.trainOrderDao.findOne(orderid);
        if(order != null){
            order.setOrderStatus(TrainOrderStatus.Rejected);
            this.trainOrderDao.save(order);
        }
    }

    /**
     * 取消订单
     * @param orderid 订单ID
     */
    public void cancelOrder(String orderid){
        TrainOrder order = this.trainOrderDao.findOne(orderid);
        if(order != null){
            order.setOrderStatus(TrainOrderStatus.Canceled);
            this.trainOrderDao.save(order);
        }
    }

    /**
     * 面试邀请
     * @param orderid
     */
    public void inviteInterview(String orderid,Interview interview){
        TrainOrder order = this.trainOrderDao.findOne(orderid);
        if(order != null){
            order.setIsInterview(true);
            this.trainOrderDao.save(order);
        }
    }

    /**
     * 获得培训订单课时列表
     * @param orderId
     * @return
     */
    public List<TrainItm> getTrainItm(String orderId){
        List<TrainItm> trainItms = new ArrayList<>();
        TrainOrder trainOrder = trainOrderDao.findOne(orderId);
        if (trainOrder == null) return trainItms;
        return trainOrder.getTrainItms();
    }

    /**
     *  课时签到
     * @param orderId
     */
    public void signTrain(String orderId, String itmid){
        TrainOrder trainOrder = trainOrderDao.findOne(orderId);
        if (trainOrder == null) throw new BsException("课时订单不存在");
        List<TrainItm> trainItms = trainOrder.getTrainItms();
        for(TrainItm itm:trainItms){
            if(itm.getId().equals(itmid)){
                itm.setIsSign(true);
            }
        }
        trainOrderDao.save(trainOrder);
    }
}
