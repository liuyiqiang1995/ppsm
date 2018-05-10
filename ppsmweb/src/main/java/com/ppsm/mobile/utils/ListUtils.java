/**
 * Copyright 2002-2013 Qingdao Civil Aviation Cares Co., LTD
 * All Rights Reserved.
 * 2013-12-26 下午6:40:47 yangql
 */
package com.ppsm.mobile.utils;

import java.util.Collections;
import java.util.List;

/**
 * List 工具类
 *
 * @Author SUN
 * @Date 2017/12/12 10:35
 */
public class ListUtils {
    /**
     * 校验是否为空
     *
     * @Author SUN
     * @Date 2017/12/12 10:39
     */
    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static boolean isNotEmpty(List list) {
        return !isEmpty(list);
    }

    /**
     * @Description: 判断两个list的内容是否相等
     * @Author: LiuYiQiang
     * @Date: 22:08 2018/4/26
     */
    public static <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
        boolean flag = true;
        if(a.size() != b.size()){
            return false;
        }
        if(ListUtils.isEmpty(a) || ListUtils.isEmpty(b)){
            return true;
        }
        Collections.sort(a);
        Collections.sort(b);
        for(int i=0;i<a.size();i++){
            String old = (String)a.get(i);
            String newL = (String)b.get(i);
            if(!old.equals(newL)){
                flag = false;
                break;
            }
        }
        return flag;
    }

}
