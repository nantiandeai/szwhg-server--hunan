package com.creatoo.szwhg.base.service;

import com.creatoo.szwhg.base.dao.ManagerDao;
import com.creatoo.szwhg.base.model.*;
import com.creatoo.szwhg.core.exception.BsException;
import com.creatoo.szwhg.core.util.CommonUtil;
import com.creatoo.szwhg.core.util.GeneralPredicatesBuilder;
import com.creatoo.szwhg.core.util.SpringContextUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 管理员管理
 * Created by yunyan on 2017/8/9.
 */
@Service
@Slf4j
public class ManagerService {
    @Autowired
    private ManagerDao managerDao;
    @Autowired
    private MenuService menuService;
    @Autowired
    private UnitService unitService;


    public  String createManager(Manager manager){
        if(managerDao.findFirstByUsername(manager.getUsername())!=null) throw new BsException("登录账号重复");
        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
        manager.setPassword(encoder.encode(SpringContextUtil.getSysParam(SysParam.AUTH_PASSWORD)));
        managerDao.save(manager);
        log.info("成功创建用户："+manager.getUsername());
        return manager.getId();
    }

    public void modifyManager(String username,Manager manager){
        Manager pmager=managerDao.findFirstByUsername(username);
        Optional<Manager> optional=Optional.ofNullable(pmager);
        optional.map(p-> {
            CommonUtil.copyNullProperties(manager,p);
            return p;
        }).orElseThrow(()->new BsException("找不到对应账号"));
        managerDao.save(optional.get());
    }

    public void modifyRoles(String username,List<String> roleids){
        Manager pmager=managerDao.findFirstByUsername(username);
        if(pmager==null) throw new BsException("找不到对应账号");
        List<Role> roles=new ArrayList<>();
        if(roleids==null) roleids=new ArrayList<>();
        roleids.forEach(roleid->{
            Role role=new Role();
            role.setId(roleid);
            roles.add(role);
        });
        pmager.setRoles(roles);
        managerDao.save(pmager);
    }

    public Manager getManagerById(String id){
        Manager manager=managerDao.findOne(id);
        manager.buildOtherProperties();
        return manager;
    }

    public void deleteManager(String username){
        Manager manager=managerDao.findFirstByUsername(username);
        if(manager==null) return;
        if(username.equals(SpringContextUtil.getSysParam(SysParam.AUTH_ADMIN_NAME))) throw new BsException("不允許刪除超級管理員");
        managerDao.delete(manager);
    }

    public Manager getManagerByUsername(String username){
        Manager manager=managerDao.findFirstByUsername(username);
        return Optional.ofNullable(manager).map(o->{o.buildOtherProperties();return o;}).orElse(null);
    }

    public Page<Manager> findAll(String unitId,String search, Pageable pageable){
        GeneralPredicatesBuilder builder=new GeneralPredicatesBuilder(Manager.class);
        BooleanExpression exp=builder.build(search);
        List<Unit> depUnits=unitService.getAllDepChildren(unitId);  //获取所有子级部门
        PathBuilder entityPath = new PathBuilder(Manager.class, Manager.class.getName()).get("unit",Unit.class).get("id");
        List<String> uids=depUnits.stream().map(unit->unit.getId()).collect(Collectors.toList()); //所有子部门id
        uids.add(unitId);//加上根节点
        BooleanExpression plusExp=null; //加上子部门的筛选条件
        for(String uid:uids){
            BooleanExpression texp=entityPath.eq(uid);
            if(plusExp==null) plusExp=texp;
            else plusExp=plusExp.or(texp);
        }
        if(exp==null) exp=plusExp;
        else exp=exp.and(plusExp);
        Page<Manager> managers=managerDao.findAll(exp,pageable);
        managers.forEach(manager -> manager.buildOtherProperties());
        return managers;
    }

    public Page<Manager> findAll(String search, Pageable pageable){
        GeneralPredicatesBuilder builder=new GeneralPredicatesBuilder(Manager.class);
        BooleanExpression exp=builder.build(search);
        Page<Manager> managers=managerDao.findAll(exp,pageable);
        managers.forEach(manager -> manager.buildOtherProperties());
        return managers;
    }

    public List<Menu> getManagerMenuCodes(String username){
        if(SpringContextUtil.getSysParam(SysParam.AUTH_ADMIN_NAME).equals(username)){
            return menuService.getAllMenus();
        }
        Manager manager=managerDao.findFirstByUsername(username);
        List<String> codes=new ArrayList<>();
        Set<String> sets = new HashSet<>();
        manager.getRoles().forEach(role->{
            sets.addAll(role.getGrants().stream().map(grant->grant.getResourceCode()).collect(Collectors.toList()));
        });
        codes.addAll(sets);
        List<Menu> menus = menuService.findByCodeIn(codes);
        return menus;
    }

    /**
     * 用户根据旧密码修改为新密码
     * @param username
     * @param newPwd
     */
    public void changePwdWithOld(String username, String newPwd) {
        Manager user = managerDao.findFirstByUsername(username);
        if (user == null) throw new BsException("用户信息不存在");
        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
//        if(!encoder.matches(oldpwd,user.getPassword())) throw new BsException("旧密码不正确");
        if (StringUtils.isBlank(newPwd.trim())) throw new BsException("新密码不能为空");
        user.setPassword(encoder.encode(newPwd));
        managerDao.save(user);
    }
}
