package com.creatoo.szwhg.core.util;

import com.creatoo.szwhg.core.exception.BsException;
import com.creatoo.szwhg.core.model.TreeNode;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.RandomStringUtils;

import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 通用工具类
 * Created by yunyan on 2017/8/10.
 */
public class CommonUtil {
    public final static String DateTime_Format="yyyy-MM-dd HH:mm:ss";
    public final static String Date_Format="yyyy-MM-dd";
    public final static String Time_Format="HH:mm";
    /**
     * 复制对象的有效值到目标对象
     * @param source
     * @param target
     * @throws Exception
     */
    public static void copyNullProperties(Object source, Object target) {
        if (source == null || target == null) return;
        PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
        try {
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(source);
            for (int i = 0; i < descriptors.length; i++) {
                PropertyDescriptor descript = descriptors[i];
                String name = descript.getName();
                PropertyDescriptor tdescript = propertyUtilsBean.getPropertyDescriptor(target, name);
                if (tdescript == null) continue;
                if ("class".equals(name)) continue;
                Object value = propertyUtilsBean.getSimpleProperty(source, name);
                if (value == null) continue;
                Class type = descript.getPropertyType();
                Class ttype = tdescript.getPropertyType();
                if (!type.getName().equals(ttype.getName())) continue;
                if (Collection.class.isAssignableFrom(type) && ((Collection)value).isEmpty()) continue;
                propertyUtilsBean.setProperty(target, name, propertyUtilsBean.getProperty(source, name));
            }
        } catch (Exception e) {
           throw new BsException("copy error",e);
        }
    }

    /**
     * 构造根节点的树结构
     * @param root
     * @param children
     */
    public static void buildRootNode(TreeNode root, List<TreeNode> children){
        Map<String,TreeNode> map=new HashMap<>();  //存储子节点映射
        children.forEach(node->map.put(node.getId(),node));
        for(Map.Entry<String,TreeNode> entry:map.entrySet()){
            TreeNode node=entry.getValue();
            TreeNode parent=node.getParent();
            if(parent.getId().equals(root.getId())){
                root.getChildren().add(node);
            }else{
                String parentId=parent.getId();
                map.get(parentId).getChildren().add(node);
            }
        }
        map.put(root.getId(),root);
        map.forEach((id,node)->node.getChildren().sort((o1,o2)->{return ((TreeNode)o1).getSeqno()-((TreeNode)o2).getSeqno();})); //子节点排序
    }

    /**
     * 生成订单号
     * @return
     */
    public static String generateOrderCode(){
        String code=DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        code=code+RandomStringUtils.randomNumeric(4);
        return code;
    }

    /**
     * 返回当前时间
     * @return
     */
    public static String getCurrentTime(){
        return DateTimeFormatter.ofPattern(DateTime_Format).format(LocalDateTime.now());
    }

    /**
     * 生成6位出票码
     * @return
     */
    public static String generateDrawnCode(){
        return RandomStringUtils.randomNumeric(6);
    }

    /**
     * 生成随机码
     * @param length
     * @return
     */
    public static String generateRandomCode(int length){
        return RandomStringUtils.randomNumeric(length);
    }

    /**
     * 生成10位验票码
     * @return
     */
    public static String generateSeatCode(){
        return RandomStringUtils.randomNumeric(10);
    }

    /**
     * UUID去掉-
     * @return uuid
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
