package com.dvt.HospitalService.business.example.dto;

public class Fpxx {
	public String id;
	public String fpdm;
	public String fphm;
	public String kprq;
	public String kjje;
	public String getFpdm() {
		return fpdm;
	}
	public void setFpdm(String fpdm) {
		this.fpdm = fpdm;
	}
	public String getFphm() {
		return fphm;
	}
	public void setFphm(String fphm) {
		this.fphm = fphm;
	}
	public String getKprq() {
		return kprq;
	}
	public void setKprq(String kprq) {
		this.kprq = kprq;
	}
	public String getKjje() {
		return kjje;
	}
	public void setKjje(String kjje) {
		this.kjje = kjje;
	}
	
	public Fpxx(String id, String fpdm, String fphm, String kprq, String kjje) {
		super();
		this.id = id;
		this.fpdm = fpdm;
		this.fphm = fphm;
		this.kprq = kprq;
		this.kjje = kjje;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Fpxx() {
		super();
	}
	
	
}
