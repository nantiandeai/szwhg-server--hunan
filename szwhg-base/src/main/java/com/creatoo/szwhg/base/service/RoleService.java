package com.creatoo.szwhg.base.service;

import com.creatoo.szwhg.base.dao.ManagerDao;
import com.creatoo.szwhg.base.dao.RoleDao;
import com.creatoo.szwhg.base.model.Grant;
import com.creatoo.szwhg.base.model.Manager;
import com.creatoo.szwhg.base.model.Role;
import com.creatoo.szwhg.core.exception.BsException;
import com.creatoo.szwhg.core.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色资源服务
 * Created by yunyan on 2017/8/3.
 */
@Service
public class RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private ManagerDao managerDao;
    @Autowired
    private MenuService menuService;

    public String createRole(Role role){
        if(roleDao.findByNameAndUnitId(role.getName(),role.getUnit().getId())!=null) throw new BsException("角色名重复");
        roleDao.save(role);
        return role.getId();
    }

    public List<Role> getRolesByUnit(String unitId){
        return roleDao.findByUnitId(unitId);
    }

    public void editRole(String id,Role role){
        Role prole=roleDao.findOne(id);
        try {
            CommonUtil.copyNullProperties(role,prole);
        } catch (Exception e) {
            throw new BsException("");
        }
        roleDao.save(prole);
    }
    public void deleteRole(String id){
        roleDao.delete(id);
    }

    public Role getRole(String id){
        return roleDao.findOne(id);
    }

    public void grantMenus(String roleId,List<String> menuCodes){
        Role role=roleDao.findOne(roleId);
        role.setGrants(menuCodes.stream().map(menu->new Grant(Grant.Menu_Type,menu)).collect(Collectors.toList()));
        roleDao.save(role);
    }

    public List<String> getMenus(String roleId){
        Role role=roleDao.findOne(roleId);
        if(role.getGrants()==null) return new ArrayList<String>();
        List<String> menuCodes=role.getGrants().stream().map(grant->grant.getResourceCode()).collect(Collectors.toList());
        return menuCodes;
    }

    public List<Manager> getManagers(String roleid){
        Role role=roleDao.findOne(roleid);
        return managerDao.findByRolesContains(role);
    }

    public void addManager(String roleId,String managerId){
        Role role=roleDao.findOne(roleId);
        Manager manager=managerDao.findOne(managerId);
        List<Role> roles=manager.getRoles();
        if(roles==null) manager.setRoles(Arrays.asList(role));
        else{
            if(!roles.contains(role)) roles.add(role);
        }
        managerDao.save(manager);
    }

    public void resetManagers(String roleid,String[] managerIds){
        if(managerIds==null) return;
        Role role=roleDao.findOne(roleid);
        List<Manager> managers=managerDao.findByRolesContains(role);
        for(Manager manager:managers){
            deleteManager(role.getId(),manager.getId());
        }
        for(String managerid:managerIds){
            Manager manager=managerDao.findOne(managerid);
            addManager(roleid,managerid);
        }
    }

    public void deleteManager(String roleId,String managerId){
        Role role=roleDao.findOne(roleId);
        Manager manager=managerDao.findOne(managerId);
        List<Role> roles=manager.getRoles();
        if(roles==null) return;
        if(roles.contains(role)) roles.remove(role);
        managerDao.save(manager);
    }

}
