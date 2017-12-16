package com.creatoo.szwhg.train.service;

import com.creatoo.szwhg.base.service.FileService;
import com.creatoo.szwhg.core.exception.BsException;
import com.creatoo.szwhg.core.model.*;
import com.creatoo.szwhg.core.util.CommonUtil;
import com.creatoo.szwhg.core.util.GeneralPredicatesBuilder;
import com.creatoo.szwhg.core.util.SpringContextUtil;
import com.creatoo.szwhg.train.dao.TrainDao;
import com.creatoo.szwhg.train.dao.TrainOrderDao;
import com.creatoo.szwhg.train.model.QTrainOrder;
import com.creatoo.szwhg.train.model.Train;
import com.creatoo.szwhg.user.model.LimitReasonType;
import com.creatoo.szwhg.user.model.PermitLimitType;
import com.creatoo.szwhg.user.model.User;
import com.creatoo.szwhg.user.service.UserActionService;
import com.creatoo.szwhg.user.service.UserInfoService;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 培训服务
 * Created by wangxl on 2017/9/5.
 */
@Service
public class TrainService {
    @Autowired
    private FileService fileService;

    @Autowired
    private TrainDao trainDao;
    @Autowired
    private TrainOrderDao orderDao;

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserActionService userActionService;

    @Value("${comment.audit.isopen}")
    private Boolean auditIsopen;

    private String blackScopeParamName="user.black.train.scope";  //黑名单规则参数名，时间范围，如一个月
    private String blackTimesParamName="user.black.train.times";   //黑名单规则参数名，报名未参加的次数
    private String blackLimitParamName="user.black.train.limit";   //黑名单规则参数名，需要限制的时间，单位天

    /**
     * 添加培训
     * @param train 培训实体
     * @return 培训唯一标识
     */
    public String add(Train train){
        //保存数据
        this.trainDao.save(train);
        //返回培训标识
        return train.getId();
    }

    /**
     * 编辑培训
     * @param train
     */
    public void edit(String id, Train train){
        Train oldTrain = this.trainDao.findOne(id);
        if(oldTrain == null){
            throw new BsException("培训不存在");
        }
        try {
            CommonUtil.copyNullProperties(train, oldTrain);
        } catch (Exception e) {
            throw new BsException("复制出错");
        }

        this.trainDao.save(oldTrain);
    }

    /**
     * 删除培训
     * @param id
     */
    public void delete(String id){
        this.trainDao.delete(id);
    }

    /**
     * 修改培训上线状态
     * @param id 培训标识
     * @param flowLog 状态信息
     */
    public void changeOnlineStatus(String id, FlowLog flowLog){
        Train train = this.trainDao.findOne(id);
        if(train != null) {
            List<FlowLog> fls = train.getFlowLogs();
            if (fls == null) {
                fls = new ArrayList<>();
            }
            fls.add(flowLog);
            train.setFlowLogs(fls);
            OnlineStatus toStatus=OnlineStatus.valueOf(flowLog.getToStatus());
            train.setOnlineStatus(toStatus);
            this.trainDao.save(train);
        }
    }

    /**
     * 推荐
     * @param id 推荐
     * @param recommend true-推荐 false-不推荐
     */
    public void doRecommend(String id, Boolean recommend){
        Train train = this.trainDao.findOne(id);
        if(train != null) {
            train.setIsRecommend(recommend);
            this.trainDao.save(train);
        }
    }

    /**
     * 查询培训详情
     * @param id 培训标识
     * @return Train
     */
    public Train findById(String id){
        return this.trainDao.findOne(id);
    }

    /**
     * 查询培训列表(分页)
     * @param search 查询条件
     * @param pageable 分页信息
     * @return Page<Train>
     */
    public Page<Train> findAll(String search, Pageable pageable){
        GeneralPredicatesBuilder builder = new GeneralPredicatesBuilder(Train.class);
        BooleanExpression exp = builder.build(search);
        return this.trainDao.findAll(exp, pageable);
    }

    /**
     * 查询培训列表
     * @param search 查询条件
     * @return List<Train>
     */
    public List<Train> findAll(String search){
        GeneralPredicatesBuilder builder = new GeneralPredicatesBuilder(Train.class);
        BooleanExpression exp=builder.build(search);
        Iterable<Train> its = this.trainDao.findAll(exp);

        List<Train> list = new ArrayList<>();

        if(its != null){
            Iterator<Train> it = its.iterator();
            while(it.hasNext()){
                list.add(it.next());
            }
        }
        return list;
    }

    public String  addComment(String trainId,Comment comment){
        Train train=trainDao.findOne(trainId);
        if(train==null) throw new BsException("");
        List<Comment> comments=train.getComments();
        if (comments == null) {
            comments = new ArrayList<>();
            train.setComments(comments);
        }
        String commentid=UUID.randomUUID().toString();
        comment.setId(commentid);
        comment.setTime(LocalDateTime.now());
        comments.add(comment);
        if (auditIsopen){
            comment.setStatus(CommentStatus.Wait);
        }else{
            comment.setStatus(CommentStatus.Pass);
        }
        trainDao.save(train);
        return commentid;
    }

    public void deleteComment(String trainId,String commentid){
        Train train=trainDao.findOne(trainId);
        Optional.ofNullable(train).map(t->t.getComments()).orElse(new ArrayList<>())
                .stream().filter(c->c.getId().equals(commentid)).findFirst().ifPresent(comment->{
                    train.getComments().remove(comment);
                    trainDao.save(train);
        });

    }

    public void auditComment(String traintid,String commentid,CommentStatus commentStatus){
        Train train=trainDao.findOne(traintid);
        Optional.ofNullable(train).map(t->t.getComments()).orElse(new ArrayList<>())
                .stream().filter(c->c.getId().equals(commentid)).findFirst().ifPresent(comment->{
                    comment.setStatus(commentStatus);
                    trainDao.save(train);
        });
    }

    public Page<Comment> findAllComments(String id,Pageable pageable){
        Train train = trainDao.findOne(id);
        List<Comment> comments =train.getComments();
        List<Comment> commentList = new ArrayList<>();
        int total = 0;
        if (comments != null && comments.size()>0) {
            int start = pageable.getOffset();
            int end = (start + pageable.getPageSize()) > comments.size() ? comments.size() : (start + pageable.getPageSize());
            if (comments != null && comments.size() > 0 && end > start) {
                comments.sort((o1, o2) -> {
                    return o2.getTime().compareTo(o1.getTime());
                });
                commentList = comments.subList(start, end);
                total = comments.size();
            }
        }
        return new PageImpl<Comment>(commentList,pageable,total);
    }

    /**
     * 定时发布预定库存，每隔30秒执行一次发送任务
     */
    @Scheduled(initialDelay=1000*10,fixedDelay = 1000*30)
    public void publishRollTickets() {
        String ntime=LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String ltime=LocalDateTime.now().minusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        StringRedisTemplate redisTemplate= SpringContextUtil.getBean(StringRedisTemplate.class);
        Pageable page=new PageRequest(0,Integer.MAX_VALUE);
        String trainSearch="onlineStatus:Published,enrolStartTime<"+ntime+",enrolStartTime>"+ltime;
        List<Train> trains=findAll(trainSearch);
        for(Train train:trains){
            String key="train-enroll-"+train.getId();
            if(redisTemplate.opsForValue().setIfAbsent(key+"look","1")) {
                List<String> enrolls=new ArrayList<>();
                int total=train.getAllLimitPeoples(); //允许总数
                int hasEnroll=Optional.ofNullable(train.getEnrollSum()).orElse(0); //已报名人数
                for(int i=1;i<total-hasEnroll+1;i++){
                    enrolls.add(i+"号");
                }
                redisTemplate.opsForList().leftPushAll(key,enrolls);
            }
        }
    }

    /**
     * 黑名单定时检查
     */
    public void scanBlackUser() {
        String scope = Optional.ofNullable(SpringContextUtil.getSysParam(this.blackScopeParamName)).orElse("1");
        String times = Optional.ofNullable(SpringContextUtil.getSysParam(this.blackTimesParamName)).orElse("2");
        String limit = Optional.ofNullable(SpringContextUtil.getSysParam(this.blackLimitParamName)).orElse("30");
        QTrainOrder query = QTrainOrder.trainOrder;
        LocalDate startDate = LocalDate.now().minusMonths(Integer.parseInt(scope));
        LocalDate endDate = LocalDate.now().minusDays(1);
        Map<String, Integer> userTimes = new HashMap<>();  //保存会员预定未参加的次数
        //查询规则时间范围内的订单并处理
        orderDao.findAll(query.trainEndTime.after(startDate).and(query.trainEndTime.before(endDate))).forEach(order -> {
            if (order.getOrderStatus() == TrainOrderStatus.Success ) { //只处理已经报名成功的订单
                String userId = order.getUserId();
                User user = userInfoService.getUserById(userId);
                if (user != null && !user.isLimit(PermitLimitType.notReserve)) { //已经限制预定的会员不处理
                    boolean pass = false;  //是否已参加
                    if (order.isAllChecked()) pass = true; //如果所有课程都已签到视为参加
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
                userActionService.limitUser(PermitLimitType.notReserve, userid, Integer.parseInt(limit), LimitReasonType.trainNotSign);
            }
        });
    }
}
