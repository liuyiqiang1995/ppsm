/**
 * Copyright 2002-2013 Qingdao Civil Aviation Cares Co., LTD
 * All Rights Reserved.
 * 2013-11-22 下午9:08:28 yangql
 */
package com.ppsm.mobile.utils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author yangql 日期、字符串装换
 */
public class DateStringUtil implements Serializable {

	private static final long serialVersionUID = -3181329633706296519L;

	/**
	 * @param date
	 * @param format
	 *            "yyyy-MM-dd HH:mm:ss"
	 * @return 返回format格式的日期字符串
	 */
	public static String formatDate(Date date, String format) {
		String result = "";
		if (date != null) {
			DateFormat df = new SimpleDateFormat(format);
			result = df.format(date);
		}
		return result;
	}

	/**
	 * @param date
	 * @param format
	 * @return 将format格式的字符串转化为日期
	 * @throws ParseException
	 */
	public static Date convertString(String date, String format)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date result = (Date) sdf.parse(date);
		return result;
	}
	
	/**将时间格式化为5的整数倍
	 * zxh
	 * @param date
	 * @return
	 */
	public static Date formatTime(Date date){
		Date rslt = null;
		if(date!=null){
			String lastMin = formatDate(date,"yyyyMMddHHmm").substring(11) ;
			Calendar lt = Calendar.getInstance();
			lt.setTime(date);
			if("1".equals(lastMin)||"6".equals(lastMin)){
				 lt.add(Calendar.MINUTE, -1) ;
			}else if("2".equals(lastMin)||"7".equals(lastMin)){
				lt.add(Calendar.MINUTE, -2) ;
			}else if("3".equals(lastMin)||"8".equals(lastMin)){
				lt.add(Calendar.MINUTE, 2) ;
			}else if("4".equals(lastMin)||"9".equals(lastMin)){
				lt.add(Calendar.MINUTE, 1) ;
			}
			rslt = lt.getTime();
		}
		return rslt;
	}
	/**判断时间段是否有重叠
	 * zxh
	 * @param startTime_new
	 * @param endTime_new
	 * @param startTime_old
	 * @param endTime_old
	 * @return
	 */
	public static boolean checkOverlap(Date startTime_new,Date endTime_new,Date startTime_old,Date endTime_old){
		if(startTime_new.after(startTime_old)&&startTime_new.before(endTime_old)){
			return false;
		}else if(endTime_new.after(startTime_old)&&endTime_new.before(endTime_old)){
			return false;
		}else if(startTime_new.before(startTime_old)&&endTime_new.after(endTime_old)){
			return false;
		}
		return true;
	}
	/**
	 * 日期增加天数
	 * @author sapburning
	 * @param date
	 * @param days
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static Date addDay(Date date,int days){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(c.DAY_OF_MONTH, days);
		return c.getTime();		
	}
	
	public static Date removeMilliSecond(Date date){
		if(date==null)
			return null;
		long temp = date.getTime()/1000;
		date.setTime(temp*1000);
		return date;
	}

	/**
	 *@author yangfx
	 *@date 16:20 2018/4/19
	 *@description  给日期增加指定的时间 HHMM
	 */
	public static String getTimeString(Date date, String info){

		if((StringUtil.isEmpty(info)) || (date == null))
			return null;

		Calendar cal = Calendar.getInstance();

		cal.clear();
		cal.setTime(date);

		String part = info.substring(0, 2);
		cal.add(Calendar.HOUR_OF_DAY, Integer.parseInt(part));

		part = info.substring(2);
		cal.add(Calendar.MINUTE, Integer.parseInt(part));

		return formatDate(cal.getTime(),"yyyy-MM-dd HHmm");
	}

}
