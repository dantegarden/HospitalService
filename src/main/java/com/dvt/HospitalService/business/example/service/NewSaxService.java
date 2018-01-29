package com.dvt.HospitalService.business.example.service;

import org.openqa.selenium.WebDriver;

import com.dvt.HospitalService.business.example.dto.SaxResult;

public interface NewSaxService {
	
	public SaxResult checkFpEffect(String ewmJson,String index) throws Exception;
	
	public WebDriver getFireFoxDriver();
}
