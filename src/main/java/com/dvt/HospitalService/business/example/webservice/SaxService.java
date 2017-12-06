package com.dvt.HospitalService.business.example.webservice;

import javax.jws.WebService;

@WebService
public interface SaxService {
	
	public String sayHello(String name);
	
	public String checkUserScore();
	
	public String checkFpEffect(String ewmJson);
	
	
}
