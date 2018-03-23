package com.dvt.HospitalService.business.example.dto;

import java.util.List;

/**
 * 9 发票代码 1 10 发票号码 2 11 开票时间 3 12 校验码 4 13 机器编号 5 14 购买方 名称 6 15 纳税人识别号 7
 * 16 地址、电话 8 17 开户行及账号 9 18 货物或应税劳务、服务名称 10 19 规格型号 11 20 单位 12 21 数量 13 22
 * 单价 14 23 金额 15 24 税率 16 25 税额 17 26 金额(带单位) 18 27 税额(带单位) 19 28 价税合计大写 20
 * 29 30 价税合计小写 22 31 销售方：名称 23 32 销售方：纳税人识别号 24 33 销售方：地址、电话：25 34
 * 销售方：开户行及账号 26
 * 
 * ****/
public class SaxMsg {
	public String fpdm;
	public String fphm;
	public String kpsj;
	public String jym;
	public String jqhm;
	public String gmf_mc;
	public String gmf_nsrsbh;
	public String gmf_dzdh;
	public String gmf_khhjzh;
	public String ysmc;//应税货物劳务服务名称
	public String ggxh;//规格型号
	public String dw;//单位
	public String sl;//数量
	public String dj;//单价
	public String je;//金额
	public String slv;//税率
	public String se;//税额
	public String je_dw;
	public String se_dw;
	public String jshjdx;
	public String jshjxx;
	public String xsf_mc;
	public String xsf_nsrsbh;
	public String xsf_dzdh;
	public String xsf_khhjzh;
	public String fp_pic;
	public String fp_base64;
	public String fpxq_pic;
	public String fpxq_base64;
	public Boolean isValid = Boolean.TRUE;
	/**卷票**/
	public String jdhm;
	public String shy;//收货员
	public String je_dx;//金额大写
	public String je_xx;//金额小写
	public List<String> bz;//备注
	
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
	public String getKpsj() {
		return kpsj;
	}
	public void setKpsj(String kpsj) {
		this.kpsj = kpsj;
	}
	public String getJym() {
		return jym;
	}
	public void setJym(String jym) {
		this.jym = jym;
	}
	public String getJqhm() {
		return jqhm;
	}
	public void setJqhm(String jqhm) {
		this.jqhm = jqhm;
	}
	public String getGmf_mc() {
		return gmf_mc;
	}
	public void setGmf_mc(String gmf_mc) {
		this.gmf_mc = gmf_mc;
	}
	public String getGmf_nsrsbh() {
		return gmf_nsrsbh;
	}
	public void setGmf_nsrsbh(String gmf_nsrsbh) {
		this.gmf_nsrsbh = gmf_nsrsbh;
	}
	public String getGmf_dzdh() {
		return gmf_dzdh;
	}
	public void setGmf_dzdh(String gmf_dzdh) {
		this.gmf_dzdh = gmf_dzdh;
	}
	public String getGmf_khhjzh() {
		return gmf_khhjzh;
	}
	public void setGmf_khhjzh(String gmf_khhjzh) {
		this.gmf_khhjzh = gmf_khhjzh;
	}
	public String getYsmc() {
		return ysmc;
	}
	public void setYsmc(String ysmc) {
		this.ysmc = ysmc;
	}
	public String getGgxh() {
		return ggxh;
	}
	public void setGgxh(String ggxh) {
		this.ggxh = ggxh;
	}
	public String getDw() {
		return dw;
	}
	public void setDw(String dw) {
		this.dw = dw;
	}
	public String getSl() {
		return sl;
	}
	public void setSl(String sl) {
		this.sl = sl;
	}
	public String getDj() {
		return dj;
	}
	public void setDj(String dj) {
		this.dj = dj;
	}
	public String getJe() {
		return je;
	}
	public void setJe(String je) {
		this.je = je;
	}
	public String getSlv() {
		return slv;
	}
	public void setSlv(String slv) {
		this.slv = slv;
	}
	public String getSe() {
		return se;
	}
	public void setSe(String se) {
		this.se = se;
	}
	public String getJe_dw() {
		return je_dw;
	}
	public void setJe_dw(String je_dw) {
		this.je_dw = je_dw;
	}
	public String getSe_dw() {
		return se_dw;
	}
	public void setSe_dw(String se_dw) {
		this.se_dw = se_dw;
	}
	public String getJshjdx() {
		return jshjdx;
	}
	public void setJshjdx(String jshjdx) {
		this.jshjdx = jshjdx;
	}
	public String getJshjxx() {
		return jshjxx;
	}
	public void setJshjxx(String jshjxx) {
		this.jshjxx = jshjxx;
	}
	public String getXsf_mc() {
		return xsf_mc;
	}
	public void setXsf_mc(String xsf_mc) {
		this.xsf_mc = xsf_mc;
	}
	public String getXsf_nsrsbh() {
		return xsf_nsrsbh;
	}
	public void setXsf_nsrsbh(String xsf_nsrsbh) {
		this.xsf_nsrsbh = xsf_nsrsbh;
	}
	public String getXsf_dzdh() {
		return xsf_dzdh;
	}
	public void setXsf_dzdh(String xsf_dzdh) {
		this.xsf_dzdh = xsf_dzdh;
	}
	public String getXsf_khhjzh() {
		return xsf_khhjzh;
	}
	public void setXsf_khhjzh(String xsf_khhjzh) {
		this.xsf_khhjzh = xsf_khhjzh;
	}
	public String getFp_pic() {
		return fp_pic;
	}
	public void setFp_pic(String fp_pic) {
		this.fp_pic = fp_pic;
	}
	
	public String getFp_base64() {
		return fp_base64;
	}
	public void setFp_base64(String fp_base64) {
		this.fp_base64 = fp_base64;
	}
	
	public void checkFpmx(){
		if(this.getGgxh().startsWith("null&&")
		   &&this.getDw().startsWith("null&&")
		   &&this.getSl().startsWith("null&&")
		   &&this.getDj().startsWith("null&&")
		   &&this.getJe().startsWith("null&&")
		   &&this.getSlv().startsWith("null&&")
		   &&this.getSe().startsWith("null&&")
		   &&this.getYsmc().startsWith("null&&")){
			setYsmc(ysmc.substring(6));
			setSl(sl.substring(6));
			setDj(dj.substring(6));
			setJe(je.substring(6));
			setSlv(slv.substring(6));
			setSe(se.substring(6));
			setGgxh(ggxh.substring(6));
			setDw(dw.substring(6));
		}
	}
	
	public SaxMsg(String fpdm, String fphm, String kpsj, String jym,
			String jqhm, String gmf_mc, String gmf_nsrsbh, String gmf_dzdh,
			String gmf_khhjzh, String ysmc, String ggxh, String dw, String sl,
			String dj, String je, String slv, String se, String je_dw,
			String se_dw, String jshjdx, String jshjxx, String xsf_mc,
			String xsf_nsrsbh, String xsf_dzdh, String xsf_khhjzh,
			String fp_pic, String fp_base64, String fpxq_pic,
			String fpxq_base64, Boolean isValid) {
		super();
		this.fpdm = fpdm;
		this.fphm = fphm;
		this.kpsj = kpsj;
		this.jym = jym;
		this.jqhm = jqhm;
		this.gmf_mc = gmf_mc;
		this.gmf_nsrsbh = gmf_nsrsbh;
		this.gmf_dzdh = gmf_dzdh;
		this.gmf_khhjzh = gmf_khhjzh;
		this.ysmc = ysmc;
		this.ggxh = ggxh;
		this.dw = dw;
		this.sl = sl;
		this.dj = dj;
		this.je = je;
		this.slv = slv;
		this.se = se;
		this.je_dw = je_dw;
		this.se_dw = se_dw;
		this.jshjdx = jshjdx;
		this.jshjxx = jshjxx;
		this.xsf_mc = xsf_mc;
		this.xsf_nsrsbh = xsf_nsrsbh;
		this.xsf_dzdh = xsf_dzdh;
		this.xsf_khhjzh = xsf_khhjzh;
		this.fp_pic = fp_pic;
		this.fp_base64 = fp_base64;
		this.fpxq_pic = fpxq_pic;
		this.fpxq_base64 = fpxq_base64;
		this.isValid = isValid;
	}
	public SaxMsg() {
		super();
	}
	public Boolean getIsValid() {
		return isValid;
	}
	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}
	public String getFpxq_pic() {
		return fpxq_pic;
	}
	public void setFpxq_pic(String fpxq_pic) {
		this.fpxq_pic = fpxq_pic;
	}
	public String getFpxq_base64() {
		return fpxq_base64;
	}
	public void setFpxq_base64(String fpxq_base64) {
		this.fpxq_base64 = fpxq_base64;
	}
	public String getJdhm() {
		return jdhm;
	}
	public void setJdhm(String jdhm) {
		this.jdhm = jdhm;
	}
	public String getShy() {
		return shy;
	}
	public void setShy(String shy) {
		this.shy = shy;
	}
	public String getJe_dx() {
		return je_dx;
	}
	public void setJe_dx(String je_dx) {
		this.je_dx = je_dx;
	}
	public String getJe_xx() {
		return je_xx;
	}
	public void setJe_xx(String je_xx) {
		this.je_xx = je_xx;
	}
	public List<String> getBz() {
		return bz;
	}
	public void setBz(List<String> bz) {
		this.bz = bz;
	}
	
	
	
	
	
}
