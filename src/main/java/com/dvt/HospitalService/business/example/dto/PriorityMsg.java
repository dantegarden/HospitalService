package com.dvt.HospitalService.business.example.dto;

public class PriorityMsg {
	private String msg;
	private int priority;
	public PriorityMsg(String msg, int priority) {
		super();
		this.msg = msg;
		this.priority = priority;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public PriorityMsg() {
		super();
	}
	
	
}
