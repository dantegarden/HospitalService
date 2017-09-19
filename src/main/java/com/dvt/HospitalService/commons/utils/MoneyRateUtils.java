package com.dvt.HospitalService.commons.utils;

import java.math.BigDecimal;

public class MoneyRateUtils {
	
	/**
	 * 计算年利率
	 * @param benchmarkIr	基准利率
	 * @param floatIr		浮动利率
	 * @return
	 */
	public static BigDecimal countYearIr(BigDecimal benchmarkIr,BigDecimal floatIr){
		return MathUtil.BDmultiply(benchmarkIr,floatIr, 6);
	}
	public static void main(String[] args) {
		
		
	}
}
