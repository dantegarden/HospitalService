package com.dvt.HospitalService.commons.utils;

import java.math.BigDecimal;
import java.text.Format;

import org.apache.commons.lang3.StringUtils;


/**
 * NumberUtil 数字工具类
 */


public class NumberUtil {
	
	public static BigDecimal parseBigDecimal(Object o){
		if(o==null){
			return null;
		}
		String className=o.getClass().getName();
		
		if ("java.lang.String".equals(className)) {
            return parseBigDecimal((String)o);
        } 
		if ("java.lang.Short".equals(className)) {
            return parseBigDecimal((Short)o);
        }
		if ("java.lang.Integer".equals(className)) {
            return parseBigDecimal((Integer)o);
        }
		if ("java.lang.Long".equals(className)) {
            return parseBigDecimal((Long)o);
        } 
		if ("java.lang.Float".equals(className)) {
            return parseBigDecimal((Float)o);
        }
		if ("java.lang.Double".equals(className)) {
            return parseBigDecimal((Double)o);
        } 
		if ("java.math.BigDecimal".equals(className)) {
            return ((BigDecimal)o);
        } 
		
		return null;
		
	}
	
	public static BigDecimal parseBigDecimal(String s){
		if(StringUtils.isBlank(s)){
			return null;
		}
		return new BigDecimal(StringUtils.trim(s));
	}
	public static BigDecimal parseBigDecimal(Short s){
		
		if(s==null){
			return null;
		}
		return new BigDecimal(s.shortValue());
	}
	public static BigDecimal parseBigDecimal(Integer i){
		
		if(i==null){
			return null;
		}
		return new BigDecimal(i.intValue());
	}
	
	public static BigDecimal parseBigDecimal(Long l){
		
		if(l==null){
			return null;
		}
		return new BigDecimal(l.longValue());
	}
	
	public static BigDecimal parseBigDecimal(Float f){
		
		if(f==null){
			return null;
		}
		return new BigDecimal(f.floatValue());
	}
	
	public static BigDecimal parseBigDecimal(Double d){
		
		if(d==null){
			return null;
		}
		return new BigDecimal(d.doubleValue());
	}
	public static BigDecimal parseBigDecimal(BigDecimal bd){
		
		return bd;
	}
	
	
	public static Double parseDouble(Object o) {
		if (o == null) {
			return null;
		}
		String className = o.getClass().getName();

		if ("java.lang.String".equals(className)) {
			return parseDouble((String) o);
		}
		if ("java.lang.Short".equals(className)) {
			return parseDouble((Short) o);
		}

		if ("java.lang.Integer".equals(className)) {
			return parseDouble((Integer) o);
		}
		if ("java.lang.Long".equals(className)) {
			return parseDouble((Long) o);
		}
		if ("java.lang.Float".equals(className)) {
			return parseDouble((Float) o);
		}
		if ("java.lang.Double".equals(className)) {
			return ((Double) o);
		}
		if ("java.math.BigDecimal".equals(className)) {
			return parseDouble((BigDecimal) o);
		}

		return null;

	}

	public static Double parseDouble(String s) {
		if (StringUtils.isBlank(s)) {
			return null;
		}
		return new Double(StringUtils.trim(s));
	}

	public static Double parseDouble(Short s) {

		if (s == null) {
			return null;
		}
		return new Double(s.shortValue());
	}

	public static Double parseDouble(Integer i) {

		if (i == null) {
			return null;
		}
		return new Double(i.intValue());
	}

	public static Double parseDouble(Long l) {

		if (l == null) {
			return null;
		}
		return new Double(l.longValue());
	}

	public static Double parseDouble(Float f) {

		if (f == null) {
			return null;
		}
		return new Double(f.floatValue());
	}

	public static Double parseDouble(Double d) {

		return d;
	}

	public static Double parseDouble(BigDecimal bd) {

		if (bd == null) {
			return null;
		}
		return new Double(bd.doubleValue());
	}

	
	
	public static Float parseFloat(Object o) {
		if (o == null) {
			return null;
		}
		String className = o.getClass().getName();

		if ("java.lang.String".equals(className)) {
			return parseFloat((String) o);
		}
		if ("java.lang.Short".equals(className)) {
			return parseFloat((Short) o);
		}
		if ("java.lang.Integer".equals(className)) {
			return parseFloat((Integer) o);
		}
		if ("java.lang.Long".equals(className)) {
			return parseFloat((Long) o);
		}
		if ("java.lang.Float".equals(className)) {
			return ((Float) o);
		}
		if ("java.lang.Double".equals(className)) {
			return parseFloat((Double) o);
		}
		if ("java.math.BigDecimal".equals(className)) {
			return parseFloat((BigDecimal) o);
		}

		return null;

	}

	public static Float parseFloat(String s) {
		if (StringUtils.isBlank(s)) {
			return null;
		}
		return new Float(StringUtils.trim(s));
	}

	public static Float parseFloat(Short s) {

		if (s == null) {
			return null;
		}
		return new Float(s.shortValue());
	}

	public static Float parseFloat(Integer i) {

		if (i == null) {
			return null;
		}
		return new Float(i.intValue());
	}

	public static Float parseFloat(Long l) {

		if (l == null) {
			return null;
		}
		return new Float(l.longValue());
	}

	public static Float parseFloat(Float f) {

		return f;
	}

	public static Float parseFloat(Double d) {

		if (d == null) {
			return null;
		}
		return new Float(d.doubleValue());
	}

	public static Float parseFloat(BigDecimal bd) {

		if (bd == null) {
			return null;
		}
		return new Float(bd.floatValue());
	}
	
	
	
	public static Integer parseInteger(Object o) {
		if (o == null) {
			return null;
		}
		String className = o.getClass().getName();

		if ("java.lang.String".equals(className)) {
			return parseInteger((String) o);
		}

		if ("java.lang.Short".equals(className)) {
			return parseInteger((Short) o);
		}
		if ("java.lang.Integer".equals(className)) {
			return ((Integer) o);
		}

		if ("java.lang.Long".equals(className)) {
			return parseInteger((Long) o);
		}
		if ("java.lang.Float".equals(className)) {
			return parseInteger((Float) o);
		}
		if ("java.lang.Double".equals(className)) {
			return parseInteger((Double) o);
		}

		if ("java.math.BigDecimal".equals(className)) {
			return parseInteger((BigDecimal) o);
		}
		return null;

	}

	public static Integer parseInteger(String s) {
		if (StringUtils.isBlank(s)) {
			return null;
		}
		return new Integer(StringUtils.trim(s));
	}

	public static Integer parseInteger(Short s) {

		if (s == null) {
			return null;
		}
		return new Integer(s.shortValue());
	}

	public static Integer parseInteger(Integer i) {

		return i;
	}

	public static Integer parseInteger(Long l) {

		if (l == null) {
			return null;
		}
		return new Integer(l.intValue());
	}

	public static Integer parseInteger(Float f) {

		if (f == null) {
			return null;
		}
		return new Integer(f.intValue());
	}

	public static Integer parseInteger(Double d) {

		if (d == null) {
			return null;
		}
		return new Integer(d.intValue());
	}

	public static Integer parseInteger(BigDecimal bd) {

		if (bd == null) {
			return null;
		}
		return new Integer(bd.intValue());
	}

	
	public static Long parseLong(Object o) {
		if (o == null) {
			return null;
		}
		String className = o.getClass().getName();

		if ("java.lang.String".equals(className)) {
			return parseLong((String) o);
		}
		if ("java.lang.Short".equals(className)) {
			return parseLong((Short) o);
		}
		if ("java.lang.Integer".equals(className)) {
			return parseLong((Integer) o);
		}
		if ("java.lang.Long".equals(className)) {
			return (Long) o;
		}
		if ("java.lang.Float".equals(className)) {
			return parseLong((Float) o);
		}
		if ("java.lang.Double".equals(className)) {
			return parseLong((Double) o);
		}

		if ("java.math.BigDecimal".equals(className)) {
			return parseLong((BigDecimal) o);
		}

		return null;

	}

	public static Long parseLong(String s) {
		if (StringUtils.isBlank(s)) {
			return null;
		}
		return new Long(Long.parseLong(StringUtils.trim(s)));
	}

	public static Long parseLong(Short s) {

		if (s == null) {
			return null;
		}
		return new Long(s.longValue());
	}

	public static Long parseLong(Integer i) {

		if (i == null) {
			return null;
		}
		return new Long(i.longValue());
	}

	public static Long parseLong(Long l) {

		return l;
	}

	public static Long parseLong(Float f) {

		if (f == null) {
			return null;
		}
		return new Long(f.longValue());
	}

	public static Long parseLong(Double d) {

		if (d == null) {
			return null;
		}
		return new Long(d.longValue());
	}

	public static Long parseLong(BigDecimal bd) {

		if (bd == null) {
			return null;
		}
		return new Long(bd.longValue());
	}
	
	public static Short parseShort(Object o) {
		if (o == null) {
			return null;
		}
		String className = o.getClass().getName();

		if ("java.lang.String".equals(className)) {
			return parseShort((String) o);
		}
		if ("java.lang.Short".equals(className)) {
			return ((Short) o);
		}
		if ("java.lang.Integer".equals(className)) {
			return parseShort((Integer) o);
		}
		if ("java.lang.Long".equals(className)) {
			return parseShort((Long) o);
		}
		if ("java.lang.Float".equals(className)) {
			return parseShort((Float) o);
		}

		if ("java.lang.Double".equals(className)) {
			return parseShort((Double) o);
		}

		if ("java.lang.BigDecimal".equals(className)) {
			return parseShort((BigDecimal) o);
		}

		return null;

	}

	public static Short parseShort(String s) {
		if (StringUtils.isBlank(s)) {
			return null;
		}
		return new Short(StringUtils.trim(s));
	}

	public static Short parseShort(Short s) {

		return s;
	}

	public static Short parseShort(Integer i) {

		if (i == null) {
			return null;
		}
		return new Short(i.shortValue());
	}

	public static Short parseShort(Long l) {

		if (l == null) {
			return null;
		}
		return new Short(l.shortValue());
	}

	public static Short parseShort(Float f) {

		if (f == null) {
			return null;
		}
		return new Short(f.shortValue());
	}

	public static Short parseShort(Double d) {

		if (d == null) {
			return null;
		}
		return new Short(d.shortValue());
	}

	public static Short parseShort(BigDecimal bd) {

		if (bd == null) {
			return null;
		}
		return new Short(bd.shortValue());
	}
	/**
	 * Double类型数据转换成double类型数据
	 *
	 * @param d  -Double类型数据
	 * @return double类型数据
	 */
	public static double doubleValue(Double d)
	{
		if (d == null)
			return 0.0;
		else
			return d.doubleValue();
	}
	/**
     * Integer类型数据转换成int类型数据
     *
     * @param i  -Integer类型数据
     * @return int类型数据
     * @since
     */
    public static int intValue(Integer i) {
        if (i == null)
            return 0;
        else
            return i.intValue();
    }	
    /**
     * Short类型数据转换成short类型数据
     *
     * @param s  -Short类型数据
     * @return short类型数据
     * @since
     */
    public static short shortValue(Short s) {
        short sh =0;
        if (s != null)
            return s.shortValue();
        else
            return sh;
    }
    /**
     * Long类型数据转换成long类型数据
     * @param l
     * @return
     */
    public static long longValue(Long l){
    	if(l!=null)
    		return l.longValue();
    	else
    		return 0;
    }
    /**
     * Float类型数据转换成float类型数据
     * @param f
     * @return
     */
    public static float floatValue(Float f){
    	if(f!=null)
    		return f.floatValue();
    	else
    		return 0;
    }

    /**
     * 判断对象是否为空或零
     *
     * @param obj -对象
     * @return boolean  true:为空或零 false 不为空或零
     * @since
     */
    public static boolean isNullOrZero(Object obj){
        if(obj==null)
            return true;
        String str= obj.toString();
        if(str.equals("")){
            return true;
        }else{
            BigDecimal bd=new BigDecimal(str).setScale(10);
            BigDecimal zero=new BigDecimal("0.0").setScale(10);
            return bd.compareTo(zero)==0;
        }
    }   
	/**
	 * 比较Long类型的值是否相等
	 * @param a
	 * @param b
	 * @return
	 */
    public static boolean compareLong(Long a, Long b) {
		boolean result = false;
			if (a == null || b == null)
				return result;

			if (a.longValue() == b.longValue())
				result = true;
		return result;
	} 
	/**
	 * Double,Float空值处理
	 * @param d Object(Double,Float)
	 * @param defaultValue double
	 * @return double
	 */
	public static double getDoubleValue(Object d,double defaultValue){
		if(d==null){
			return defaultValue;
		}else{
			if(d instanceof Double)
				return ((Double)d).doubleValue();
			else if(d instanceof Float)
				return Double.parseDouble(d.toString());
			else
				return defaultValue;
		}
	}	
	/**
	 * Long,Integer空值处理
	 * @param d 
	 * @param defaultValue int
	 * @return int
	 */
	public static int getIntValue(Object d,int defaultValue){
		if(d==null){
			return defaultValue;
		}else{
			if(d instanceof Long)
				return ((Long)d).intValue();
			else if(d instanceof Integer)
				return ((Integer)d).intValue();
			else
				return defaultValue;
		}
	}
	
	/**
	 * 判断String 是否可以转换为Integer
	 * @param str
	 * @return
	 */
	public static boolean isIntegerToString(String str){
		boolean bean=false;
		try{
			Integer.parseInt(str);
			bean=true;
			
		}catch(NumberFormatException e){
			return bean;
		}
		return bean;
	}
	/**
	 * 格式化数字
	 */
	public static BigDecimal formatDouble(Double d) {
		Format format = new java.text.DecimalFormat("#0.00");
		return new BigDecimal(format.format(d));
	}
}
