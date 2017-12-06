package com.dvt.HospitalService.business.example.webservice;

import javax.jws.WebService;

@WebService
public interface PublishService {
	
	public String publish2MQ(String ewmJson);
}
