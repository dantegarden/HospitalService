package com.dvt.HospitalService.business.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dvt.HospitalService.commons.GlobalConstants;



@Controller
@RequestMapping("/test")
public class TestController {
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);
	
//	@Autowired
//	private TestService testService;
//	@Autowired
//	private TestSqliteService testSqliteService;
	
	@RequestMapping
	public String init() {
		System.out.println("test initting");
		
		return GlobalConstants.PAGE_TEST;
	}
}
