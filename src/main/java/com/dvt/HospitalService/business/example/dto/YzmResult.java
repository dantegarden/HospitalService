package com.dvt.HospitalService.business.example.dto;

public class YzmResult {
	public String err_no;
	public String err_str;
	public String pic_id;
	public String pic_str;
	public String md5;
	public String str_debug;
	public String getErr_no() {
		return err_no;
	}
	public void setErr_no(String errNo) {
		err_no = errNo;
	}
	public String getErr_str() {
		return err_str;
	}
	public void setErr_str(String errStr) {
		err_str = errStr;
	}
	public String getPic_id() {
		return pic_id;
	}
	public void setPic_id(String picId) {
		pic_id = picId;
	}
	public String getPic_str() {
		return pic_str;
	}
	public void setPic_str(String picStr) {
		pic_str = picStr;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public String getStr_debug() {
		return str_debug;
	}
	public void setStr_debug(String strDebug) {
		str_debug = strDebug;
	}
	private YzmResult(String errNo, String errStr, String picId, String picStr,
			String md5, String strDebug) {
		super();
		err_no = errNo;
		err_str = errStr;
		pic_id = picId;
		pic_str = picStr;
		this.md5 = md5;
		str_debug = strDebug;
	}
	private YzmResult() {
		super();
	}
	
	
}
