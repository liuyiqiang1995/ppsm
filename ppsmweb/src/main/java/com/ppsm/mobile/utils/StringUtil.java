/**
 * Copyright 2002-2013 Qingdao Civil Aviation Cares Co., LTD
 * All Rights Reserved.
 * 2013-12-26 下午6:40:47 yangql
 */
package com.ppsm.mobile.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author yangql
 */
public class StringUtil {

    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

    public static boolean isNull(String str) {
        if (str == null || str.equals(""))
            return true;
        return false;
    }

    public static boolean isNotNull(String str) {
        if (str == null || str.equals(""))
            return false;
        return true;
    }

    public static String setNull(String str) {
        return str == null ? "" : str;
    }

    public static boolean isNull(String[] array) {
        if (array == null || array.length == 0)
            return true;
        else
            return false;
    }

    public static String getLastItem(String array) {
        String result = "";
        if (array != null && array.contains(",")) {
            if (array.split(",").length > 0)
                result = array.split(",")[array.split(",").length - 1];
        }
        return result;
    }

    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        if ("".equals(str.trim())) {
            return true;
        }
        return false;
    }
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isEmpty(List<?> list) {
        if (list == null || list.isEmpty())
            return true;
        else
            return false;
    }

    public static String getStrConcat(String front, String back, String split) {
        String result = "";
        if (!isNull(front) && !isNull(back)) {
            if (front.equals(back))
                result = front;
            else
                result = front + split + back;
        } else if (!isNull(front) && isNull(back)) {
            result = front;
        } else if (isNull(front) && !isNull(back)) {
            result = back;
        } else {
            result = "";
        }
        return result;
    }

    /**
     * 校验是否可以转化为数字（仅支持正整数）
     *
     * @Author SUN
     * @Date 2017/6/20 20:27
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 删除末尾的逗号
     *
     * @Author SUN
     * @Date 2017/11/7 18:36
     */
    public static String delEndComma(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return str.substring(0, str.length() - 1);
    }

    /**
     * 过滤出数字
     *
     * @Author SUN
     * @Date 2018/1/18 14:39
     */
    public static String filterNumber(String number) {
        number = number.replaceAll("[^(0-9)]", "");
        return number;
    }

    /**
     * 过滤出字母
     *
     * @Author SUN
     * @Date 2018/1/18 16:24
     */
    public static String filterAlphabet(String alph) {
        alph = alph.replaceAll("[^(A-Za-z)]", "");
        return alph;
    }

    public static String toString(Long data){
        if (data == null){
            return "";
        }else {
            return String.valueOf(data);
        }
    }

    public static String toString(Integer data){
        if (data == null){
            return "";
        }else {
            return String.valueOf(data);
        }
    }

    public static String toString(Short data){
        if (data == null){
            return "";
        }else {
            return String.valueOf(data);
        }
    }

    public static String toString(Float data){
        if (data == null){
            return "";
        }else {
            return String.valueOf(data);
        }
    }

    public static String toString(String data){
        if (data == null){
            return "";
        }else {
            return data.toString();
        }
    }

    /**
     * 截取字符串长度
     *  当字符串长度不足时返回 subString(beginIndex,length()-1)
     *
     * @Author SUN
     * @Date 2018/3/19 19:51
     */
    public static String subStringCheckLength(String string, int beginIndex, int endIndex) {
        if (string == null || string.length() < beginIndex) {
            return "";
        }
        if (string.length() < endIndex) {
            endIndex = string.length() - 1;
        }
        return string.substring(beginIndex, endIndex);
    }

    /**
     * @Description: 截取字符串，长度小于起始位置时返回空字符串；长度小于结束位置时，截取到最大长度
     * @Author: durunguo
     * @Date:10:13 2018/3/20
     */
    public static String subString(String string, int beginIndex, int endIndex) {
        if (string == null) {
            return "";
        }
        int len  = string.length();
        // 如果字符串长度小于截取开始位置
        if(len < beginIndex){
            return "";
        }
        // 如果字符串长度小于截取结束位置
        if (len < endIndex) {
            return string.substring(beginIndex, len);
        }
        return string.substring(beginIndex, endIndex);
    }

   /**
    * @Description: 截取字符串，长度小于起始位置时返回空字符串
    * @Author: durunguo
    * @Date:10:12 2018/3/20
    */
    public static String subString(String string, int beginIndex) {
        if (string == null) {
            return "";
        }
        int len  = string.length();
        // 如果字符串长度小于起始截取位置
        if(len < beginIndex){
            return "";
        }
        return string.substring(beginIndex);
    }
    
    public static String getSqlStrByList(List<Long> delFlightIdList, String columnName) {  
		int splitNum = delFlightIdList.size();
		//因为数据库的列表sql限制，不能超过1000.   
		if(delFlightIdList.size() > 1000) {
			splitNum = 1000;
		}
        StringBuffer sql = new StringBuffer("");  
        if (delFlightIdList != null) {  
            sql.append(" ").append(columnName).append (" IN ( ");  
            for (int i = 0; i < delFlightIdList.size(); i++) {  
                sql.append("'").append(delFlightIdList.get(i) + "',");  
                if ((i + 1) % splitNum == 0 && (i + 1) < delFlightIdList.size()) {  
                    sql.deleteCharAt(sql.length() - 1);  
                    sql.append(" ) OR ").append(columnName).append (" IN (");  
                }  
            }  
            sql.deleteCharAt(sql.length() - 1);  
            sql.append(" )");  
        }  
        return sql.toString();  
    }

}
