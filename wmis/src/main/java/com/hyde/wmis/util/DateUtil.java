package com.hyde.wmis.util;


import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * 日期工具类
 */
public class DateUtil {
	public static final String YMD = "yyyyMMdd";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";
	public static final String YMDHMSS = "yyyy-MM-dd HH:mm:ss,SSS";
	public static final String YYYY_year_MM_month_DD_day = "yyyy年MM月dd日";
	public static final String HHMMSS = "HH:mm:ss";
	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String MMDD = "MM.dd";
	public static final String YYYY = "YYYY";
	public static final String MM = "MM";
	public static final String DD = "dd";
	public static final String YYYYMMDDHHMMSSS = "yyyyMMddHHmmssSSS";
	/**
	 * 描述：字符串转换时间类型
	 * @param str 时间字符串
	 * @param dateFormat 格式 "yyyy-MM-dd"
	 * @return
	 */
	public static Date convertStringToDate(String str,String dateFormat){
		SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
		try{
			Date birthDate = sf.parse(str);
			return birthDate;
		}
		catch(ParseException e){
			e.printStackTrace();
		}
		return null;

	}
	/**
	 * 描述：获取当前日期
	 * @return 返回当前日期 日期格式 yyyy-MM-dd
	 */
	public static String currentDate(){
		return currentDate(YYYY_MM_DD);
	}

	/**
	 * 描述：获取当前日期
	 * @param dateFormat 日期格式
	 * @return 返回当前日期
	 */
	public static String currentDate(String dateFormat){
		SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
		String currentDate = sf.format(new Date());
		return currentDate;
	}

	/**
	 * 描述：把java.util.date 转换成String类型
	 * @param date 日期
	 * @return 格式化成yyyy-MM-dd格式的字符串类型日期
	 */
	public static String convertDate(Date date){
		return convertDate(date,YYYY_MM_DD);
	}

	/**
	 * 描述：把java.util.date 转换成String类型
	 * @param date 日期
	 * @param dateFormat 日期格式
	 * @return 格式化成特定格式的字符串类型日期
	 */
	public static String convertDate(Date date,String dateFormat){
		SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
		String currentDate = sf.format(date);
		return currentDate;
	}
	/**
	 * 方法名称: getDate<br>
	 * 描述：获取当前时间
	 * @return
	 */
	public static Date getDate(){
		Date date = new Date();
		return date;
	}

	/**
	 * 描述：将传入的时间转化成秒数
	 * @return 秒数
	 */
	public static int getSecond(String value){
		StringTokenizer s=new StringTokenizer(value,":");
		int type=s.countTokens()-1;
		int result=0;
		Pattern p=Pattern.compile(":");
		if(type==0)// ss
			result=Integer.parseInt(value);
		if(type==1){// mm:ss
			String strValue[]=p.split(value);
			result=Integer.parseInt(strValue[0])*60+Integer.parseInt(strValue[1]);
		}
		if(type==2){// hh:mm:ss
			String strValue[]=p.split(value);
			result=Integer.parseInt(strValue[0])*3600+Integer.parseInt(strValue[1])*60+Integer.parseInt(strValue[2]);
		}
		return result;
	}

	/**根据日期获取年*/
	public static final String TYPE_NUM_BY_BIRTHDAY_YEAR = "year";
	/**根据日期获取月*/
	public static final String TYPE_NUM_BY_BIRTHDAY_MONTH = "month";
	/**根据日期获取日*/
	public static final String TYPE_NUM_BY_BIRTHDAY_DAY = "day";

	/**
	 * 描述：根据日期获取年、月、日
	 * @param birthday
	 * @param flag
	 * @return
	 */
	public static String getNumByBirthday(Date birthday,String flag){
		String Num = null;
		String birthdayStr = convertDate(birthday,YYYY_MM_DD);
		if(StringUtils.isNotBlank(birthdayStr)){
			String[] birthdaySplit = birthdayStr.split("-");
			if(birthdaySplit!=null){
				if(birthdaySplit.length==3){
					if(TYPE_NUM_BY_BIRTHDAY_YEAR.equals(flag)){
						Num = birthdaySplit[0];
					}
					if(TYPE_NUM_BY_BIRTHDAY_MONTH.equals(flag)){
						Num = birthdaySplit[1];
					}
					if(TYPE_NUM_BY_BIRTHDAY_DAY.equals(flag)){
						Num = birthdaySplit[2];
					}
				}
			}
		}
		return Num;
	}

	/**
	 *
	 * @Title: chgDate
	 * @Description: 修改日期
	 * @param startDate
	 * @param y 年偏量
	 * @param m 月偏量
	 * @param d 日偏量
	 * @return
	 * @author wanghuayue
	 * @throws
	 */
	public static Date chgDate(Date startDate, int y, int m, int d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.add(Calendar.YEAR, y);
		cal.add(Calendar.MONTH, m);
		cal.add(Calendar.DATE, d);
		return cal.getTime();
	}
	/**
	 *
	 * @Title: chgDate
	 * @Description: 修改日期
	 * @param startDate
	 * @param y 年偏量
	 * @param m 月偏量
	 * @param d 日偏量
	 * @return
	 * @author wanghuayue
	 * @throws
	 */
	public static Date chgDate(String startDate, String pattern, int y, int m, int d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(convertStringToDate(startDate, pattern));
		cal.add(Calendar.YEAR, y);
		cal.add(Calendar.MONTH, m);
		cal.add(Calendar.DATE, d);
		return cal.getTime();
	}

	/**
	 * 计算日期（包含临界值）
	 * @param targetDate
	 * @param format
	 * @param dateNum
	 * @param unit
	 * @return
	 */
	public static String dateAdd(Date targetDate, String format, int dateNum, String unit){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat(format);
		cal.setTime(targetDate);
		if(("Y").equals(unit)){
			cal.add(Calendar.YEAR, dateNum);
		}else if("M".equals(unit)){
			cal.add(Calendar.MONTH, dateNum);
		}else if("D".equals(unit)){
			cal.add(Calendar.DATE, dateNum);
		}
		return df.format(cal.getTime());
	}

	/**
	 * 计算日期（包含临界值）
	 * @author WHY
	 * @since 2017-9-16 20:39:45
	 * @param targetDate 日期
	 * @param format 返回日期格式
	 * @param dateNum 时间量
	 * @param unit 枚举【YMDhms】
	 * @return
	 */
	public static String datetimeAdd(Date targetDate, String format, int dateNum, String unit){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat(format);
		cal.setTime(targetDate);
		if(("Y").equals(unit)){
			cal.add(Calendar.YEAR, dateNum);
		}else if("M".equals(unit)){
			cal.add(Calendar.MONTH, dateNum);
		}else if("D".equals(unit)){
			cal.add(Calendar.DATE, dateNum);
		}else if("h".equals(unit)){
			cal.set(Calendar.HOUR_OF_DAY, dateNum);
		}else if("m".equals(unit)){
			cal.set(Calendar.MINUTE, dateNum);
		}else if("s".equals(unit)){
			cal.set(Calendar.SECOND, dateNum);
		}
		return df.format(cal.getTime());
	}

	/**
	 * 计算日期（不包含临界值）
	 * @param targetDate
	 * @param format
	 * @param dateNum
	 * @param unit
	 * @return
	 */
	public static String dateAddNoCriticality(Date targetDate, String format, int dateNum, String unit){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat(format);
		cal.setTime(targetDate);
		if(("Y").equals(unit)){
			cal.add(Calendar.YEAR, dateNum-1);
		}else if(("M").equals(unit)){
			cal.add(Calendar.MONTH, dateNum);
		}else if(("D").equals(unit)){
			cal.add(Calendar.DATE, dateNum);
		}
		cal.add(Calendar.DATE, 1);
		return df.format(cal.getTime());
	}
	/**
	 * 获取传入日期的当天开始时间。<br>
	 * 如：传入日期为2013-10-10 15:39:20，则返回日期为2013-10-10 00:00:00
	 * @param d date
	 * @return
	 */
	public static Date getStartDate(Date d) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		return calendar.getTime();
	}

}