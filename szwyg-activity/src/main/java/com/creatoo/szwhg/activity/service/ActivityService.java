package com.creatoo.szwhg.activity.service;

import com.creatoo.szwhg.activity.dao.ActivityDao;
import com.creatoo.szwhg.activity.dao.ActivityOrderDao;
import com.creatoo.szwhg.activity.model.*;
import com.creatoo.szwhg.base.model.Comment;
import com.creatoo.szwhg.base.model.CommentStatus;
import com.creatoo.szwhg.base.service.CommentService;
import com.creatoo.szwhg.base.service.FileService;
import com.creatoo.szwhg.core.exception.BsException;
import com.creatoo.szwhg.core.model.*;
import com.creatoo.szwhg.core.util.CommonUtil;
import com.creatoo.szwhg.core.util.GeneralPredicatesBuilder;
import com.creatoo.szwhg.core.util.SpringContextUtil;
import com.creatoo.szwhg.user.model.LimitReasonType;
import com.creatoo.szwhg.user.model.PermitLimitType;
import com.creatoo.szwhg.user.model.User;
import com.creatoo.szwhg.user.service.UserActionService;
import com.creatoo.szwhg.user.service.UserInfoService;
import com.creatoo.szwhg.venue.dao.VenueRoomDao;
import com.creatoo.szwhg.venue.model.VenueRoom;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yunyan on 2017/9/2.
 */
@Service
@Slf4j
public class ActivityService {
    @Autowired
    private FileService fileService;
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private ActivityOrderDao orderDao;
    @Autowired
    private VenueRoomDao roomDao;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserActionService userActionService;
    @Autowired
    private CommentService commentService ;

    @Value("${comment.audit.isopen}")
    private Boolean auditIsopen;

    private String blackScopeParamName="user.black.activity.scope";  //黑名单规则参数名，时间范围，如一个月
    private String blackTimesParamName="user.black.activity.times";   //黑名单规则参数名，报名未参加的次数
    private String blackLimitParamName="user.black.activity.limit";   //黑名单规则参数名，需要限制的时间，单位天

    /**
     * 创建活动
     * @param act
     * @return 活动id
     */
    public String createActivity(Activity act) {
        act.setCreateTime(LocalDateTime.now());
        act.setOnlineStatus(OnlineStatus.WaitAudit);
        if (act.getVenueRoom() != null) {
            VenueRoom venueRoom = roomDao.findOne(act.getVenueRoom().getId());
            act.setVenueRoom(venueRoom);
        }
        List<ActivityItm> itms = act.getItms();
        if (itms != null && itms.size() > 0) {
            for (ActivityItm itm : itms) {
                int sum = act.totalSeats();
                itm.setLeftTicketSum(sum);
            }
        }
        activityDao.save(act);
        return act.getId();
    }

    /**
     * 查询活动列表
     * @param search 条件参数
     * @param pageable
     * @return
     */
    public Page<Activity> findAll(String search, Pageable pageable){
        GeneralPredicatesBuilder builder=new GeneralPredicatesBuilder(Activity.class);
        BooleanExpression exp=builder.build(search);
        return activityDao.findAll(exp,pageable);
    }

    /**
     * 查询活动详情
     * @param id 活动id
     * @return 活动对象
     */
    public Activity getById(String id){
        Activity activity = activityDao.findOne(id);
        if (activity != null && activity.getItms() != null && activity.getItms().size()>0){
            activity.getItms().forEach(itm->{
                // 在线选座
                if (activity.getReserveType() == ReserveType.online){
                    List<ActivityOrder> orders = orderDao.findByActivityIdAndItmId(id,itm.getId());
                    // 已預定座位
                    if (orders != null && orders.size()>0) {
                        List<String> seatnos = new ArrayList<>();
                        for (ActivityOrder order:orders){
                            List<Seat> seats = order.getSeats();
                            List<String> seatno=seats.stream().map(seat->seat.getSeatNo()).collect(Collectors.toList());
                            seatnos.addAll(seatno);
                        }
                        itm.setReserveSeats(activity.occupSeats(seatnos));
                    }
                }
            });
        }
        return activity;
    }

    /**
     * 编辑活动
     * @param id 活动id
     * @param act 活动bean
     */
    public void modifyAct(String id,Activity act){
        Activity pact=activityDao.findOne(id);
        try {
            CommonUtil.copyNullProperties(act,pact);
        } catch (Exception e) {
           throw new BsException("复制出错");
        }
        activityDao.save(pact);
    }

    /**
     * 删除活动
     * @param id 活动id
     */
    public void deleteAct(String id){
        activityDao.delete(id);
    }

    /**
     * 变更活动状态
     * @param id 活动id
     * @param oper 流程处理对象
     */
    public void changeActOnlineStatus(String id,FlowLog oper){
        Activity act=activityDao.findOne(id);
        OnlineStatus toStatus=OnlineStatus.valueOf(oper.getToStatus());
        act.setOnlineStatus(toStatus);
        List<FlowLog> list = act.getFlowLogs();
        if (list == null){
            list = new ArrayList();
        }
        list.add(oper);
        act.setFlowLogs(list);
        activityDao.save(act);
    }

    /**
     * 创建单个活动场次
     * @param id 活动id
     * @param itm 活动场次对象
     * @return 活动场次id
     */
    public String addActItm(String id, ActivityItm itm) {
        Activity act = activityDao.findOne(id);
        List<ActivityItm> itms = act.getItms();
        if (itms == null) {
            itms = new ArrayList<>();
        }
        itm.setId(UUID.randomUUID().toString());
        itms.add(itm);
        act.setItms(itms);
        activityDao.save(act);
        return itm.getId();
    }

    /**
     * 创建多个活动场次
     * @param id 活动id
     * @param itms 活动场次集合
     */
    public void addActItms(String id,List<ActivityItm> itms){
        Activity act=activityDao.findOne(id);
        List<ActivityItm> pitms=act.getItms();
        if(itms==null) {
            pitms = new ArrayList<>();
        }
        for(ActivityItm itm:itms){
            itm.setId(UUID.randomUUID().toString());
            pitms.add(itm);
        }
        act.setItms(pitms);
        activityDao.save(act);
    }

    /**
     * 删除单个活动场次
     * @param actId 活动id
     * @param itmId 活动场次id
     */
    public void deleteItm(String actId,String itmId){
        Activity act=activityDao.findOne(actId);
        List<ActivityItm> pitms=act.getItms();
        if(pitms!=null){
            ActivityItm target=null;
            for(ActivityItm itm:pitms){
                if(itm.getId().equals(itmId)){
                    target=itm;
                    break;
                }
            }
            if(target!=null) {
                pitms.remove(target);
                activityDao.save(act);
            }
        }
    }

    /**
     * 删除活动所有场次
     * @param actId 活动id
     */
    public void deleteAllItms(String actId){
        Activity act=activityDao.findOne(actId);
        act.setItms(null);
        activityDao.save(act);
    }

    /**
     * 创建活动订单
     * @param order 活动订单对象
     * @return 活动订单id
     */
    public String createOrder(ActivityOrder order){
        String actId = order.getActivityId();
        Activity act = activityDao.findOne(actId);
        String bsnType = OrderType.activity.getCode();
        order.setBsnType(bsnType);
        String orderCode = bsnType + CommonUtil.generateOrderCode();
        order.setOrderCode(orderCode);
        order.setActivityId(act.getId());
        order.setActivityName(act.getName());
        order.setActivityAddress(act.getAddress());
        order.setUnitName(act.getUnit().getName());
        order.setUnitId(act.getUnit().getId());
        order.setCreateTime(LocalDateTime.now());
        order.setOrderStatus(ActOrderStatus.reserved);
        String drawnCode = CommonUtil.generateDrawnCode();
        while (orderDao.findFirstByUnitIdAndDrawnCodeAndItmDateGreaterThan(act.getUnit().getId(), drawnCode, LocalDate.now()) != null) {
            drawnCode = CommonUtil.generateDrawnCode();
        }
        order.setDrawnCode(drawnCode);
        String itmId=order.getItmId();
        ReserveType reserveType=order.getReserveType();
        if(reserveType!= ReserveType.none){
            int bookCount = this.queryCountUserOrder(actId,order.getUserId(),order.getItmId()) ;
            int sum=Optional.ofNullable(order.getReserveSum()).orElse(order.getSeats().size()) + bookCount;
            if(sum>act.getPerAllow()) {
                throw new BsException("超过单场允许人数");
            }
            if(act.getReserveType()== ReserveType.online) {
                List<Seat> seats=order.getSeats();
                if(seats==null) {
                    throw new BsException("没有选择座位");
                }
                List<String> seatNos=order.getSeats().stream().map(seat->seat.getSeatNo()).collect(Collectors.toList());
                ActivityItm itm=act.getItmById(itmId);
                StringRedisTemplate redisTemplate= SpringContextUtil.getBean(StringRedisTemplate.class);
                String key="activity-ticket-"+act.getId()+"-itm-"+itm.getId();; //redis键
                int seatSeq = 1;
                for (Seat seat : seats) {
                    String seatNo=seat.getSeatNo();
                    long ret=redisTemplate.opsForList().remove(key,1l,seatNo);
                    if(ret==0) throw new BsException("座位已被占用，请重新选择");
                    seat.setCode(order.getDrawnCode() + seatSeq);
                    seatSeq++;
                }
                int left=itm.getLeftTicketSum();
                left-=seatNos.size();
                itm.setLeftTicketSum(left);
            }else{
                ActivityItm itm=act.getItmById(itmId);
                StringRedisTemplate redisTemplate= SpringContextUtil.getBean(StringRedisTemplate.class);
                String key="activity-ticket-"+act.getId()+"-itm-"+itm.getId();; //redis键
                int seatSeq = 1;
                List<Seat> seats=new ArrayList<>();
                for (int i=0;i<order.getReserveSum();i++) {
                    String enrollNo=redisTemplate.opsForList().rightPop(key); //获取座位号
                    if(enrollNo==null) throw new BsException("余票不足");
                    Seat seat=new Seat();
                    seat.setSeatNo(enrollNo);
                    seat.setCode(order.getDrawnCode() + seatSeq);
                    seats.add(seat);
                    seatSeq++;
                }
                order.setSeats(seats);
                int left=itm.getLeftTicketSum();
                left-=order.getReserveSum();
                itm.setLeftTicketSum(left);
            }
        }
        orderDao.save(order);
        activityDao.save(act);
        return order.getId();
    }

    /**
     * 查询订单详情
     * @param orderId 订单id
     * @return 活动订单对象
     */
    public ActivityOrder getOrderById(String orderId){
        return orderDao.findOne(orderId);
    }


    public Page<ActivityOrder> findAllOrder(String search, Pageable pageable){
        GeneralPredicatesBuilder builder=new GeneralPredicatesBuilder(ActivityOrder.class);
        BooleanExpression exp=builder.build(search);
        return orderDao.findAll(exp,pageable);
    }

    public List<ActivityOrder> getUserOrders(String userid){
        QActivityOrder qto = QActivityOrder.activityOrder;
        List<ActivityOrder> list=new ArrayList<>();
        this.orderDao.findAll(qto.userId.eq(userid).and(qto.itmDate.gt(LocalDate.now()))).forEach(order->list.add(order));
        return list;
    }


    public Page<ActivityOrder> getUserOrdersHistory(String userid, Pageable pageable){
        QActivityOrder qto = QActivityOrder.activityOrder;
        List<ActivityOrder> list=new ArrayList<>();
        return this.orderDao.findAll(qto.userId.eq(userid).and(qto.itmDate.lt(LocalDate.now())),pageable);
    }

    public List<ActivityOrder> findActivityOrders(String actId){
        return orderDao.findByActivityId(actId);
    }

    /**
     * 按照出票码查询有效活动
     * @param drawnCode
     * @return
     */
    public ActivityOrder findValidOrderByDrawncode(String drawnCode){
        List<ActivityOrder> orders=orderDao.findByDrawnCode(drawnCode);
        ActivityOrder order=null;
        for(ActivityOrder o:orders){
            if(!o.isOutDate() && o.getOrderStatus() == ActOrderStatus.reserved){
                order=o;
                break;
            }
        }
        return order;
    }

    /**
     * 变更订单状态
     * @param orderId
     * @param status
     */
    public void changeOrderStatus(String orderId,ActOrderStatus status){
        ActivityOrder order=orderDao.findOne(orderId);
        order.setOrderStatus(status);
        orderDao.save(order);
    }

    /**
     * 按照座位码查询有效订单
     * @param seatCode
     * @return
     */
    public ActivityOrder findValidOrderBySeatCode(String seatCode){
        List<ActivityOrder> orders = orderDao.findBySeatsCode(seatCode);
        ActivityOrder activityOrder = null;
        for (ActivityOrder order:orders){
            for (Seat seat1 :order.getSeats()){
                if (seat1.getCode().equals(seatCode)){
                    activityOrder = order;
                    break;
                }
            }
        }
        return activityOrder;
    }

    /**
     *  验票
     * @param seatCode
     * @return
     */
    public ActivityOrder checkSeat(String seatCode){
        ActivityOrder order=this.findValidOrderBySeatCode(seatCode);
        if(order==null) return null;
        for (Seat seat1 :order.getSeats()){
            if (seat1.getCode().equals(seatCode)){
                seat1.setUsed(true);
                break;
            }
        }
        orderDao.save(order);
        return order;
    }

    /**
     *  取消订单信息
     * @param orderId
     * @param log
     */
    public void cancelOrder(String orderId, CancelLog log){
        ActivityOrder order=orderDao.findOne(orderId);
        order.setOrderStatus(ActOrderStatus.canceled);
        order.setCancelLog(log);
        // 释放订单信息
        if (order.getSeats() != null && order.getSeats().size()>0){
            List<String> seatNos = new ArrayList<>();
            order.getSeats().forEach(seat -> {
                seatNos.add(seat.getSeatNo());
            });
            //释放座位
            StringRedisTemplate redisTemplate=SpringContextUtil.getBean(StringRedisTemplate.class);
            String key="activity-ticket-"+order.getActivityId()+"-itm-"+order.getItmId();
            redisTemplate.opsForList().leftPushAll(key,seatNos);
        }
        orderDao.save(order);
    }

    /**
     *  添加问卷
     * @param id
     * @param research
     */
    public void addResearch(String id, Research research){
        Activity activity = activityDao.findOne(id);
        if (activity == null) {
            throw new BsException("活动不存在");
        }
        activity.setResearch(research);
        activityDao.save(activity);
    }

    /**
     * 删除活动问卷
     * @param id
     */
    public void removeResearch(String id){
        Activity activity = activityDao.findOne(id);
        if (activity == null) {
            throw new BsException("活动不存在");
        }
        activity.setResearch(null);
        activityDao.save(activity);
    }

    /**
     *  添加活动数字资源
     * @param id
     * @param digitInfo
     * @return
     */
    public String createDigital(String id,DigitInfo digitInfo){
        Activity activity = activityDao.findOne(id);
        if (activity == null) {
            throw new BsException("活动不存在");
        }
        String did = UUID.randomUUID().toString().replace("-","");
        digitInfo.setId(did);
        List<DigitInfo> digitInfos = activity.getDigits();
        if (digitInfos == null){
            digitInfos = new ArrayList<>();
            activity.setDigits(digitInfos);
        }
        digitInfos.add(digitInfo);
        activityDao.save(activity);
        return digitInfo.getId();
    }

    /**
     *  获取数字资源列表
     * @param id
     * @return
     */
    public List<DigitInfo> getDigitInfos(String id){
        Activity activity = activityDao.findOne(id);
        if (activity == null) {
            throw new BsException("活动不存在");
        }
        return activity.getDigits();
    }

    /**
     *  查看数字资源详情
     * @param id
     * @param did
     * @return
     */
    public DigitInfo getOneDigitInfo(String id,String did){
        Activity activity = activityDao.findOne(id);
        DigitInfo digitInfo = null;
        if (activity == null) {
            throw new BsException("活动不存在");
        }
        List<DigitInfo> digitInfos = activity.getDigits();
        if (digitInfos != null && digitInfos.size()>0){
            for (DigitInfo info:digitInfos){
                if (info.getId().equals(did)){
                    digitInfo =info ;
                    break;
                }
            }
        }else{
            throw new BsException("该数字资源不存在");
        }
        return digitInfo;
    }

    /**
     *  删除数字资源
     * @param id
     * @param did
     */
    public void deleteDigitInfo(String id,String did){
        Activity activity = activityDao.findOne(id);
        if (activity == null) {
            throw new BsException("活动不存在");
        }
        List<DigitInfo> digitInfos = activity.getDigits();
        if (digitInfos != null && digitInfos.size()>0){
           for (DigitInfo digitInfo:digitInfos){
               if (digitInfo.getId().equals(did)){
                   digitInfos.remove(digitInfo);
                   break;
               }
           }
        }else{
            throw new BsException("该数字资源不存在");
        }
        activityDao.save(activity);
    }

    /**
     *  编辑数字资源
     * @param id
     * @param did
     * @param digitInfo
     */
    public void editDigitalInfo(String id,String did,DigitInfo digitInfo){
        Activity activity = activityDao.findOne(id);
        if (activity == null) {
            throw new BsException("活动不存在");
        }
        List<DigitInfo> digitInfos = activity.getDigits();
        if (digitInfos != null && digitInfos.size()>0){
            for (DigitInfo info:digitInfos){
                if (info.getId().equals(did)){
                    try {
                        CommonUtil.copyNullProperties(digitInfo,info);
                    } catch (Exception e) {
                        throw new BsException("复制出错");
                    }
                    break;
                }
            }
        }else{
            throw new BsException("该数字资源不存在");
        }
        activityDao.save(activity);
    }

    /**
     *  提交问卷
     * @param id
     * @param sheet
     */
    public void submitResearchAnswer(String id, UserResearchSheet sheet){
        Activity activity = activityDao.findOne(id);
        if (activity == null) throw new BsException("活动不存在");
        Research research=activity.getResearch();
        if(research==null) throw new BsException("调查问卷未定义");
        research.addResearchSheet(sheet);
        activityDao.save(activity);
    }

    /**
     * 查找会员问卷
     * @param activityId
     * @param userId
     * @return
     */
    public UserResearchSheet getUserResearchSheet(String activityId,String userId){
        Activity activity = activityDao.findOne(activityId);
        if (activity == null) throw new BsException("活动不存在");
        Research research=activity.getResearch();
        if(research==null) throw new BsException("问卷不存在");
        List<UserResearchSheet> sheets=Optional.ofNullable(research.getSheets()).orElse(new ArrayList<>());
        for(UserResearchSheet sheet:sheets){
            if(sheet.getUserId().equals(userId)) return sheet;
        }
        throw new BsException("问卷不存在");
    }

    /**
     *  查询问卷答案列表
     * @param id
     * @return
     */
    public List<UserResearchSheet> findAnswers(String id){
        Activity activity = activityDao.findOne(id);
        if (activity == null) throw new BsException("活动不存在");
        return activity.getResearch().getSheets();
    }
    /**
     * 扫描黑名单用户
     */
    public void scanBlackUser(){
        String scope=Optional.ofNullable(SpringContextUtil.getSysParam(this.blackScopeParamName)).orElse("1");
        String times=Optional.ofNullable(SpringContextUtil.getSysParam(this.blackTimesParamName)).orElse("2");
        String limit=Optional.ofNullable(SpringContextUtil.getSysParam(this.blackLimitParamName)).orElse("30");
        QActivityOrder query=QActivityOrder.activityOrder;
        LocalDate startDate=LocalDate.now().minusMonths(Integer.parseInt(scope));
        LocalDate endDate=LocalDate.now().minusDays(1);
        Map<String,Integer> userTimes=new HashMap<>();  //保存会员预定未参加的次数
        //查询规则时间范围内的订单并处理
        orderDao.findAll(query.itmDate.after(startDate).and(query.itmDate.before(endDate))).forEach(order->{
            if(order.getOrderStatus()!= ActOrderStatus.canceled ||order.getReserveType()!= ReserveType.none){ //已经取消和无需订票的订单不处理
                String userId=order.getUserId();
                User user=userInfoService.getUserById(userId);
                if(user!=null && !user.isLimit(PermitLimitType.notReserve)){ //已经限制预定的会员不处理
                    boolean pass=false;  //是否已参加
                    if(order.getOrderStatus()== ActOrderStatus.drawn) pass=true; //如果已出票可视为参加
                    if(order.isAllChecked()) pass=true; //如果所有座位票都已检票视为参加
                    if(!pass) { //如果未参加，次数加1
                        Integer count=userTimes.getOrDefault(userId,1);
                        userTimes.put(userId,count+1);
                    }
                }
            }
        });
        userTimes.forEach((userid,count)->{
            User user=userInfoService.getUserById(userid);
            if(count>=Integer.parseInt(times)){ //满足预定义规则则限制会员
                userActionService.limitUser(PermitLimitType.notReserve,userid,Integer.parseInt(limit), LimitReasonType.activityNotSign);
            }
        });
    }


    /**
     * 发布座位票
     */
    public void publishTickets() {
        StringRedisTemplate redisTemplate = SpringContextUtil.getBean(StringRedisTemplate.class);
        QActivity query = QActivity.activity;
        Predicate predicate = query.signStartTime.after(LocalDateTime.now().minusHours(24))
                .and(query.signStartTime.before(LocalDateTime.now())
                .and(query.onlineStatus.eq(OnlineStatus.Published)));
        activityDao.findAll(predicate).forEach(act -> {
            List<ActivityItm> itms = act.getItms();
            if (itms != null) {
                for (ActivityItm itm : itms) {
                    String key = "activity-ticket-" + act.getId() + "-itm-" + itm.getId();
                    if (redisTemplate.opsForValue().setIfAbsent(key + "look", "1")) {
                        List<String> tickets = new ArrayList<>();
                        if (act.getReserveType() == ReserveType.free) {
                            int total = act.getTotalAllow();
                            for (int i = 1; i < total + 1; i++) {
                                tickets.add(i + "号");
                            }
                        } else if (act.getReserveType() == ReserveType.online) {
                            List<Grid> seats = act.allSeats();
                            tickets = seats.stream().map(seat -> seat.getSeatNo()).collect(Collectors.toList());
                        } else break;
                        redisTemplate.opsForList().leftPushAll(key, tickets);
                        itm.setLeftTicketSum(tickets.size()); //设置场次余票数量
                        activityDao.save(act);
                    }
                }
            }
        });
    }

    public int queryCountUserOrder(String actId,String userId,String itmId) {
        QActivityOrder qto = QActivityOrder.activityOrder;
        List<ActivityOrder> list=new ArrayList<>();
        this.orderDao.findAll(qto.userId.eq(userId).and(qto.itmId.eq(itmId)).and(qto.activityId.eq(actId))).forEach(order->list.add(order));
        int count = 0 ;
        if(list!=null && list.size() > 0) {
            for(ActivityOrder order : list) {
                count += order.getReserveSum() ;
            }
        }
        return count ;
    }

}
