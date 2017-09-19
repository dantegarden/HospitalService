package com.dvt.HospitalService.commons.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.beust.jcommander.internal.Maps;
import com.dvt.HospitalService.business.example.webservice.impl.SaxServiceImpl;
import com.dvt.HospitalService.commons.utils.SeleniumUtils;


public class TestSelenium {
	@Test
	public void test2(){
		//String ewmJson = "{\"kjje\": \"667216\", \"fphm\": \"58990918\", \"kprq\": \"20170706\", \"fpdm\": \"4403162320\"}";
		//String ewmJson = "{\"fpdm\":\"2102162350\",\"fphm\":\"00543232\",\"kprq\":\"20170302\",\"kjje\":\"235107\"}";
		//查无此票
		//String ewmJson = "{\"fpdm\":\"3700162320\",\"fphm\":\"10898752\",\"kprq\":\"20170328\",\"kjje\":\"718693\"}";
		//不一致
		//String ewmJson = "{\"fpdm\":\"2102162350\",\"fphm\":\"00543266\",\"kprq\":\"20170321\",\"kjje\":\"369831\"}";
		//String ewmJson = "{\"fpdm\":\"2100161350\",\"fphm\":\"00982149\",\"kprq\":\"20170103\",\"kjje\":\"824184\"}";
		String ewmJson = "{\"fpdm\":\"3300164320\",\"fphm\":\"08336408\",\"kprq\":\"20170613\",\"kjje\":\"282274\"}";
		SaxServiceImpl saxServiceImpl = new SaxServiceImpl();
		String backjson = saxServiceImpl.checkFpEffect(ewmJson);
		System.out.println(backjson);
	}
	
	//@Test
	public void test(){
		String contextDir = System.getProperty("user.dir");
		System.setProperty("webdriver.firefox.bin","D://Program Files (x86)//Mozilla Firefox_47.0.1//firefox.exe");//"/usr/bin/firefox"
		System.setProperty("webdriver.firefox.marionette",
				contextDir + "\\driver\\geckodriver.exe");//改\\变/exe
		System.out.println(contextDir + "\\driver\\geckodriver.exe"); //改exe
		WebDriver driver = new FirefoxDriver();
		driver.manage().window().maximize();// 窗口最大化
		try {
			driver.get("http://app1.sfda.gov.cn/datasearch/face3/dir.html");
			WebElement table = driver.findElement(By.xpath("/html/body/center/table[5]"));
			WebElement ylqxlink = table.findElement(By.xpath("/html/body/center/table[5]/tbody/tr[6]/td/table/tbody/tr[2]/td/table/tbody/tr[3]/td[2]/a"));
			ylqxlink.click();
			
			fetchPage(driver,15);
			
			
			System.out.println("11");
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			driver.quit();
		}
	}
	
	public void fetchPage(WebDriver driver, Integer pageNum){
		System.out.println("开始抓取第"+pageNum+"页");
		if(pageNum!=1){
			blankRetry(driver, pageNum);
		}
		int rollCounter = 0;
		
		while(true){
			
			while(true){
				if(SeleniumUtils.doesWebElementExist(driver, By.xpath("//*[@id='content']"))){
					break;
				}else{
					blankRetry(driver, pageNum);
				}
			}
			rollCounter++;
			
			
			System.out.println("检索第"+pageNum+"页第"+rollCounter+"行记录");
			
			WebElement content = driver.findElement(By.xpath("//*[@id='content']"));
			
			WebElement content_table = null;
			try {
				Thread.currentThread().sleep(2000);
				content_table = content.findElement(By.xpath("//*[@id='content']/table[2]"));
			} catch (NoSuchElementException e) {
				content_table = content.findElement(By.xpath("//*[@id='content']/div/table[2]"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			List<WebElement> tr_alinks = content_table.findElements(By.cssSelector("a"));
			int len = tr_alinks.size();
			if(len>0){
				int trCounter = rollCounter + rollCounter -1;
				WebElement a = null;
				try {
					a = content_table.findElement(By.xpath("//*[@id='content']/table[2]/tbody/tr["+ trCounter +"]/td/p/a"));
				} catch (Exception e) {
					a = content_table.findElement(By.xpath("//*[@id='content']/div/table[2]/tbody/tr["+ trCounter +"]/td/p/a"));
				}
				
						
				a.click();
				
				boolean blank_flag = fetchMessage(driver);
				if(!blank_flag){//刷新表格失败
					blankRetry(driver, pageNum, trCounter);
				}
				
				if(len == rollCounter){//本页抓完
					WebElement nextPageButton = null;
					nextPageButton = null;
					try {
						nextPageButton = driver.findElement(By.xpath("//*[@id='content']/table[4]/tbody/tr/td[4]/img"));
					} catch (Exception e) {
						nextPageButton = driver.findElement(By.xpath("//*[@id='content']/div/table[4]/tbody/tr/td[4]/img"));
					}
					
					nextPageButton.click();
					fetchPage(driver, ++pageNum);
				}
			}
			
		}
	 }	
	
	public boolean fetchMessage(WebDriver driver){

		int blank_counter = 0;
		while(true){
			if(SeleniumUtils.doesWebElementExist(driver, By.xpath("//*[@id='content']/div"))){
				break;
			}else{
				try {
					if(blank_counter == 1){
						return Boolean.FALSE;
					}
					Thread.currentThread().sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			blank_counter++;
		}		
				
		WebElement mechineTable = driver.findElement(By.xpath("//*[@id='content']/div"));
		List<WebElement> tds =  mechineTable.findElements(By.cssSelector("table tbody td"));
		if(CollectionUtils.isNotEmpty(tds)){
			int count = 0;
			Map<String,String> params = Maps.newHashMap();
			for (WebElement td : tds) {
//				if(count>0 && count<11){//需要的数据在这里
//					/System.out.println(count + " : " + td.getText());
//				}
				
				if(count==2){
					params.put("code", td.getText());
				}else if(count==4){
					params.put("class_code", td.getText());
				}else if(count==6){
					params.put("manage_class", td.getText());
				}else if(count==8){
					params.put("type_name", td.getText());
				}else if(count==10){
					params.put("class_name", td.getText());
				}
				count++;
			}
			//int uid = (int)XmlRpcUtils.executeCommonXMLRPC(String.format("%s/xmlrpc/2/commom", "http://101.200.124.206:4713"), "authenticate", Arrays.asList("hospital-saas","admin","123456",Maps.newHashMap()));
			writeFile(params.get("code")+"|"+params.get("class_code")+"|"+params.get("manage_class")+"|"+params.get("type_name")+"|"+params.get("class_name"));
			//XmlRpcUtils.executeXMLRPC(String.format("%s/xmlrpc/2/object", "http://101.200.124.206:4713"), "execute_kw", Arrays.asList("hospital-saas","1","123456","device.class","create",Arrays.asList(params)));
		}
		
		WebElement gobackImg = mechineTable.findElement(By.xpath("//*[@id='content']/div/div/table[2]/tbody/tr/td"));
		gobackImg.click();
		return Boolean.TRUE;
	}
	
	public void blankRetry(WebDriver driver,Integer pageNum,Integer trCounter){
		while(true){
			try {
				driver.get(driver.getCurrentUrl());//重定向
				WebDriverWait tabwait = new WebDriverWait(driver, 3);
				WebElement tableDiv = tabwait.until(new ExpectedCondition<WebElement>() {
					@Override
					public WebElement apply(WebDriver arg0) {
						return arg0.findElement(By.id("goInt"));
					}
				});
				if(tabwait!=null){break;}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		WebElement goInt = driver.findElement(By.id("goInt"));
		goInt.clear();
		goInt.sendKeys(pageNum.toString());
		try {
			driver.findElement(By.xpath("//*[@id='content']/div/table[4]/tbody/tr/td[7]/input")).click();//跳到原页面
		} catch (NoSuchElementException e) {
			driver.findElement(By.xpath("//*[@id='content']/table[4]/tbody/tr/td[7]/input")).click();//跳到原页面
		}
		
		while(true){
			try {
				Thread.currentThread().sleep(2000);
				if(SeleniumUtils.doesWebElementExist(driver, By.xpath("//*[@id='content']/table[2]"))||SeleniumUtils.doesWebElementExist(driver, By.xpath("//*[@id='content']/div/table[2]"))){
					break;
				}else{
					blankRetry(driver, pageNum);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		try {
			driver.findElement(By.xpath("//*[@id='content']/div/table[2]/tbody/tr["+ trCounter +"]/td/p/a")).click();
		} catch (NoSuchElementException e) {
			driver.findElement(By.xpath("//*[@id='content']/table[2]/tbody/tr["+ trCounter +"]/td/p/a")).click();
		}
		
		if(!fetchMessage(driver)){
			blankRetry(driver, pageNum, trCounter);
		}
	}
	
	
	public void blankRetry(WebDriver driver,Integer pageNum){
		while(true){
			try {
				driver.get(driver.getCurrentUrl());//重定向
				WebDriverWait tabwait = new WebDriverWait(driver, 3);
				WebElement tableDiv = tabwait.until(new ExpectedCondition<WebElement>() {
					@Override
					public WebElement apply(WebDriver arg0) {
						return arg0.findElement(By.id("goInt"));
					}
				});
				if(tabwait!=null){break;}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		WebElement goInt = driver.findElement(By.id("goInt"));
		goInt.clear();
		goInt.sendKeys(pageNum.toString());
		try {
			driver.findElement(By.xpath("//*[@id='content']/div/table[4]/tbody/tr/td[7]/input")).click();//跳到原页面
		} catch (NoSuchElementException e) {
			driver.findElement(By.xpath("//*[@id='content']/table[4]/tbody/tr/td[7]/input")).click();//跳到原页面
		}
		
	}
	
	public void writeFile(String text){
		FileWriter fw = null;
		try {
			//如果文件存在，则追加内容；如果文件不存在，则创建文件
			File f=new File("E:\\医疗器械类别.txt");
			fw = new FileWriter(f, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter pw = new PrintWriter(fw);
		pw.println(text);
		pw.flush();
		try {
			fw.flush();
			pw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
