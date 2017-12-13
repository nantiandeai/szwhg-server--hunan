package com.creatoo.szwhg.base.service;

import com.creatoo.szwhg.base.dao.MenuDao;
import com.creatoo.szwhg.base.model.Menu;
import com.creatoo.szwhg.core.model.TreeNode;
import com.creatoo.szwhg.core.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yunyan on 2017/8/21.
 */
@Service
public class MenuService {
    @Autowired
    private MenuDao menuDao;

    public String createMenu(Menu menu){
        Menu parent=menu.getParent();
        if(parent!=null && StringUtils.isNotEmpty(parent.getId())){
            parent=menuDao.findOne(parent.getId());
            List<Menu> ancesters=new ArrayList<>();
            if(parent.getAncestors()!=null) ancesters=parent.getAncestors();
            ancesters.add(parent);
            menu.setAncestors(ancesters);
        }
        menuDao.save(menu);
        return menu.getId();
    }

    public void modifyMenu(String menuId,Menu menu){
        Menu pmenu=menuDao.findOne(menuId);
        try {
            CommonUtil.copyNullProperties(menu,pmenu);
            menuDao.save(pmenu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteMenu(String id){
        menuDao.delete(id);
    }

    public Menu getMenu(String id){
        return menuDao.findOne(id);
    }

    public List<Menu> getAllMenus(){
        return menuDao.findAll();
    }

    public List<Menu> getTopMenuTree(){
        List<Menu> topMenus=menuDao.findByParentIsNullOrderBySeqno();
        topMenus.forEach(menu->{
            List<Menu> nodes=menuDao.findByAncestors(menu);
            List<TreeNode> children=nodes.stream().map(node->(TreeNode)node).collect(Collectors.toList());
            CommonUtil.buildRootNode(menu,children);
        });
        return topMenus;
    }

    public List<Menu> getAllLeafNodes(){
        List<Menu> all=menuDao.findAll();
        List<Menu> leafs=new ArrayList<>();
        for(Menu one:all){
            boolean isleaf=true;
            for(Menu item:all){
                if(item.getParent()!=null && item.getParent().getId().equals(one.getId())){
                    isleaf=false;
                }
            }
            if(isleaf) leafs.add(one);
        }
        return leafs;
    }

    public List<Menu> findByCodeIn(List<String> code){
        return menuDao.findByCodeIn(code);
    }
}
