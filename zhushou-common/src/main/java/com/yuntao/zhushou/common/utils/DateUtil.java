package com.yuntao.zhushou.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
	public static final int MINITUE_SECONDS = 60;
	public static final int MINITUE_MILLIS = MINITUE_SECONDS * 1000;
	
	public static final int MINITUE_5_SECONDS = 60 * 5;
	public static final int MINITUE_10_SECONDS = 60 * 10;
	public static final int MINITUE_5_MILLIS  = MINITUE_5_SECONDS * 1000;
	
	public static final int HOUR_SECONDS = MINITUE_SECONDS * 60;
	public static final int HOUR_MILLIS = HOUR_SECONDS * 1000;
	
	public static final int DAY_SECONDS = HOUR_SECONDS * 24;
	public static final int DAY_MILLIS  =DAY_SECONDS * 1000;
	
	public static final int MONTH_SECONDS = DAY_SECONDS * 30;
	public static final int MONTH_MILLIS  = MONTH_SECONDS * 1000;

	public static String getFmtYMDNoSymbol(long timeMillis) {
		return new SimpleDateFormat("yyyyMMdd").format(new Date(timeMillis));
	}

	public static String getFmtyMdHmNoSymbol(long timeMillis) {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(timeMillis));
	}

	public static String getFmtyMdHmsSSSNoSymbol(long timeMillis) {
		return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date(timeMillis));
	}

	public static String getFmtYMDHMS(long timeMillis) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timeMillis));
	}
	public static String getFmtYMDHM(long timeMillis) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(timeMillis));
	}
	public static String getFmt(long timeMillis,String format) {
		return new SimpleDateFormat(format).format(new Date(timeMillis));
	}
	public static Date getDate(String time,String format) {
		try {
			return new SimpleDateFormat(format).parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param dt
	 * @param day
	 * @return
	 */
	public static Date getDateAfter(Date dt, int day) {
		Calendar curr = Calendar.getInstance();
		curr.setTime(dt);
		curr.add(Calendar.DATE, day);
		return curr.getTime();
	}

	/**
	 * 获取给定间隔之后的时间
	 * @param dt
	 * @param field
	 * @param num
     * @return
     */
	public static Date getTimeAfter(Date dt, int field,int num) {
		Calendar curr = Calendar.getInstance();
		curr.setTime(dt);
		curr.add(field, num);
		return curr.getTime();
	}
    /**
     * 
     * @param dt
     * @param day
     * @return
     */
    public static Date getMonthAfter(Date dt, int day) {
        Calendar curr = Calendar.getInstance();
        curr.setTime(dt);
        curr.add(Calendar.MONTH, day);
        return curr.getTime();
    }
	/**
	 * 
	 * @param dt
	 * @param day
	 * @return
	 */
	public static boolean isDateAfter(Date dt, int day) {
		Date copDate = DateUtil.getDateAfter(dt, day);
		if (copDate.before(new Date())) {
			return false;
		}
		return true;

	}
	
    public static Date getDateFirstSecond(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date getDateLastSecond(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

	public static String remainTime(Date beginTime, Date endTime) {
		if (beginTime == null || endTime == null) {
			return null;
		}
		long seconds = (endTime.getTime() - beginTime.getTime()) / 1000;
		if ((seconds / DAY_SECONDS) > 0) {
			return seconds / DAY_SECONDS + "天";
		}
		if ((seconds / HOUR_SECONDS) > 0) {
			return seconds / HOUR_SECONDS + "小时";
		}
		if ((seconds / MINITUE_SECONDS) > 0) {
			return seconds / MINITUE_SECONDS + "分钟";
		}
		return null;
	}

	public static Date transGMT2CST(String gmtTime){
		DateFormat format=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		try {
			return format.parse(gmtTime);
		} catch (ParseException e) {
		}
		return null;
	}

	public static String transCST2GMT(Date date){
		DateFormat format=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		return format.format(date);
	}
	//获取当前周的最后时刻
	public static Date getLastTimeOfThisWeek(){
		Calendar cal = Calendar.getInstance();  
        //设置该周第一天为星期一  
        cal.setFirstDayOfWeek(Calendar.MONDAY);   
        //设置最后一天是星期日  
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek() + 6); // Sunday  
        //格式化日期  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        String lastDayOfWeek = sdf.format(cal.getTime())+" 23:59:59";  
        try {
        	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date lastDate=sdf2.parse(lastDayOfWeek);
			return lastDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//获取当前周的最时刻
	public static Date getFirstTimeofThisWeek(){
		Calendar cal = Calendar.getInstance();  
        //设置该周第一天为星期一  
        cal.setFirstDayOfWeek(Calendar.MONDAY);   
        //设置最后一天是星期日  
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek()); // Monday 
        //格式化日期  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        String lastDayOfWeek = sdf.format(cal.getTime())+" 00:00:00";  
        try {
        	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date lastDate=sdf2.parse(lastDayOfWeek);
			return lastDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
		
		
	}
	//到截止时间还剩余天数小时数
	public static String distanceTime(Date beginTime, Date endTime) {
		long days=0;
		long hours=0;
		long minitues=0;
		if (beginTime == null || endTime == null) {
			return null;
		}
		long seconds = (endTime.getTime() - beginTime.getTime()) / 1000;
		
		if ((seconds / DAY_SECONDS) > 0) {
			days=seconds / DAY_SECONDS;
		}
		if (((seconds-days*DAY_SECONDS) / HOUR_SECONDS) > 0) {
			hours=(seconds-days*DAY_SECONDS) / HOUR_SECONDS;
		}
		if (((seconds-days*DAY_SECONDS-hours*HOUR_SECONDS) / MINITUE_SECONDS) > 0) {
			minitues=(seconds-days*DAY_SECONDS-hours*HOUR_SECONDS) / MINITUE_SECONDS;
		}
		StringBuffer sb=new StringBuffer();
		if(days>0){
			sb.append(days).append("天");
		}
		if(hours>0){
			sb.append(hours).append("小时");
		}
		if(minitues>0){
			sb.append(minitues).append("分");
		}
		return sb.toString();
	}

	/**
	 * 返回时间模糊数
	 * 1分钟内：几秒前
	 1小时内：几分前
	 1天内：几小时前
	 1周内：几天前
	 1月内：几月几日
	 1年内；几年几月几日
	 * @param time
	 * @return
     */
	public static String getRangeTime(Date time){
		if(time == null){
			return null;
		}
		Date now = new Date();
		long seconds = (now.getTime() - time.getTime()) / 1000;
		if(seconds < 60 ){
			return seconds+"秒钟前";
		}else if(seconds < 60 * 60){
			return (seconds/60)+"分钟前";
		}else if(seconds < 60 * 60 * 24){
			return (seconds/60/60)+"小时前";
		}else{
			return getFmt(time.getTime(),"yyyy-MM-dd");
		}
	}

	public static String getRangeTime(Date time,String format){
		if(time == null){
			return null;
		}
		Date now = new Date();
		long seconds = (now.getTime() - time.getTime()) / 1000;
		if(seconds < 60 ){
			return seconds+"秒钟前";
		}else if(seconds < 60 * 60){
			return (seconds/60)+"分钟前";
		}else if(seconds < 60 * 60 * 24){
			return (seconds/60/60)+"小时前";
		}else{
			if (StringUtils.isEmpty(format)) {
				format = "yyyy-MM-dd";
			}
			return getFmt(time.getTime(),format);
		}
	}
	public static void main(String[] args) {
//	    System.out.println(new Date(1437521526000L).getMinutes());
//
//		Calendar curr = Calendar.getInstance();
//		curr.set(2015, 6, 7, 0, 1, 1);
//		System.out.println(DateUtil.isDateAfter(curr.getTime(),1));
//
//
//
//		System.out.println(getDateAfter(new Date(),1));
//		System.out.println(DigestUtils.md5Hex("type=1&uid=1166&appid=5091&wechatid=oI2NQt7shnylutzgWbBKD4fvUGhQ&appuserid=c9z5dcgn&key=DFAEADFDSAFSFEADFDSFA"));

		//Date time = getDate("2016-02-19 14:02:59","yyyy-MM-dd HH:mm:ss");
		//String result = getRangeTime(time);
		//System.out.println(result);


		Date time = getTimeAfter(new Date(),Calendar.MINUTE,5);
		System.out.println(time);
		String beforeTimeString = DateUtil.getFmt(time.getTime(),"yyyy-MM-dd HH:mm");
		System.out.printf(beforeTimeString);
	}

}
