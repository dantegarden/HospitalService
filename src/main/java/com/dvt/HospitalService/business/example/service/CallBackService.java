package com.dvt.HospitalService.business.example.service;

import java.io.IOException;

public interface CallBackService {
	public void invokeOdooInPost(String backJson, Integer index, String message) throws IOException;
}
