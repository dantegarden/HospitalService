package com.dvt.HospitalService.commons.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;


public class TestMethod2 {
	@Test
	public void test(){
//		String str = "请假类型是病假";
//		Pattern pattern = Pattern.compile("(?<year>(?:病|年|事|婚|丧)假)");    
		
		
		String str = "周永康";//
		String p = "[赵|钱|孙|李|周|吴|郑|王|冯|陈|褚|卫|蒋|沈|韩|杨|朱|秦|尤|许|何|吕|施|张|孔|曹|严|华|金|魏|陶|姜|戚|谢|邹|喻|柏|水|窦|章|云|苏|潘|葛|奚|范|彭|郎|鲁|韦|昌|马|苗|凤|花|方|俞|任|袁|柳|酆|鲍|史|唐|费|廉|岑|薛|雷|贺|倪|汤|滕|殷|罗|毕|郝|邬|安|常|乐|于|时|傅|皮|卞|齐|康|伍|余|元|卜|顾|孟|平|黄|和|穆|萧|尹|姚|邵|湛|汪|祁|毛|禹|狄|米|贝|明|臧|计|伏|成|戴|谈|宋|茅|庞|熊|纪|舒|屈|项|祝|董|梁|杜|阮|蓝|闵|席|季|麻|强|贾|路|娄|危|江|童|颜|郭|梅|盛|林|刁|钟|徐|邱|骆|高|夏|蔡|田|樊|胡|凌|霍|虞|万|支|柯|咎|管|卢|莫|经|房|裘|缪|干|解|应|宗|宣|丁|贲|邓|郁|单|杭|洪|包|诸|左|石|崔|吉|钮|龚|程|嵇|邢|滑|裴|陆|荣|翁|荀|羊|於|惠|甄|魏|加|封|芮|羿|储|靳|汲|邴|糜|松|井|段|富|巫|乌|焦|巴|弓|牧|隗|山|谷|车|侯|宓|蓬|全|郗|班|仰|秋|仲|伊|宫|宁|仇|栾|暴|甘|钭|厉|戎|祖|武|符|刘|姜|詹|束|龙|叶|幸|司|韶|郜|黎|蓟|薄|印|宿|白|怀|蒲|台|从|鄂|索|咸|籍|赖|卓|蔺|屠|蒙|池|乔|阴|郁|胥|能|苍|双|闻|莘|党|翟|谭|贡|劳|逄|姬|申|扶|堵|冉|宰|郦|雍|却|璩|桑|桂||濮|牛|寿|通|边|扈|燕|冀|郏|浦|尚|农|温|别|庄|晏|柴|瞿|阎|充|慕|连|茹|习|宦|艾|鱼|容|向|古|易|慎|戈|廖|庚|终|暨|居|衡|步|都|耿|满|弘|匡|国|文|寇|广|禄|阙|东|殴|殳|沃|利|蔚|越|夔|隆|师|巩|厍|聂|晁|勾|敖|融|冷|訾|辛|阚|那|简|饶|空|曾|毋|沙|乜|养|鞠|须|丰|巢|关|蒯|相|查|后|红|游|竺|权|逯|盖|益|桓|公|万|俟|司马|上官|欧阳|夏侯|诸葛|闻|东方|皇甫|尉迟|公羊|澹台|公冶|宗政|濮|淳于|仲孙|太叔|申屠|公孙|乐正|轩辕|令狐|钟离|闾丘|长孙|慕容|鲜于|宇文|司徒|司空|亓官|司寇|仉督|子车|颛孙|端木|巫马|公西|漆雕|乐正|壤驷|公良|拓拔|夹谷|宰父|谷粱|晋楚|阎法|汝鄢|涂钦|段|百里|东郭|南门|呼延|归海|羊舌|微生|岳|帅|缑亢|况|后|有|琴|梁|丘|左|东门|西门|商|牟|佘|佴|伯|赏|南宫|墨|哈|谯|笪|年|爱|阳|佟|第|五|言|福][\u4e00-\u9fff]{1,2}";
		Pattern pattern = Pattern.compile(p);
		Matcher mr = pattern.matcher(str);
		System.out.println(mr.matches());
//		if(mr.matches()){
//	    }
		
//		Pattern pattern = Pattern.compile("(?:(?:结束时间是?(?<v1>.*))|(?:(?<v2>.*)结束)|(?:从(?:.*)到(?<v3>.*)))");
//	    Matcher mr = pattern.matcher(str);
//	    if(mr.find()){
//	    	for (int i = 1; i <= mr.groupCount(); i++) {
//	    		String patternStr = mr.group("v"+i);
//	    		System.out.println("捕获组"+i+":"+ patternStr);
//	    		
//	    		if(StringUtils.isNotBlank(patternStr)){
//	    			int startIndex = mr.start(i);
//	    			int lastIndex = mr.end(i) -1 ;
//	    			System.out.println("startIndex:"+startIndex+",lastIndex:"+lastIndex);
//	    			String m = "9点";
//	    			int _startIndex = str.indexOf(m);
//	    			int _lastIndex = _startIndex + m.length() -1;
//	    			System.out.println("_startIndex:"+_startIndex+",_lastIndex:"+_lastIndex);
//	    		}
//	    	}
//	    }
		
		//		String str = "因为家里有事";
//		Pattern pattern = Pattern.compile("(因为|由于)(?<reason>.*)");
		
//		Pattern pattern = Pattern.compile("(\\d+,)(\\d+)");  
		//String str = "123,456-34,345";  
	    Matcher matcher = pattern.matcher(str);
	    //System.out.println(matcher.matches());
	    while(matcher.find()){
	    	System.out.println(matcher.groupCount());
	    	System.out.println(matcher.group());
	    	//System.out.println(matcher.group("v3"));
	    	System.out.println(matcher.group("v2"));
	    	//System.out.println(matcher.group("v4"));
	    }
		System.out.println(matcher.matches());
	}
	
	//@Test
	public void readFileByLines() {  
        File file = new File("D:/QMES/761043617/FileRecv/m.txt");  
        File outFile = new File("D:/QMES/761043617/FileRecv/_m.txt");
        BufferedReader reader = null; 
        BufferedWriter writer = null;
        try {  
        	if (!outFile.exists()) {  
        		outFile.createNewFile();  
        	}else{
        		outFile.delete();
        		outFile.createNewFile();  
        	} 
            System.out.println("以行为单位读取文件内容，一次读一整行：");  
            reader = new BufferedReader(new FileReader(file));  
            writer = new BufferedWriter(new FileWriter(outFile));
            
            
            String tempString = null;  
            int line = 1;  
            // 一次读入一行，直到读入null为文件结束  
            while ((tempString = reader.readLine()) != null) {  
                // 显示行号  
                System.out.println("line " + line + ": " + tempString);  
                String decodeStr = unicodeToString(tempString);
                System.out.println(decodeStr);
                writer.write(decodeStr);
                line++;  
            }  
            reader.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (reader != null) {  
                try {  
                    reader.close();  
                } catch (IOException e1) {  
                }  
            }  
        }  
    }  
   

	public  String unicodeToString(String str) {
 
	    Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");    
	    Matcher matcher = pattern.matcher(str);
	    char ch;
	    while (matcher.find()) {
	        ch = (char) Integer.parseInt(matcher.group(2), 16);
	        str = str.replace(matcher.group(1), ch + "");    
	    }
	    return str;
	} 
}
