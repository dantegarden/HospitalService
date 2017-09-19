package com.dvt.HospitalService.commons.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;


//import com.tansun.common.service.CodeTable;

/**
 * 时间计算工具类
 */
public class DateTools {
	
	private final static String TERMUNIT = "TermUnitCd";

	private final static String TERMUNIT_S1 = "S1";

	private final static String TERMUNIT_S2 = "S2";

	private final static String TERMUNIT_S3 = "S3";

	
	/**
	 * 字符串转Date
	 * @param str
	 * @return
	 */
	public static Date stringToDate(String str,String _format){
		if(StringUtils.isBlank(str) || StringUtils.isBlank(_format)){
			return null;
		}
		DateFormat format = new SimpleDateFormat(_format);
		Date date = null;
		try {    
	         date = format.parse(str);   
		}catch(ParseException e) {    
	         e.printStackTrace();
	    } 
		return date;

	}
	
	/**
	 * date 转 string
	 * @param date
	 * @param format
	 * @return
	 */
	public static String dateToString(Date date,String format){
		String _str = null;
		DateFormat _format = new SimpleDateFormat(format);
		if(date != null ){
			_str = _format.format(date);
		}		
		return _str;
	}
	
	/**
	 * 获取一个月有多少天数
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getActualMaximum(int year,int month){
		 Calendar c= Calendar.getInstance();
		 c.set(Calendar.YEAR, year);
		 c.set(Calendar.MONTH, month-1);
		 c.set(Calendar.DATE, 1);
		 return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
    /** 
     * 根据日期获得星期 
     * @param date 
     * @return 
     */ 
	public static  String getWeekOfDate(Date date) { 
		  String[] weekDaysName = { "日", "一", "二", "三", "四", "五", "六" }; 
		  //String[] weekDaysCode = { "0", "1", "2", "3", "4", "5", "6" }; 
		  Calendar calendar = Calendar.getInstance(); 
		  calendar.setTime(date); 
		  int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; 
		  return weekDaysName[intWeek];
	}
	/**
	 * 获取系统当前时间 del 改用注入ISystemTimeService
	 * @param format
	 * @return
	 */
	public static String getCurrentDate(String format){
		SimpleDateFormat df = new SimpleDateFormat(format);//设置日期格式
		return df.format(new Date());
	}
	
	/**
	 * 取得到期日期
	 * 
	 * @param start
	 * @param unit
	 * @param amount
	 * @return
	 */
	public static Date getEndingDate(Date start, String unit, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		return getEndingDate(cal, unit, amount).getTime();
	}

	/**
	 * 取得到期日期
	 * 
	 * @param start
	 *            Calendar
	 * @param unit
	 *            String
	 * @param amount
	 *            int
	 */
	public static Calendar getEndingDate(Calendar start, String unit, int amount) {

		int field = -1;
		GregorianCalendar cal = new GregorianCalendar(start.get(Calendar.YEAR),
				start.get(Calendar.MONTH), start.get(Calendar.DAY_OF_MONTH),
				start.get(Calendar.HOUR), start.get(Calendar.MINUTE));

		//if (unit.equals(CodeTable.getValue(TERMUNIT, TERMUNIT_S1)))
		if (unit.equals("1"))
			field = Calendar.YEAR;
		//if (unit.equals(CodeTable.getValue(TERMUNIT, TERMUNIT_S2)))
		if (unit.equals("2"))
			field = Calendar.MONTH;
		//if (unit.equals(CodeTable.getValue(TERMUNIT, TERMUNIT_S3)))
		if (unit.equals("3"))
			field = Calendar.DAY_OF_MONTH;
		cal.add(field, amount);

		return cal;
	}

	/**
	 * 计算两个日期相差的天数，只考虑年月日，不考虑时分秒(如果加上时分秒，可能导致天数计算错误)
	 * 
	 * @param before
	 * @param after
	 * @return long 相差的天数
	 */
	public static int getDateDiff(Date before, Date after) {
		if (before == null || after == null)
			return 0;
		Calendar calendar1 = new GregorianCalendar();
		Calendar calendar2 = new GregorianCalendar();
		calendar1.setTime(before);
		calendar1.set(Calendar.HOUR_OF_DAY, 0);
		calendar1.set(Calendar.MINUTE, 0);
		calendar1.set(Calendar.SECOND, 0);
		calendar1.set(Calendar.MILLISECOND, 0);
		calendar2.setTime(after);
		calendar2.set(Calendar.HOUR_OF_DAY, 0);
		calendar2.set(Calendar.MINUTE, 0);
		calendar2.set(Calendar.SECOND, 0);
		calendar2.set(Calendar.MILLISECOND, 0);
		int diff = (int) ((calendar2.getTime().getTime() - calendar1.getTime().getTime()) / (24 * 60 * 60 * 1000));
		return diff;
	}

	/**
	 * 计算两个日期相差的天数，不考虑日期前后，返回结果>=0
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static int getAbsDateDiff(Date before, Date after) {
		int diff = getDateDiff(before, after);
		return Math.abs(diff);
	}

	/**
	 * 比较第一个日期是否早于第二个日期 利用了getDateDiff方法，如果两者相差天数>0,则为true;
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static boolean getDateIsBefore(Date d1, Date d2) {
		if (d1 == null || d2 == null)
			return false;
		int diff = getDateDiff(d1, d2);
		if (diff > 0)
			return true;
		return false;
	}

	/**
	 * 比较第一个日期是否晚于第二个日期 利用了getDateDiff方法，如果两者相差天数<0,则为true;
	 * 
	 * @param before
	 * @param after
	 * @return boolean
	 */
	public static boolean getDateIsAfter(Date d1, Date d2) {
		if (d1 == null || d2 == null)
			return false;
		int diff = getDateDiff(d1, d2);
		if (diff < 0)
			return true;
		return false;
	}

	/**
	 * Timestamp转成Calendar
	 * 
	 * @param timestamp
	 * @return Calendar
	 * 
	 */
	public static Calendar timestampToCalendar(Timestamp timestamp) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(timestamp.getTime());
		calendar.getTime();// 立即刷新
		return calendar; // 不能使用getInstance,否则变当时状态
	}

	/**
	 * Calendar转成Timestamp
	 * 
	 * @param calendar
	 * @return Timestamp
	 * 
	 */
	public static Timestamp calendarToTimestamp(Calendar calendar) {
		return new Timestamp(calendar.getTimeInMillis());// 不能使用getInstance,否则变成当时状态

	}

	/**
	 * 根据期限单位取期限的天数
	 * 
	 * @param termUnitCd
	 *            期限单位
	 * @param term
	 *            期限
	 * @return 期限（单位为天）
	 * @since
	 * 
	 * <pre>
	 * int temp = ContractUtil.intValue(term);
	 * if (null != termUnitCd &amp;&amp; null != term) {
	 * 	if (CodeTable.getValue(&quot;TermUnit&quot;, &quot;S1&quot;).equals(termUnitCd)) {
	 * 		temp = ContractUtil.intValue(term) * 365;
	 * 	}
	 * 	if (CodeTable.getValue(&quot;TermUnit&quot;, &quot;S2&quot;).equals(termUnitCd)) {
	 * 		temp = ContractUtil.intValue(term) * 30;
	 * 	}
	 * }
	 * return temp;
	 * </pre>
	 */
	public static int calTermDay(String termUnitCd, Integer term) {
		int temp = NumberUtil.intValue(term);
		if (null != termUnitCd && null != term) {
			//if (CodeTable.getValue(TERMUNIT, TERMUNIT_S1).equals(termUnitCd)) {
			if ("1".equals(termUnitCd)) {
				temp = NumberUtil.intValue(term) * 365;
			}
			//if (CodeTable.getValue(TERMUNIT, TERMUNIT_S2).equals(termUnitCd)) {
			if ("2".equals(termUnitCd)) {
				temp = NumberUtil.intValue(term) * 30;
			}
		}
		return temp;
	}

	/**
	 * 根据开始日期和结束日期计算两个日期的工作日天数
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @param holidays 节假日集合 Date of List(yyyy-MM-dd) 也可为空（为空的情况忽略法定节假日）
	 * @param weekEndWords 周末为工作日的日期集合
	 * @return int 返回工作天数
	 */
	public static int countWorkingDays(Date startDate, Date endDate,List holidays,List weekEndWorkDays) {
		Date date ;
		if(getDateIsBefore(startDate,endDate)){
			date=startDate;
		}else{
			date=endDate;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int count = 0;	
		while (!date.after(endDate)) {			
			if(isWorkDay(date,holidays,weekEndWorkDays)){//判断是否为工作日，如果为工作日则加1
				count++;
			}
			cal.add(Calendar.DATE, 1);//日期向后加1天
			date = cal.getTime();	
		}		

		return count;
	}
	
	/**
	 * 根据传入日期返回星期几
	 * @param date 日期
	 * @return 1-7 1：星期天,2:星期一,3:星期二,4:星期三,5:星期四,6:星期五,7:星期六
	 */
	public static int getDayOfWeek(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * 根据日期判断是否为工作日
	 * @param date 需要判断的日期
	 * @param holidays 法定假日集合
	 * @param weekEndWorkDays 周末工作日集合
	 * @return 是工作日返回true,不是工作日返回false;
	 */
	public static boolean isWorkDay(Date date,List holidays,List weekEndWorkDays) {
		int day_of_week = DateTools.getDayOfWeek(date);
		if (day_of_week == 1 && !isHoliday(date,weekEndWorkDays)) {// 星期六
			return false;
		} else if (day_of_week == 7 && !isHoliday(date,weekEndWorkDays)) {// 星期天
			return false;
		}
		if (holidays != null && holidays.size() > 0) {
			if(isHoliday(date,holidays)){//判断是否为法定假日
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 判断是否为法定假日
	 * @param date 需要判断的日期
	 * @param holidays 假日集合
	 * @return true为法定假日，false为工作日
	 */
	public static boolean isHoliday(Date date,List holidays){
		if(holidays!=null && holidays.size()>0){
			Date holiday;
			for (int j = 0; j < holidays.size(); j++) {
				holiday = (Date) holidays.get(j);
				if ( getDateDiff(holiday,date) == 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 日期年份计算函数
	 * @param date 要计算的日期
	 * @param addYears 要计算的数字 正数为增加XX年 负数为减少XX年
	 * @return 计算后的日期
	 */
	public static Date addYear(Date date, Integer addYears){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, addYears);
		return calendar.getTime();
	}
	
	/**
	 * 日期月份计算函数
	 * @param date 要计算的日期
	 * @param addMouths 要计算的数字 正数为增加XX月 负数为减少XX月
	 * @return 计算后的日期
	 */
	public static Date addMouth(Date date, Integer addMouths){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, addMouths);
		return calendar.getTime();
	}
	
	/**
	 * 日期天数计算函数
	 * @param date 要计算的日期
	 * @param addDays 要计算的数字 正数为增加XX天 负数为减少XX天
	 * @return 计算后的日期
	 */
	public static Date addDay(Date date, Integer addDays){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, addDays);
		return calendar.getTime();
	}
	
	/**
	 * 获取系统时间(系统环境影响时区经过修正)
	 * 
	 * @param format
	 * @return 
	 */
	public static String getSystemDate(String format){   
	    SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);   
	    formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));   
	    Date currTime = new Date();   
	    return formatter.format(currTime);   
	}
	
	/**
	 * 计算所处时区
	 * 
	 * @param timeZone_digital 时区时间差值 格式： -480,480等
	 * @return
	 */
	public static TimeZone getTimeZone(String timeZone_digital) {
        String gmt = "";   
        if (timeZone_digital != null) {   
            int length = timeZone_digital.length();
            String d = "";   
            String f = "";   
            if (length == 1){   
                gmt = "GMT";   
            } else if (timeZone_digital.startsWith("-")) {   
                d = timeZone_digital.substring(0, 1);
                f = timeZone_digital.substring(1, length);
                gmt = "GMT+" + Integer.parseInt(f)/60;   
            } else {   
                gmt = "GMT-" + Integer.parseInt(timeZone_digital)/60;   
            }   
        } else {   
            // 北京时区   
            return TimeZone.getTimeZone("GMT+8");
        }   
        return TimeZone.getTimeZone(gmt);
	}
	
	public static int getLastMonthDay() {
       Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        int lastMonth = cal.get(Calendar.MONTH); // 上个月月份    
        int thisYear = cal.get(Calendar.YEAR); 
        int thisDay1 = cal.getActualMinimum(Calendar.DAY_OF_MONTH);// 起始天数
        int thisDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH); // 结束天数       
        int last = cal.getActualMaximum(Calendar.DATE);
        System.out.println("thisYear:"+thisYear);
        System.out.println("lastMonth:"+lastMonth);
        System.out.println("thisDay1:"+thisDay1);
        System.out.println("thisDay:"+thisDay);
        System.out.println("thisMonthDay:"+last);
        
        
        cal.add(Calendar.MONTH,-1); 
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        
        System.out.println("lastMonthDay:"+lastDay);
        
        return lastDay;
        
        /*Calendar a = Calendar.getInstance();       
        a.set(Calendar.DATE, 1); //把日期设置为当月第一天       
        a.roll(Calendar.DATE, -1); //日期回滚一天，也就是上一个月最后一天   
        
        int MaxDate = a.getActualMaximum(Calendar.DATE); 
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
        String ddd = simpleDateFormat.format(a.getTime()); 
        System.out.println(":"+ddd);
        System.out.println("该月最大天数:"+MaxDate);*/

        /*
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
        Calendar calendar = new GregorianCalendar(2000,2,1); 
        String ddd = simpleDateFormat.format(calendar.getTime()); 
        System.out.println("2000年3月1号:"+ddd);
        calendar.add(Calendar.MONTH,-1); 
        //calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)-1); 
        String ddd2 = simpleDateFormat.format(calendar.getTime()); 
        System.out.println("2000年2月最后一天:"+ddd2);
        System.out.println( "2000年3月1号的上个月份最后一天是 "+calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        */
    }
	/**
	 * 获取指定日期的当月的月份数
	 * @param date
	 * @return
	 */
	public static int getLastMonth(Date date) {
	    Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int lastMonth = cal.get(Calendar.MONTH); // 上个月月份           
        
        return lastMonth;

	 }
	/**
	 * 上月的最后一天
	 * @param date
	 * @return
	 */
	public static Calendar newLastMonth(Date date) {
	    Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int lastMonth = cal.get(Calendar.MONTH); // 上个月月份    
        int thisYear = cal.get(Calendar.YEAR); 
        int thisDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH); // 本月结束天数
        
        if(lastMonth!=12){
        	cal.set(thisYear, lastMonth, thisDay);
        }else{
        	cal.set(thisYear-1, lastMonth, thisDay);
        }
   
        return cal;

	}
	/**
	 * 特定日期的当月第一天
	 * @param date
	 * @return
	 */
	public static Calendar newThisMonth(Date date) {
	    Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int lastMonth = cal.get(Calendar.MONTH); // 上个月月份    
        int thisYear = cal.get(Calendar.YEAR); 
        int thisDay1 = cal.getActualMinimum(Calendar.DAY_OF_MONTH); // 本月结束天数
        //int thisDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH); // 本月结束天数
        
        cal.set(thisYear, lastMonth, thisDay1, 0, 0, 0);  //当月第一天
        
        return cal;

	}
	/**
	 * 特定日期的当旬第一天
	 * @param date
	 * @return
	 */
	public static Calendar newFirstTenDays(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int newDay = 0;
		if(day > 0 && day<=10)
			newDay = 1;
		if(day >10 && day<= 20)
			newDay = 11;
		if(day >20)
			newDay = 21;
		c.set(Calendar.DAY_OF_MONTH, newDay);
        return c;

	}
	/**
	 * 特定日期的当年第一天
	 * @param date
	 * @return
	 */
	public static Calendar newThisYear(Date date) {
	    Calendar cal = Calendar.getInstance();
        cal.setTime(date);   
        int thisYear = cal.get(Calendar.YEAR); 
        
        cal.set(thisYear, 0, 1 , 0, 0, 0);  //1-1
        
        return cal;

	 }
	/**
	 * 特定日期的本季度第一天
	 * @param date
	 * @return
	 */
	public static Calendar newThisSeason(Date date) {
	    Calendar cal = Calendar.getInstance();
        cal.setTime(date);   
        int thisYear = cal.get(Calendar.YEAR); 
        int lastMonth = cal.get(Calendar.MONTH); // 上个月月份
        if(1 <= lastMonth && lastMonth< 3){
        	cal.set(thisYear, 0, 1,0, 0, 0);
        }else if(3 <= lastMonth && lastMonth< 6){
        	cal.set(thisYear, 3, 1,0, 0, 0);
        }else if(6 <= lastMonth && lastMonth< 9){
        	cal.set(thisYear, 6, 1,0, 0, 0);
        }else if(9 <= lastMonth && lastMonth< 12){
        	cal.set(thisYear, 9, 1,0, 0, 0);
        }else if(lastMonth ==12){
        	cal.set(thisYear, 0, 1,0, 0, 0);
        }
       
        
        return cal;

	 }
	/**
	 * 获取指定日期的上个月的最后一个日期数
	 * @param date
	 * @return
	 */
	public static int getLastMonthDay(Date date) {
	    Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        
        cal.add(Calendar.MONTH,-1); 
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        
        System.out.println("lastMonthDay:"+lastDay);
        
        return lastDay;
	}
	/**
	 * 获取指定日期的本月的最后一个日期数
	 * @param date
	 * @return
	 */
	public static int getCurrentMonthDay(Date date) {
	    Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        
        /*int lastMonth = cal.get(Calendar.MONTH); // 上个月月份    
        int thisYear = cal.get(Calendar.YEAR); 
        int thisDay1 = cal.getActualMinimum(Calendar.DAY_OF_MONTH);// 起始天数
        int thisDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH); // 结束天数 
        */
        int last = cal.getActualMaximum(Calendar.DATE);
        
        return last;
	}
	
	/**
	 * 获取指定日期的本旬的最后一个日期数
	 * @param date
	 * @return
	 */
	public static int getTenDaysLastDayCount(Date date) {
	    Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int last = 0;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		if(day >0 && day <=10)
			last = 10;
		if(day >10 && day <=20)
			last = 20;
		if(day>20)
			last = DateTools.getCurrentMonthDay(date);
		return last;

	}
	
	/**
	 * 获取指定日期的本旬的最后一天
	 * @param date
	 * @return
	 */
	public static Calendar getTenDaysLastDay(Date date) {
	    Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int last = 0;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		if(day >0 && day <=10)
			last = 10;
		if(day >10 && day <=20)
			last = 20;
		if(day>20)
			last = DateTools.getCurrentMonthDay(date);
	    cal.set(Calendar.DAY_OF_MONTH, last);
		return cal;

	}
	/**
	 * 获取本年最后一天
	 * @param date
	 * @return
	 */
	public static Calendar getThisYearEndDay(Date date){
	 	Calendar c = Calendar.getInstance();
	 	c.setTime(date); 
        int currentYear = c.get(Calendar.YEAR);
        try {
            c.set(Calendar.YEAR, currentYear);
            c.set(Calendar.MONTH, 11);
            c.set(Calendar.DATE, 31);
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND,59);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
	}
	/**
	 * 获取本季度最后一天
	 * @param date
	 * @return
	 */
	public static Calendar getThisSeasonEndDay(Date date){
	 	Calendar c = Calendar.getInstance();
	 	c.setTime(date); 
        int currentMonth = c.get(Calendar.MONTH) + 1;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 8);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND,59);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
	}
	/**
	 * 如果date小于dateUpper,返回date，否则返回dateUpper
	 * @param dat
	 * @param dateUpper
	 * @return
	 */
	public static Calendar getUpperData(Date date, Date dateUpper){
		Calendar c = Calendar.getInstance();
		if(date.getTime() < dateUpper.getTime()){
			c.setTime(date);
		}else{
			c.setTime(dateUpper);
		}
		return c;
	}
	/**
	 * 比较年月是否相同
	 * 
	 * @param startDate开始日期
	 * @param endDate结束日期
	 * @return
	 */
	public static boolean isMonthCompare(Date startDate, Date endDate) {
		if (startDate != null && endDate != null) {
			if (dateToString(startDate, "yyyy-MM").equals(dateToString(endDate, "yyyy-MM"))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 返回日期后num个月
	 * 
	 * @param date日期
	 * @param num月数
	 * @param repaymentDate约定还款日
	 * @return
	 */
	public static Date getDateYYYYMMDD(Date date, Integer num, Integer repaymentDate) {
		if (date == null || num == null || repaymentDate == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int cnt = DateTools.getActualMaximum(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1 + num);
		if (cnt < repaymentDate.intValue()) {
			repaymentDate = cnt;
		}
		date = getDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1 + num, repaymentDate);
		return date;
	}

	/**
	 * 字串转成时间
	 * 
	 * @param year年
	 * @param month月
	 * @param day日
	 * @return
	 */
	public static Date getDate(int year, int month, int day) {
		StringBuffer sb = new StringBuffer();
		sb.append(year).append("-");
		if (month < 10 && month > 0) {
			sb.append(0).append(month);
		} else {
			sb.append(month);
		}
		sb.append("-");
		if (day < 10 && day > 0) {
			sb.append(0).append(day);
		} else {
			sb.append(day);
		}
		return DateTools.stringToDate(sb.toString(), "yyyy-MM-dd");
	}
	/**
	 * 返回当前时间的下一天
	 * 
	 * @param sysDate
	 * @return
	 */
	public static Date getNextDateYYYYMMDD(Date sysDate) {
		Calendar calendar = Calendar.getInstance();
		if (sysDate == null) {
			calendar.setTime(new Date());
		} else {
			calendar.setTime(sysDate);
		}
		return getDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DATE) + 1);
	}
	public static void main(String[] args) {

		System.out.println(DateTools.getLastMonthDay());
	}
}
