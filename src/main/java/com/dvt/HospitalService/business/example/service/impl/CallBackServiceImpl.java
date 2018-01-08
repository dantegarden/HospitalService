package com.dvt.HospitalService.business.example.service.impl;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dvt.HospitalService.business.example.dto.PassResult;
import com.dvt.HospitalService.business.example.service.CallBackService;
import com.dvt.HospitalService.commons.utils.HttpHelper;
import com.dvt.HospitalService.commons.utils.JsonUtils;
import com.google.common.collect.ImmutableMap;

public class CallBackServiceImpl implements CallBackService{
	private static final Logger logger = LoggerFactory.getLogger(CallBackServiceImpl.class);
	
	private final static String SYS_HOSTNAME;
	private final static String ODOO_INTERFACE;
	
	static{
		Properties prop = new Properties();
		try {
			prop.load(CallBackServiceImpl.class.getClassLoader().getResourceAsStream("deploy.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ODOO_INTERFACE = prop.getProperty("odoo.interface");
		SYS_HOSTNAME = InitServiceImpl.getHostNameForLiunx();
	}
	
	@Override
	public void invokeOdooInPost(String backJson, Integer index, String message) throws IOException {
		Map<String,String> params = new ImmutableMap.Builder<String,String>()
				.put("result", backJson)
				.build();
		logger.info(SYS_HOSTNAME + " Worker ["+index+"] said : Pass Message To Odoo ["+message+"] ");
		String passJson = HttpHelper.startPost(ODOO_INTERFACE, params);
		PassResult pr = JsonUtils.jsonToJavaBean(passJson, PassResult.class);
		if(pr.getState().equals("0")){
			logger.info(SYS_HOSTNAME + " Worker ["+index+"] said : Pass Message Over ["+message+"] ");
		}
		logger.info(SYS_HOSTNAME + " Worker ["+index+"] said : 线程["+Thread.currentThread().getId()+"]关闭");
	}
	
}
