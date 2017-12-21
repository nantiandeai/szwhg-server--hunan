package com.creatoo.szwhg.venue.service;

import com.creatoo.szwhg.base.model.Comment;
import com.creatoo.szwhg.base.model.CommentStatus;
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
import com.creatoo.szwhg.venue.dao.RoomOrderDao;
import com.creatoo.szwhg.venue.dao.VenueDao;
import com.creatoo.szwhg.venue.dao.VenueRoomDao;
import com.creatoo.szwhg.venue.model.*;
import com.querydsl.core.types.dsl.BooleanExpression;
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

/**
 * 场馆服务
 * Created by yunyan on 2017/8/16.
 */
@Service
public class VenueService {
    @Autowired
    private VenueDao venueDao;
    @Autowired
    private VenueRoomDao roomDao;
    @Autowired
    private RoomOrderDao orderDao;
    @Autowired
    private FileService fileService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserActionService userActionService;

    @Value("${comment.audit.isopen}")
    private Boolean auditIsopen;

    private String blackScopeParamName="user.black.venRoom.scope";  //黑名单规则参数名，时间范围，如一个月
    private String blackTimesParamName="user.black.venRoom.times";   //黑名单规则参数名，报名未参加的次数
    private String blackLimitParamName="user.black.venRoom.limit";   //黑名单规则参数名，需要限制的时间，单位天

    /**
     * 创建场馆
     * @param venue
     * @return
     */
    public String createVenue(Venue venue){
        venueDao.save(venue);
        return venue.getId();
    }

    public Page<Venue> findAll(String search, Pageable pageable){
        GeneralPredicatesBuilder builder=new GeneralPredicatesBuilder(Venue.class);
        BooleanExpression exp=builder.build(search);
        return venueDao.findAll(exp,pageable);
    }

    public Venue getById(String id){
        return venueDao.findOne(id);
    }

    /**
     * 创建场馆活动室
     * @param room
     * @return
     */
    public String createRoom(VenueRoom room){
        room.setOnlineStatus(OnlineStatus.WaitCommit.name());
        roomDao.save(room);
        return room.getId();
    }

    /**
     * 查询活动室列表
     * @param search
     * @param pageable
     * @return
     */
    public Page<VenueRoom> findAllRooms(String search, Pageable pageable){
        GeneralPredicatesBuilder builder=new GeneralPredicatesBuilder(VenueRoom.class);
        BooleanExpression exp=builder.build(search);
        return roomDao.findAll(exp,pageable);
    }

    /**
     * 获得活动室场次信息
     * @param id 活动室id
     * @return
     */
    public List<RoomItm> getRoomItms(String id) {
        List<RoomItm> roomItms = new ArrayList<>();
        VenueRoom venueRooms = roomDao.findOne(id);
        if (venueRooms == null) throw new BsException("活动室不存在");
        // 获得活动场次定义
        RoomItmDef roomItmDef = venueRooms.getItmDef();
        if (roomItmDef == null) return roomItms;
        if (!roomItmDef.getDisEnable()) throw new BsException("该活动室场次不允许预定");
        // 获得规则参数
        int reserveStartDays = Integer.valueOf(SpringContextUtil.getSysParam("reserveStartDays"));
        int limitDays = Integer.valueOf(SpringContextUtil.getSysParam("limitDays"));
        // 获得周场次
        List<WeekRule> weekRules = roomItmDef.getRules();
        weekRules.sort(Comparator.comparing(WeekRule::getEffectiveDate));
        // 限制天数
        LocalDate limitDay = LocalDate.now().plusDays(limitDays);
        // 后续可以添加往后多少天开始的规则，目前从当前日期开始
        LocalDate ndate = null;
        if (reserveStartDays == 0) {
            ndate = LocalDate.now();
        } else {
            ndate = LocalDate.now().plusDays(reserveStartDays);
        }
        if (weekRules.size() > 0) {
            List<LocalDate> localDates = new ArrayList<>();
            // 排序放入list中，后面对比需要
            for (WeekRule weekRule : weekRules) {
                if (weekRule.getEffectiveDate().isBefore(limitDay) || weekRule.getEffectiveDate().equals(limitDay)) {
                    localDates.add(weekRule.getEffectiveDate());
                } else {
                    localDates.add(limitDay);
                    break;
                }
            }
            if (!localDates.contains(limitDay)) localDates.add(limitDay);
            int i = 1;
            for (WeekRule weekRule : weekRules) {
                // 获得场次列表
                if (weekRule.getEffectiveDate().isBefore(limitDay) || weekRule.getEffectiveDate().equals(limitDay)) {
                    List<WeekDayDef> dayItms = weekRule.getDayItms();
                    if (dayItms != null && dayItms.size() > 0) {
                        if (ndate.isBefore(localDates.get(i - 1)) || ndate.equals(localDates.get(i - 1)))
                            ndate = localDates.get(i - 1);
                        for (WeekDayDef weekDayDef : dayItms) {
                            LocalDate wdate = ndate;
                            while (wdate.isBefore(localDates.get(i)) || wdate.equals(localDates.get(i))) {
                                if (wdate.getDayOfWeek().toString().equals(weekDayDef.getWeekDay().toString().toUpperCase())) {
                                    RoomItm roomItm = new RoomItm();
                                    roomItm.setItmDate(wdate);
                                    roomItm.setItmStarttime(weekDayDef.getStartTime());
                                    roomItm.setItmEndtime(weekDayDef.getEndTime());
                                    roomItms.add(roomItm);
                                }
                                wdate = wdate.plusDays(1);
                            }
                        }
                    }
                }
                i++;
            }
        }

        // 例外场次
        List<RoomItm> removeRooitm = new ArrayList<>();
        List<ExceptDayDef> exceptItms = roomItmDef.getExceptItms();
        if (exceptItms != null && exceptItms.size() > 0) {
            for (ExceptDayDef exceptDayDef : exceptItms) {
                // 判断例外场次是否有与周场次重叠，有则以例外场次为准
                for (RoomItm roomItm : roomItms) {
                    if (roomItm.getItmDate().equals(exceptDayDef.getDate())) {
                        removeRooitm.add(roomItm);
                    }
                }
            }
            if (removeRooitm.size() > 0) {
                for (RoomItm roomItm : removeRooitm) {
                    roomItms.remove(roomItm);
                }
            }

            for (ExceptDayDef exceptDayDef : exceptItms) {
                if (exceptDayDef.getDate().isBefore(limitDay) || exceptDayDef.getDate().equals(limitDay)) {
                    for (TimeSection timeSection : exceptDayDef.getTimes()) {
                        RoomItm roomItm = new RoomItm();
                        roomItm.setItmDate(exceptDayDef.getDate());
                        roomItm.setItmStarttime(timeSection.getStartTime());
                        roomItm.setItmEndtime(timeSection.getEndTime());
                        roomItms.add(roomItm);
                    }
                }
            }
        }

        // 判断是否已被预定
        List<RoomOrder> roomOrders = orderDao.findByStatusOrStatus(RoomOrderStatus.created.name(), RoomOrderStatus.success.name());
        if (roomOrders != null && roomOrders.size() > 0) {
            for (RoomOrder roomOrder : roomOrders) {
                if (roomOrder.getItms() != null && roomOrder.getItms().size() > 0) {
                    for (RoomItm roomItm : roomItms) {
                        for (OrderItm orderItm : roomOrder.getItms()) {
                            if (orderItm.getItmDate().equals(roomItm.getItmDate()) && orderItm.getItmStarttime().equals(roomItm.getItmStarttime())) {
                                roomItm.setIsReserve(true);
                            }
                        }
                    }
                }
            }
        }
        roomItms.sort(Comparator.comparing(RoomItm::getItmDate));
        return roomItms;
    }

    /**
     * 场馆编辑
     * @param id 场馆id
     * @param venue
     */
    public void modifyVenue(String id, Venue venue) {
        Venue pvenue=venueDao.findOne(id);
        try {
            CommonUtil.copyNullProperties(venue,pvenue);
        } catch (Exception e) {
            throw new BsException("");
        }
        venueDao.save(pvenue);
    }

    /**
     * 删除场馆
     * @param id 场馆id
     */
    public void deleteVenue(String id){
        venueDao.delete(id);
    }

    /**
     * 活动室编辑
     * @param roomid 活动室id
     * @param room
     */
    public void modifyRoom(String roomid, VenueRoom room) {
        VenueRoom proom=roomDao.findOne(roomid);
        try {
            CommonUtil.copyNullProperties(room,proom);
        } catch (Exception e) {
            throw new BsException("");
        }
        roomDao.save(proom);
    }

    /**
     * 编辑活动室场次定义
     * @param roomid 活动室id
     * @param def
     */
    public void modifyRommItm(String roomid, RoomItmDef def){
        VenueRoom proom=roomDao.findOne(roomid);
        proom.setItmDef(def);
        roomDao.save(proom);
    }

    /**
     * 查询活动室详情
     * @param roomid 活动室id
     * @return
     */
    public VenueRoom getRoomById(String roomid) {
        return roomDao.findOne(roomid);
    }

    /**
     *  变更活动室在线状态
     * @param roomid 活动室id
     * @param oper
     */
    public void changeRoomOnlineStatus(String roomid,FlowLog oper){
        VenueRoom proom=roomDao.findOne(roomid);
        OnlineStatus toStatus=OnlineStatus.valueOf(oper.getToStatus());
        proom.setOnlineStatus(toStatus.name());
        List<FlowLog> flowLogs = proom.getFlowLogs();
        if (flowLogs == null) {
            flowLogs = new ArrayList<>();
            proom.setFlowLogs(flowLogs);
        }
        flowLogs.add(oper);
        roomDao.save(proom);
    }

    /**
     * 删除活动室
     * @param roomid 活动室id
     */
    public void deleteRoom(String roomid) {
        roomDao.delete(roomid);
    }

    /**
     *  创建活动室订单
     * @param order
     * @return
     */
    public String createRoomOrder(RoomOrder order) {
        String roomid = order.getRoomId();
        VenueRoom proom = roomDao.findOne(roomid);
        String bsnType = OrderType.venueroom.getCode();
        order.setBsnType(bsnType);
        String orderCode = bsnType + CommonUtil.generateOrderCode();
        order.setOrderCode(orderCode);
        order.setRoomId(proom.getId());
        order.setRoomName(proom.getName());
        order.setStatus(RoomOrderStatus.created);
        order.setVenueName(proom.getVenue().getName());
        List<OrderItm> itms = order.getItms();
        if (itms == null || itms.size() == 0) throw new BsException("必须选择场次");
        int seqno = 1;
        for (OrderItm itm : itms) {
            String id = Integer.toString(seqno++);
            itm.setId(id);
        }
        if (proom.getVenue() != null) order.setAddress(proom.getVenue().getAddress());
        orderDao.save(order);
        return order.getId();
    }

    /**
     * 查询活动室订单列表
     * @param search
     * @param pageable
     * @return
     */
    public Page<RoomOrder> findAllOrder(String search, Pageable pageable) {
        GeneralPredicatesBuilder builder=new GeneralPredicatesBuilder(RoomOrder.class);
        BooleanExpression exp=builder.build(search);
        return orderDao.findAll(exp,pageable);
    }

    /**
     * 查询活动室历史订单
     * @param id 用户id
     * @param pageable
     * @return
     */
    public Page<RoomOrder> findHistoryOrder(String id, Pageable pageable) {
        List<RoomOrder> roomOrders = orderDao.findByUserId(id);
        List<RoomOrder> list = new ArrayList<>();
        roomOrders.forEach(roomOrder -> {
            if (roomOrder.getHistoryRoomOrder()) {
                VenueRoom venueRoom = getRoomById(roomOrder.getRoomId());
                if (venueRoom != null) roomOrder.setTelePhone(venueRoom.getTelephone());
                list.add(roomOrder);
            }
        });
        int total = 0;
        List<RoomOrder> content = new ArrayList<>();
        if (list.size() > 0) {
            int start = pageable.getOffset();
            int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
            if (list != null && list.size() > 0 && end > start) {
                content = list.subList(start, end);
                total = list.size();
            }
        }
        return new PageImpl<>(content, pageable, total);
    }

    /**
     * 查询活动室当前订单
     * @param id 用户id
     * @param pageable
     * @return
     */
    public Page<RoomOrder> findCurrentOrder(String id, Pageable pageable) {
        List<RoomOrder> roomOrders = orderDao.findByUserId(id);
        List<RoomOrder> list = new ArrayList<>();
        roomOrders.forEach(roomOrder -> {
            if (!roomOrder.getHistoryRoomOrder()) {
                VenueRoom venueRoom = getRoomById(roomOrder.getRoomId());
                if (venueRoom != null) roomOrder.setTelePhone(venueRoom.getTelephone());
                list.add(roomOrder);
            }
        });
        int total = 0;
        List<RoomOrder> content = new ArrayList<>();
        if (list.size() > 0) {
            int start = pageable.getOffset();
            int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
            if (list != null && list.size() > 0 && end > start) {
                content = list.subList(start, end);
                total = list.size();
            }
        }
        return new PageImpl<>(content, pageable, total);
    }

    /**
     * 查询活动室订单详情
     * @param orderid 订单id
     * @return
     */
    public RoomOrder getOrderById(String orderid) {
        return orderDao.findOne(orderid);
    }

    /**
     * 取消订单
     * @param orderid 订单id
     * @param log
     */
    public void cancelOrder(String orderid, CancelLog log) {
        RoomOrder order=orderDao.findOne(orderid);
        order.setStatus(RoomOrderStatus.cancel);
        order.setCancelLog(log);
        orderDao.save(order);
    }

    /**
     * 通过订单
     * @param orderId 订单id
     */
    public void passOrder(String orderId){
        RoomOrder order=orderDao.findOne(orderId);
        // 验票码
        String drawnCode=CommonUtil.generateDrawnCode();
        order.setStatus(RoomOrderStatus.success);
        order.setCode(drawnCode);
        orderDao.save(order);
    }

    /**
     * 查询验票码订单信息
     * @param code 验票码
     * @return
     */
    public RoomOrder findOrderByValidCode(String code) {
        List<RoomOrder> orders = orderDao.findByCode(code);
        RoomOrder target = null;
        for (RoomOrder order : orders) {
            if (!order.isOutDate() && order.getStatus().equals(RoomOrderStatus.success)) {
                target = order;
                break;
            }
        }
        return target;
    }

    /**
     * 验票
     * @param code 验票码
     * @return
     */
    public RoomOrder checkCode(String code) {
        RoomOrder order = this.findOrderByValidCode(code);
        if (order == null) return null;
        order.setHasChecked(true);
        orderDao.save(order);
        return order;
    }

    public void rejectOrder(String orderId){
        RoomOrder order=orderDao.findOne(orderId);
        order.setStatus(RoomOrderStatus.reject);
        orderDao.save(order);
    }
    /**
     *  添加数字资源
     * @param id  场馆id
     * @param digitInfo
     * @return
     */
    public String createDigital(String id,DigitInfo digitInfo){
        Venue venue = venueDao.findOne(id);
        if (venue == null) throw new BsException("场馆不存在");
        String did = UUID.randomUUID().toString().replace("-","");
        digitInfo.setId(did);
        List<DigitInfo> digitInfos = venue.getDigitInfos();
        if (digitInfos == null){
            digitInfos = new ArrayList<>();
            venue.setDigitInfos(digitInfos);
        }
        digitInfos.add(digitInfo);
        venueDao.save(venue);
        return digitInfo.getId();
    }

    /**
     *  获取数字资源列表
     * @param id  场馆id
     * @return
     */
    public List<DigitInfo> getDigitInfos(String id){
        Venue venue = venueDao.findOne(id);
        if (venue == null) throw new BsException("场馆不存在");
        return venue.getDigitInfos();
    }

    /**
     *  查看数字资源详情
     * @param id 场馆id
     * @param did
     * @return
     */
    public DigitInfo getOneDigitInfo(String id,String did){
        Venue venue = venueDao.findOne(id);
        DigitInfo digitInfo = null;
        if (venue == null) throw new BsException("场馆不存在");
        List<DigitInfo> digitInfos = venue.getDigitInfos();
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
     * @param id 场馆id
     * @param did
     */
    public void deleteDigitInfo(String id,String did){
        Venue venue = venueDao.findOne(id);
        if (venue == null) throw new BsException("场馆不存在");
        Optional.ofNullable(venue).map(t->t.getDigitInfos()).orElse(new ArrayList<>()).stream().filter(c->c.getId().equals(did)).findFirst().ifPresent(digitInfo -> {
            venue.getDigitInfos().remove(digitInfo);
            venueDao.save(venue);
        });
    }

    /**
     *  编辑数字资源
     * @param id 场馆id
     * @param did 资源id
     * @param digitInfo
     */
    public void editDigitalInfo(String id,String did,DigitInfo digitInfo){
        Venue venue = venueDao.findOne(id);
        if (venue == null) throw new BsException("场馆不存在");
        List<DigitInfo> digitInfos = venue.getDigitInfos();
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
        venueDao.save(venue);
    }

    /**
     * 黑名单定时任务
     */
    public void scanBlackUser() {
        String scope = Optional.ofNullable(SpringContextUtil.getSysParam(this.blackScopeParamName)).orElse("1");
        String times = Optional.ofNullable(SpringContextUtil.getSysParam(this.blackTimesParamName)).orElse("2");
        String limit = Optional.ofNullable(SpringContextUtil.getSysParam(this.blackLimitParamName)).orElse("30");
        QOrderItm query = QOrderItm.orderItm;
        LocalDate startDate = LocalDate.now().minusMonths(Integer.parseInt(scope));
        LocalDate endDate = LocalDate.now().minusDays(1);
        Map<String, Integer> userTimes = new HashMap<>();  //保存会员预定未参加的次数
        //查询规则时间范围内的订单并处理
        orderDao.findAll(query.itmDate.after(startDate).and(query.itmDate.before(endDate))).forEach(order -> {
            if (order.getStatus() == RoomOrderStatus.success) { //只处理审核通过的订单
                String userId = order.getUserId();
                User user = userInfoService.getUserById(userId);
                if (user != null && !user.isLimit(PermitLimitType.notReserve)) { //已经限制预定的会员不处理
                    boolean pass = false;  //是否已参加
                    if (order.getHasChecked()) {
                        pass = true; //如果已验票可视为参加
                    }
                    if (!pass) { //如果未参加，次数加1
                        Integer count = userTimes.getOrDefault(userId, 1);
                        userTimes.put(userId, count + 1);
                    }
                }
            }
        });
        userTimes.forEach((userid, count) -> {
            User user = userInfoService.getUserById(userid);
            if (count >= Integer.parseInt(times)) { //满足预定义规则则限制会员
                userActionService.limitUser(PermitLimitType.notReserve, userid, Integer.parseInt(limit), LimitReasonType.venueNotSign);
            }
        });
    }

}
