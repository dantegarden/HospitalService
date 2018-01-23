package com.dvt.HospitalService.business.example.service;

import com.dvt.HospitalService.business.example.dto.SaxResult;

public interface NewSaxService {
	
	public SaxResult checkFpEffect(String ewmJson,String index) throws Exception;
	
}
