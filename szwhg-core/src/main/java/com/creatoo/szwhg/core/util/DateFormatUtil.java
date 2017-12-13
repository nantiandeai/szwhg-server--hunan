package com.creatoo.szwhg.core.util;

import java.util.Date;

/**
 * 日期格式化
 * Created by wangxl on 2017/9/6.
 */
public class DateFormatUtil {
    public static final java.text.SimpleDateFormat sdf_yyyyMMdd = new java.text.SimpleDateFormat("yyyy-MM-dd");
    public static final java.text.SimpleDateFormat sdf_yyyyMMddHHmm = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final java.text.SimpleDateFormat sdf_yyyyMMddHHmmss = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 将字符串日期转换成日期对象
     * @param yyyyMMdd 日期字符串, 如：2017-01-01
     * @return Date对象
     */
    public static Date parseYMD(String yyyyMMdd){
        Date date = null;
        try{
            date = sdf_yyyyMMdd.parse(yyyyMMdd);
        }catch (Exception e){}
        return date;
    }

    /**
     * 将字符串日期转换成日期对象
     * @param yyyyMMddHHmm 日期字符串, 如：2017-01-01 14:30
     * @return Date对象
     */
    public static Date parseYMDHM(String yyyyMMddHHmm){
        Date date = null;
        try{
            date = sdf_yyyyMMddHHmm.parse(yyyyMMddHHmm);
        }catch (Exception e){}
        return date;
    }

    /**
     * 将字符串日期转换成日期对象
     * @param yyyyMMddHHmmss 日期字符串, 如：2017-01-01 14:30:30
     * @return Date对象
     */
    public static Date parseYMDHMS(String yyyyMMddHHmmss){
        Date date = null;
        try{
            date = sdf_yyyyMMddHHmmss.parse(yyyyMMddHHmmss);
        }catch (Exception e){}
        return date;
    }

    /**
     * 将日期对象格式化成字符串: yyyy-MM-dd
     * @param date 日期对象
     * @return yyyy-MM-dd字符串
     */
    public static String formatYMD(Date date){
        String formatStr = "";
        try{
            formatStr = sdf_yyyyMMdd.format(date);
        }catch (Exception e){}
        return formatStr;
    }

    /**
     * 将日期对象格式化成字符串: yyyy-MM-dd HH:mm
     * @param date 日期对象
     * @return yyyy-MM-dd HH:mm字符串
     */
    public static String formatYMDHM(Date date){
        String formatStr = "";
        try{
            formatStr = sdf_yyyyMMddHHmm.format(date);
        }catch (Exception e){}
        return formatStr;
    }

    /**
     * 将日期对象格式化成字符串: yyyy-MM-dd HH:mm:ss
     * @param date 日期对象
     * @return yyyy-MM-dd HH:mm:ss字符串
     */
    public static String formatYMDHMS(Date date){
        String formatStr = "";
        try{
            formatStr = sdf_yyyyMMddHHmmss.format(date);
        }catch (Exception e){}
        return formatStr;
    }


}
