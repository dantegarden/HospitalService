package com.dvt.HospitalService.commons.utils;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Maps;

public class ServletUtils {
	
	/**
	 * 传递的参数个数不固定且参数名有重复的
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String[]> getParameterValuesMap(HttpServletRequest request){
		Map<String, String[]> map = Maps.newHashMap();
		Enumeration<String> enums=request.getParameterNames();
		while (enums.hasMoreElements()) {
			String paramName =enums.nextElement();
			String[]   values=request.getParameterValues(paramName);
			map.put(paramName, values);
		}
		return map;
	}
	/**
	 * 传递的参数个数不固定且参数名没有重复的<br>
	 * 注:得到枚举类型的参数名称，参数名称若有重复的只能得到第一个
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getParameterValueMap(HttpServletRequest request){
		Map<String, String> map = Maps.newHashMap();
		Enumeration<String> enums=request.getParameterNames();
		while (enums.hasMoreElements()) {
			String paramName =enums.nextElement();
			String   value=request.getParameter(paramName);
			map.put(paramName, value);
		}
		return map;
	}
	
}
