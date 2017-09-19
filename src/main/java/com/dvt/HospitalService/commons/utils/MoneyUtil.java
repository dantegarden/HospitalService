package com.dvt.HospitalService.commons.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;


/**
 * <p>Title: 功能类的名称:MoneyUtil.java </p>
 * <p>Description:人民币小写阿拉伯数字转大写</p>
 * @创建时间 2014-08-01
 */
public class MoneyUtil {   
	    private final static String[] CN_Digits = { "零", "壹", "貳", "叁", "肆", "伍",   
	            "陆", "柒", "捌", "玖", };   

	
	    /**
		 * 给金额加上千位分隔符(,)，小数点后保留两位小数
		 */
		public static String formatMoney(BigDecimal money){
			
			if(null == money){
				return null;
			}
			return formatMoney(money, 2);
		}
		
		/**
		 * 给金额加上千位分隔符(,)
		 */
		public static String formatMoney(BigDecimal money,int num){
			
			if(null == money){
				return null;
			}
			
			String lenthStr = null;
			if(num == 0){
				lenthStr = StringUtils.rightPad("###,##", 6, "0");
			}else{
				lenthStr = StringUtils.rightPad("###,##0.", 8 + num , "0");
			}
			
			Locale.setDefault(Locale.US);
			DecimalFormat usFormat = new DecimalFormat(lenthStr);
			return usFormat.format(money);
		}
	/**
	 * 剔除所有的金额千位分隔符(,)
	 */
	public static BigDecimal returnMoney(String money){
		
		if(StringUtils.isBlank(money)){
			return null;
		}
		//剔除所有的金额千位分隔符(,)
		Locale.setDefault(Locale.US);
		DecimalFormat usFormat = new DecimalFormat("###,##0.00");
		Double number = null;
		try {
			number = usFormat.parse(money).doubleValue();
			return BigDecimal.valueOf(number);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	    
	    /**  
	     * 将数字型货币转换为中文型货币 <br/>  
	     * @param moneyValue  
	     *            　字符串形式的金额，小数部分，将多于3位部分舍去，不做四舍五入  
	     * @return  
	     */  
	    
	    public static void main(String args[]){
	    	System.out.println(CNValueOf("8989.1")+"8989.1");
	    	System.out.println(formatMoney(new BigDecimal(-1231223.013)));
	    	System.out.println(formatMoney(new BigDecimal(0)));
	    	System.out.println(returnMoney("-23,123,123.003"));
	    }
	    
	    public static String CNValueOf(String moneyValue) {   
	        //使用正则表达式，去除前面的零及数字中的逗号   
	        String value = moneyValue.replaceFirst("^0+", "");   
	        value = value.replaceAll(",", "");   
	        //分割小数部分与整数部分   
	        int dot_pos = value.indexOf('.');   
	        String int_value;   
	        String fraction_value;   
	        if (dot_pos == -1) {   
	            int_value = value;   
	            fraction_value = "00";   
	        } else {   
	            int_value = value.substring(0, dot_pos);   
	            fraction_value = value.substring(dot_pos + 1, value.length())   
	                    + "00".substring(0, 2);//也加两个0，便于后面统一处理   
	        }   
  
	        int len = int_value.length();   
	        if (len>16){
	        	throw new IllegalArgumentException("数额过大暂不能实现大写转换");
	        }
	        StringBuffer cn_currency = new StringBuffer();   
	        String[] CN_Carry = new String[] { "", "万", "亿", "万" };   
	        //数字分组处理，计数组数   
	        int cnt = len/4+(len%4==0?0:1);   
	        //左边第一组的长度   
	        int partLen = len-(cnt-1)*4;   
	        String partValue=null;   
	        boolean bZero=false;//有过零   
	        String curCN=null;  
	        //youjunguo 20130315 add 如果还的钱类似为0.3或者0.03前面加零 start
	        if("".equals(int_value)){
	        	cn_currency.append("零");
	        }
	        for(int i =0; i<cnt; i++){   
	            partValue = int_value.substring(0,partLen);   
	            int_value=int_value.substring(partLen);   
	            curCN = Part2CN(partValue,i!=0&&!"零".equals(curCN));   
	            //System.out.println(partValue+":"+curCN);   
	            //若上次为零，这次不为零，则加入零             
	            if(bZero && !"零".equals(curCN)){   
	                cn_currency.append("零");   
	                bZero=false;   
	            }   
	            if("零".equals(curCN))bZero=true;   
	            //若数字不是零，加入中文数字及单位   
	            if(!"零".equals(curCN)){   
	                cn_currency.append(curCN);   
	                cn_currency.append(CN_Carry[cnt-1-i]);   
	            }   
	            //除最左边一组长度不定外，其它长度都为4   
	            partLen=4;   
	            partValue=null;   
	        }   
	       
	       //如果还的钱类似为0.3或者0.03前面加零 end
	        cn_currency.append("元");   
	        //处理小数部分   
	        int fv1 = Integer.parseInt(fraction_value.substring(0,1));   
	        int fv2 = Integer.parseInt(fraction_value.substring(1,2));   
	        if(fv1+fv2==0){   
	            cn_currency.append("整");   
	        }   
	        else{   
	            cn_currency.append(CN_Digits[fv1]).append("角");   
	            cn_currency.append(CN_Digits[fv2]).append("分");   
	        }
	        if(cn_currency.toString().endsWith("零元整")){
	        	cn_currency.replace(cn_currency.length()-3, cn_currency.length() - 2, "");
			}
	        return cn_currency.toString();   
	    }   
	  
	    /**  
	     * 将一组数字（不多于四个）转化成中文表示   	
	     *   
	     * @param partValue 字符串形式的数字  
	     * @param bInsertZero 是否在前面添加零  
	     */  
	    private static String Part2CN(String partValue,boolean bInsertZero) {   
	        //使用正则表达式，去除前面的0   
	        partValue = partValue.replaceFirst("^0+", "");   
	        int len = partValue.length();   
	        if (len == 0)   
	            return "零";   
	        StringBuffer sbResult = new StringBuffer();   
	        int digit;   
	        String[] CN_Carry = new String[] { "", "拾", "佰", "仟" };   
	        for (int i = 0; i < len; i++) {   
	            digit = Integer.parseInt(partValue.substring(i, i + 1));   
	            if (digit != 0) {   
	                sbResult.append(CN_Digits[digit]);   
	                sbResult.append(CN_Carry[len - 1 - i]);   
	            } else {   
	                // 若不是最后一位，且下不位不为零，追加零   
	                if (i != len - 1  
	                        && Integer.parseInt(partValue.substring(i + 1, i + 2)) != 0)   
	                    sbResult.append("零");   
	            }   
	        }   
	        if(bInsertZero && len!=4)
	        	sbResult.insert(0, "零");   
	        return sbResult.toString();   
	    }   
	    
	    
	    /**
	     * 将"0.00"格式的金额拆分，从分开始到千万，i为0->9。
	     * @param money double型的金额。
	     * @param i 0->9对应分->千万。
	     * @return
	     */
		public static String MoneySplit(Double money,Integer i)
		{
			
			DecimalFormat df = new DecimalFormat("0.00");
			String str = "";
			String str1 = String.valueOf(df.format(money));
			String str2 = "";
			
			for(int m=0;m<str1.length();m++)
	            str2 = str2+str1.charAt(str1.length()-1-m);			

			if(i<=1)
				str = str2.substring(i,i+1);
			if(i>1&&i<str2.length()-1)
				str = str2.substring(i+1,i+2);
			if(i==str2.length()-1)
				str = "￥";
			return str;
		}
	    /**
	     * 判断传进来的BigDecimal是否为空，为空则返回"0.00";
	     * 不为空，则返回本身。
	     * @param money
	     * @return 
	     */
		public static BigDecimal BDvalueof(BigDecimal temp)
		{
			if(temp==null)
				return new BigDecimal("0.00");
			return temp;
		}
	}  

