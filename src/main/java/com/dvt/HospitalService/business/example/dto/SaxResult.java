package com.dvt.HospitalService.business.example.dto;

public class SaxResult {
	public String status;
	public String des;
	public SaxMsg saxmsg;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public SaxMsg getSaxmsg() {
		return saxmsg;
	}
	public void setSaxmsg(SaxMsg saxmsg) {
		this.saxmsg = saxmsg;
	}
	
	public SaxResult() {
		super();
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public SaxResult(String status, String des, SaxMsg saxmsg) {
		super();
		this.status = status;
		this.des = des;
		this.saxmsg = saxmsg;
	}
	
	
}
