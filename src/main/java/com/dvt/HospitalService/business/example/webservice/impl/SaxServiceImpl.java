package com.dvt.HospitalService.business.example.webservice.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.dvt.HospitalService.business.example.WebDriverPool;
import com.dvt.HospitalService.business.example.api.ChaoJiYing;
import com.dvt.HospitalService.business.example.dto.Fpxx;
import com.dvt.HospitalService.business.example.dto.SaxMsg;
import com.dvt.HospitalService.business.example.dto.SaxResult;
import com.dvt.HospitalService.business.example.dto.YzmResult;
import com.dvt.HospitalService.business.example.webservice.SaxService;
import com.dvt.HospitalService.commons.utils.Base64Utils;
import com.dvt.HospitalService.commons.utils.CommonHelper;
import com.dvt.HospitalService.commons.utils.ImageHandleHelper;
import com.dvt.HospitalService.commons.utils.JsonUtils;
import com.dvt.HospitalService.commons.utils.SeleniumUtils;
import com.dvt.HospitalService.commons.utils.TestImage;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;


public class SaxServiceImpl implements SaxService{
	
	public static final Map version = ImmutableMap.of("selenium.version", "3.3.1", //3.4.0的实现方式无法截全屏
													  "firefox.version", "47.0.1", //54
													  "geckodriver.version","0.18.exe");
	
	
//	public static final String contextDir = "E://Program Files//apache-tomcat-8.0.41//webapps//HospitalService";
//	public static final String filepath = contextDir+ "//yzm_pic//yzm"; // 验证码图片
//	public static final String filepath2 = contextDir + "//yzm_pic//yzm_ms";// 验证码要求
//	public static final String fileoutpath = contextDir + "//yzm_pic//union"; // 拼好的验证码+要求
//	public static final String fileyzmmsA = contextDir+"//yzm_pic//yzmmsA.jpeg";
//	public static final String fileyzmmsB = contextDir+"//yzm_pic//yzmmsB.jpeg";
//	public static final String firefoxPath = "D:\\Program Files (x86)\\Mozilla Firefox_"+version.get("firefox.version")+"\\firefox.exe"; 
//	public static final String profilePath = "C://Users//ＣＩＬＡＮＧ//AppData//Roaming//Mozilla//Firefox//Profiles//ssl-p";
//	public static final String phantomjspath = "E://chrome//phantomjs-2.1.1-windows//bin//phantomjs.exe";
//	
//	//使用超级鹰的账号密码信息
//	public static final String username = "dantegarden";
//	public static final String password = "10121118Dante";
//	public static final String machineCode = "1234";// 数字，随便写
//	public static final String codeType = "6004";// 写死6004
//	private static WebDriverPool webDriverPool = new WebDriverPool(10);

	
	public static final String contextDir;
	public static final String filepath; // 验证码图片
	public static final String filepath2;// 验证码要求
	public static final String fileoutpath; // 拼好的验证码+要求
	public static final String fileyzmmsA;
	public static final String fileyzmmsB;
	public static final String profilePath;//火狐profile文件夹位置
	public static final String firefoxPath;
	public static final String phantomjspath;
	private static WebDriverPool webDriverPool = null;//驱动池
	
	//使用超级鹰的账号密码信息
	public static final String username;
	public static final String password;
	public static final String machineCode;// 数字，随便写
	public static final String codeType;// 写死6004
	
	static{
		Properties prop = new Properties();
		try {
			prop.load(SaxServiceImpl.class.getClassLoader().getResourceAsStream("deploy.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		username = prop.getProperty("chaojiying.username");
		password = prop.getProperty("chaojiying.password");
		machineCode = prop.getProperty("chaojiying.machineCode");
		codeType = prop.getProperty("chaojiying.codeType");
		contextDir=prop.getProperty("contextDir");
		filepath = contextDir + prop.getProperty("yzm.path");
		filepath2 = contextDir + prop.getProperty("yzm.yqpath");
		fileoutpath = contextDir + prop.getProperty("yzm.unionpath");
		fileyzmmsA = contextDir + prop.getProperty("yzm.msA");
		fileyzmmsB = contextDir+ prop.getProperty("yzm.msB");
		profilePath = prop.getProperty("profileDir");
		firefoxPath = "/usr/bin/firefox";
	    phantomjspath = prop.getProperty("phantomjs.path");
	    webDriverPool = new WebDriverPool(Integer.valueOf(prop.getProperty("queue.capacity")));
	}
	
	
	@Override
	public String sayHello(String name) {
		ImageHandleHelper.overlapImage(filepath2, filepath, fileoutpath);
		System.out.println(contextDir + " from client..."+name);
		return "Hello " + name + contextDir;
	}
	
	@Override
	public String checkUserScore(){
		return ChaoJiYing.GetScore(username, password);
	}
	
	@Override
	public String checkFpEffect(String ewmJson) {
		long start=System.currentTimeMillis();  
		
		SaxResult sr = null;
		if(StringUtils.isNotBlank(ewmJson)){
			System.out.println("LOG-checkFpEffect:" + ewmJson);
			
			Fpxx fpxx = JsonUtils.jsonToJavaBean(ewmJson, Fpxx.class);
			//通过发票代码确定是哪种票
			final String tableid = "tabPage2";
			WebDriver driver = null;
			
			if("3.4.0".equals(version.get("selenium.version"))){
				//火狐54
				FirefoxOptions options = new FirefoxOptions();
			    options.setBinary(new FirefoxBinary(new File(firefoxPath)));
			    DesiredCapabilities capabilities = DesiredCapabilities.firefox();
				capabilities.setCapability("moz:firefoxOptions", options);
				System.setProperty("webdriver.gecko.driver",contextDir + "/driver/geckodriver-"+version.get("geckodriver.version"));
				
				try {
					driver = new FirefoxDriver(capabilities);
					//driver.manage().window().maximize();// 窗口最大化
					sr = this.fillForm(driver,tableid,fpxx,0);
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					if(driver!=null)
						driver.quit();
				}
			
			}else if("3.3.1".equals(version.get("selenium.version"))){
                
//                try {
//                	//创建无界面浏览器对象
//                	driver = webDriverPool.get();
//                    sr = this.fillFormByPhantomJS(driver,tableid,fpxx,0);
//				} catch (Exception e) {
//					e.printStackTrace();
//				} finally{
//                    //关闭并退出浏览器
//					if(driver!=null){
//						webDriverPool.close(driver);
//					}
//                }
				
				//火狐47.0.1
				System.setProperty("webdriver.firefox.bin", firefoxPath);
				//驱动器位置
				System.setProperty("webdriver.firefox.marionette",
						contextDir + "/driver/geckodriver-"+version.get("geckodriver.version")); 
				File profileFolder = new File(profilePath);//注释去掉 
				FirefoxProfile profile = new FirefoxProfile(profileFolder);//注释去掉 
				
				try {
					driver = new FirefoxDriver(profile);
					//driver.manage().window().maximize();// 窗口最大化
					sr = this.fillForm(driver,tableid,fpxx,0);
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					if(driver!=null)
						driver.quit();
				}
			}else{
				System.out.println("selenium版本加载失败");
			}
			
			//加载插件
//			File screenshotXPI = new File(contextDir+"/driver/easy_screenshot-1.2.7.2-fx.xpi");
//			FirefoxProfile profile = new FirefoxProfile();
//			profile.addExtension(screenshotXPI);
//			profile.setPreference("extensions.easyscreenshot.hotkeys.enabled", true);
//			profile.setPreference("extensions.easyscreenshot.openDirectory", false);
//			profile.setPreference("extensions.easyscreenshot.savePosition", contextDir+"/fp_pic");
//			WebDriver driver = new FirefoxDriver(profile);
			
		}else{
			sr = new SaxResult();
			sr.setStatus("0");
			sr.setDes("没有接收到来自客户端的发票信息");
		}
		System.out.println("耗时 "+ (System.currentTimeMillis()-start)+" 毫秒"); 
		return JsonUtils.JavaBeanToJson(sr);
	}
	

	private  SaxResult fillForm(WebDriver driver, String tableid, Fpxx fpxx,int tryCount) throws Exception{
		String timestamp = new Date().getTime() + "";
		String yzm_filepath = filepath + "_" + timestamp + ".png";
		String yzm_filepath2 = filepath2 + "_" + timestamp + ".jpeg";
		String yzm_fileoutpath = fileoutpath + "_" + timestamp + ".jpeg";
		
		tryCount++;//尝试次数计数器 
		System.out.println("LOG-checkFpEffect:" + "开始第"+tryCount+"次获取发票信息");
		
		SaxResult sr = new SaxResult();
		SaxMsg sm = new SaxMsg();
		
		System.out.println("LOG-checkFpEffect:" + "清理验证码图片");
		//清理验证码图片
		String[] paths = {yzm_filepath,yzm_filepath2,yzm_fileoutpath};
		for (String p : paths) {
			File curfile = new File(p);
			if(curfile.exists()){
				curfile.delete();
			}
		}		
		
		System.out.println("LOG-checkFpEffect:" + "打开网页，赋值表单");
		
		driver.get("https://inv-veri.chinatax.gov.cn");
		WebElement fpdm = driver.findElement(By.id("fpdm"));
		fpdm.sendKeys(fpxx.getFpdm());
		WebElement fphm = driver.findElement(By.id("fphm"));
		fphm.sendKeys(fpxx.getFphm());
		WebElement kprq = driver.findElement(By.id("kprq"));
		kprq.sendKeys(fpxx.getKprq());
		WebElement kjje = driver.findElement(By.id("kjje"));
		kjje.sendKeys(fpxx.getKjje());
		
		System.out.println("LOG-checkFpEffect:" + "等待验证码刷新");
		// 等待验证码刷出来，最多20秒
		WebDriverWait wait = new WebDriverWait(driver, 20);
		//FluentWait wait = new FluentWait<>(driver).withTimeout(3, TimeUnit.SECONDS).pollingEvery(5, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		boolean yzmready = wait.until(new Function<WebDriver,Boolean>(){
			@Override
			public Boolean apply(WebDriver arg0) {
				String pic_src = arg0.findElement(By.id("yzm_img")).getAttribute("src");
				return pic_src.startsWith("data:image/png;base64,");
			}
		});
		
		if (yzmready) {
			
			System.out.println("LOG-checkFpEffect:" + "验证码刷新成功");
			
			WebElement yzm = driver.findElement(By.id("yzm"));// 验证码输入框
			WebElement yzm_img = driver.findElement(By.id("yzm_img"));// 验证码base64字符串
			WebElement yzminfo = driver.findElement(By.id("yzminfo"));// 验证码提示
			String yzminfo_context = yzminfo.getText();
			WebElement yzminfoFont = null;
			if(SeleniumUtils.doesWebElementExist(driver, By.cssSelector("td#yzminfo font"))){
				yzminfoFont = driver.findElement(By.cssSelector("td#yzminfo font"));
			}
			
			int i = 0;
			while(true){
				try {
					i++;
					Thread.currentThread().sleep(1000);
					if(i==2){
						System.out.println("LOG-checkFpEffect:" + "全屏截图");
						SeleniumUtils.snapshot(driver, yzm_img, yzm_filepath);
						if(yzminfoFont!=null){
							SeleniumUtils.snapshot(driver, yzminfoFont, yzm_filepath2);
//							TestImage.exportImg1_snapshot("请输入",fileyzmmsA,45);
//							TestImage.exportImg1_snapshot("文字",fileyzmmsB,30);
							String[] tPaths = {fileyzmmsA,yzm_filepath2,fileyzmmsB};
							ImageHandleHelper.mergeImage(tPaths, 1, yzm_filepath2);
						}else{
							SeleniumUtils.snapshot(driver, yzminfo, yzm_filepath2);
						}
					}else if(i>10){break;}//超时跳出
					if(new File(yzm_filepath).exists()){break;}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			

			
			// String yzm_base64 =
			// yzm_img.getAttribute("src").replaceAll("data:image/png;base64,",
			// "");
			// Base64Utils.GenerateImage(yzm_base64,filepath);//写到本地
			System.out.println("LOG-checkFpEffect:" + "获取验证码要求");
			TestImage.createWhitePic(yzm_fileoutpath);
			ImageHandleHelper.overlapImageA(yzm_fileoutpath, yzm_filepath2, yzm_fileoutpath, "1");
			//TestImage.exportImg1_snapshot(yzminfo_context, yzm_filepath2);// 要求也写到本地
			System.out.println("LOG-checkFpEffect:" + "拼接图片");
			ImageHandleHelper.overlapImageA(yzm_fileoutpath, yzm_filepath, yzm_fileoutpath,"2");// 拼接图片
			System.out.println("LOG-checkFpEffect:" + "拼接结束");
			
			while(true){
				try {
					if(new File(yzm_fileoutpath).exists()){break;}
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("LOG-checkFpEffect:" + "调用超级鹰接口解码");
			//String yzm_res = "{\"err_no\":0,\"err_str\":\"OK\",\"pic_id\":\"262128065301\",\"pic_str\":\"\u8fd9\",\"md5\":\"657907e379e81c494cb11ee1221150c3\",\"str_debug\":\"trans-message\"}";
			String yzm_res = ChaoJiYing.PostPic(username, password,machineCode, codeType, "0", "0", "trans-message",yzm_fileoutpath);
			System.out.println("LOG-checkFpEffect:" + yzm_res);
			
			YzmResult yzmres = JsonUtils.jsonToJavaBean(yzm_res,YzmResult.class);
			// json字符串转对象
			System.out.println(yzmres.getPic_str());
			yzm.sendKeys(yzmres.getPic_str());
			String picid = yzmres.getPic_id();
			System.out.println(picid);

			WebElement checkfp = driver.findElement(By.id("checkfp"));// 查验按钮

			if (checkfp.getAttribute("style").equals("display: inline-block;")) {
				checkfp.click();
				try {
					WebDriverWait tabwait = new WebDriverWait(driver, 100);
					WebElement tableDiv = tabwait.until(new Function<WebDriver,WebElement>(){
						@Override
						public WebElement apply(WebDriver arg0) {
							return arg0.findElement(By.id(tableid));
						}
					});
					List<WebElement> hrList = tableDiv.findElements(By.cssSelector("table > tbody > tr > td > span")); // By.xpath("//table/tbody/tr/td/span")
					if (CollectionUtils.isNotEmpty(hrList)) {
						System.out.println("LOG-checkFpEffect:" + "获取发票表单成功");
						if(hrList.size()<=4){
							System.out.println("LOG-checkFpEffect:" + "表单只有四项信息-查无此发票");
							WebElement fpWindow = driver.findElement(By.id("con_tent"));// 发票窗口
							String cyjg = driver.findElement(By.id("cyjg")).getText();
							String fpname = CommonHelper.getNowStr("yyyyMMddHHmmss")+"_"+fpxx.getFpdm()+"_"+fpxx.getFphm()+".png";
							String fppath = contextDir +"/fp_pic/"+fpname;
							System.out.println("LOG-checkFpEffect:" + "结果窗口截屏");
							SeleniumUtils.snapshot(driver, fpWindow, fppath);
							while(true){
								try {
									Thread.sleep(1000);
									if(new File(fppath).exists()){
										System.out.println("LOG-checkFpEffect:" + "发票截图转码");
										sm.setFp_base64(Base64Utils.GetImageStr(fppath));
										File curfile = new File(fppath);
										curfile.delete();
										break;}
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							sr.setStatus("2");
							sr.setSaxmsg(sm);
							sr.setDes(cyjg);
						}else{
							
							
							//可能还会有款项详情的表单，先看显示详情的按钮是否存在
							if(SeleniumUtils.doesWebElementExist(tableDiv, By.id("showmx"))){
								//带有详情按钮的表格
								System.out.println("LOG-checkFpEffect:" + "带有详情按钮的表格");
								int counter = 0;
								//先把普通表格上的信息拿走
								for (WebElement we : hrList) {
									System.out.println(counter+":" + we.getText());
									switch (counter) {
									case 0:{sm.setFpdm(we.getText());break;}
									case 1:{sm.setFphm(we.getText());break;}
									case 2:{sm.setKpsj(we.getText());break;}//开票时间
									case 3:{sm.setJym(we.getText());break;}//校验码
									case 4:{sm.setJqhm(we.getText());break;}//机器号
									case 5:{sm.setGmf_mc(we.getText());break;}//购买方 名称
									case 6:{sm.setGmf_nsrsbh(we.getText());break;}//购买方 纳税人识别号
									case 7:{sm.setGmf_dzdh(we.getText());break;} //地址电话
									case 8:{sm.setGmf_khhjzh(we.getText());break;} //开户行及账号
									case 25:{sm.setJe_dw(we.getText());break;}//金额单位
									case 26:{sm.setSe_dw(we.getText());break;}//税额单位
									case 27:{sm.setJshjdx(we.getText());break;}//大写
									case 29:{sm.setJshjxx(we.getText());break;}//小写
									case 30:{sm.setXsf_mc(we.getText());break;}//销售方 名称
									case 31:{sm.setXsf_nsrsbh(we.getText());break;}//销售方 纳税人识别号
									case 32:{sm.setXsf_dzdh(we.getText());break;}//销售方 地址电话
									case 33:{sm.setXsf_khhjzh(we.getText());break;}//销售方 开户行及账号
									default:
										break;
									}
									counter++;
								}
								
								System.out.println("LOG-checkFpEffect:" + "点击显示详情按钮");
								
								tableDiv.findElement(By.id("showmx")).click();//点击 显示详情
								WebDriverWait mxwait = new WebDriverWait(driver, 3);
								WebElement mxDiv = mxwait.until(new Function<WebDriver,WebElement>(){
									@Override
									public WebElement apply(WebDriver arg0) {
										return arg0.findElement(By.id("hwmxqd"));
									}
								});
								List<WebElement> mxList = mxDiv.findElements(By.cssSelector("table > tbody > tr > td "));
								if (CollectionUtils.isNotEmpty(mxList)) {
									System.out.println("LOG-checkFpEffect:" + "获得详情表格");
									
									int mxcount = 0;
									int startIndex = 15;
									for (WebElement mxwe : mxList) {
										System.out.println(mxcount+":" + mxwe.getText());
										if(mxcount>=15){
											if("小计".equals(mxwe.getText())){
												System.out.println("LOG-checkFpEffect:" + "获取完毕，截屏");
												String fpname = CommonHelper.getNowStr("yyyyMMddHHmmss")+"_"+fpxx.getFpdm()+"_"+fpxx.getFphm()+".png";
												String fppath = contextDir +"/fp_pic/"+fpname;
												SeleniumUtils.snapshot(driver, mxDiv, fppath);
												while(true){
													try {
														Thread.sleep(1000);
														if(new File(fppath).exists()){
															System.out.println("LOG-checkFpEffect:" + "详情截图转码");
															sm.setFpxq_pic(fpname);
															sm.setFpxq_base64(Base64Utils.GetImageStr(fppath));
															File curfile = new File(fppath);
															curfile.delete();
															break;}
													} catch (InterruptedException e) {
														e.printStackTrace();
													}
												}
												System.out.println("LOG-checkFpEffect:" + "关闭详情窗口");
												mxDiv.findElement(By.id("mxclose")).click();//关闭窗口
												break;
											}
											if(startIndex+1==mxcount){sm.setYsmc(strC(sm.getYsmc())+mxwe.getText());}
											else if(startIndex+2==mxcount){sm.setGgxh(strC(sm.getGgxh())+mxwe.getText());}
											else if(startIndex+3==mxcount){sm.setDw(strC(sm.getDw())+mxwe.getText());}
											else if(startIndex+4==mxcount){sm.setSl(strC(sm.getSl())+mxwe.getText());}
											else if(startIndex+5==mxcount){sm.setDj(strC(sm.getDj())+mxwe.getText());}
											else if(startIndex+6==mxcount){sm.setJe(strC(sm.getJe())+mxwe.getText());}
											else if(startIndex+7==mxcount){sm.setSlv(strC(sm.getSlv())+mxwe.getText());}
											else if(startIndex+8==mxcount){sm.setSe(strC(sm.getSe())+mxwe.getText());
																		   startIndex=mxcount+1;}
										}
										mxcount++;
									}
								}
								
								
							}else{
								//正常表格
								System.out.println("LOG-checkFpEffect:" + "正常表格");
								
								boolean flag = Boolean.FALSE;
								int counter = 0;
								
//								if(SeleniumUtils.doesWebElementExist(driver, By.id("tabPage-jsfp"))){
//									System.out.println("LOG-checkFpEffect:" + "卷票");
//									
//									int startIndex = 10;
//									
//									for (WebElement we : hrList) {
//										System.out.println(counter+":" + we.getText());
//										if(counter<10 && counter>-1){
//											switch (counter) {
//												case 0:{sm.setFpdm(we.getText());break;}
//												case 1:{sm.setFphm(we.getText());break;}
//												case 2:{sm.setJdhm(we.getText());break;}
//												case 3:{sm.setJqhm(we.getText());break;}
//												case 4:{sm.setXsf_mc(we.getText());break;}
//												case 5:{sm.setXsf_nsrsbh(we.getText());break;}
//												case 6:{sm.setKpsj(we.getText());break;}
//												case 7:{sm.setShy(we.getText());break;}
//												case 8:{sm.setGmf_mc(we.getText());break;}
//												case 9:{sm.setGmf_nsrsbh(we.getText());break;}
//												default:
//													break;
//											}
//										}else if(counter>=10){
//											if(!flag && (counter==10 || (counter-10)%4==0)){
//												if(we.getText().contains("￥")){
//													sm.setJe_xx(we.getText());
//													flag = Boolean.TRUE;
//												}else{
//													sm.setYsmc(strC(sm.getYsmc())+we.getText());
//												}
//												startIndex = counter;
//											}else{
//												if(flag){//正常
//													if(startIndex+1==counter){sm.setJe_dx(we.getText());}
//													else if(startIndex+2==counter){sm.setJym(we.getText());}
//												}else{
//													if(startIndex+1==counter){sm.setDj(strC(sm.getDj())+we.getText());}
//													else if(startIndex+2==counter){sm.setSl(strC(sm.getSl())+we.getText());}
//													else if(startIndex+3==counter){sm.setJe(strC(sm.getJe())+we.getText());}
//												}
//											}
//										}
//										
//										counter++;
//									}
//									
//								}else{
									System.out.println("LOG-checkFpEffect:" + "普通发票");
									
									int startIndex = 17;
									
									for (WebElement we : hrList) {
										System.out.println(counter+":" + we.getText());
										if(counter<17 && counter>-1){
											switch (counter) {
											case 0:{sm.setFpdm(we.getText());break;}
											case 1:{sm.setFphm(we.getText());break;}
											case 2:{sm.setKpsj(we.getText());break;}
											case 3:{sm.setJym(we.getText());break;}
											case 4:{sm.setJqhm(we.getText());break;}
											case 5:{sm.setGmf_mc(we.getText());break;}
											case 6:{sm.setGmf_nsrsbh(we.getText());break;}
											case 7:{sm.setGmf_dzdh(we.getText());break;}
											case 8:{sm.setGmf_khhjzh(we.getText());break;}
											case 9:{sm.setYsmc(we.getText());break;}	
											case 10:{sm.setGgxh(we.getText());break;}
											case 11:{sm.setDw(we.getText());break;}
											case 12:{sm.setSl(we.getText());break;}
											case 13:{sm.setDj(we.getText());break;}
											case 14:{sm.setJe(we.getText());break;}
											case 15:{sm.setSlv(we.getText());break;}
											case 16:{sm.setSe(we.getText());break;}
											default:
												break;
											}
										}else if(counter>=17){
											
											if(!flag && (counter==17 || (counter-17)%8==0)){
												if(we.getText().contains("￥")){
													sm.setJe_dw(we.getText());
													flag = Boolean.TRUE;
												}else{
													sm.setYsmc(strC(sm.getYsmc())+we.getText());
												}
												startIndex = counter;
											}else{
												if(flag){//正常
													if(startIndex+1==counter){sm.setSe_dw(we.getText());}
													else if(startIndex+2==counter){sm.setJshjdx(we.getText());}
													else if(startIndex+4==counter){sm.setJshjxx(we.getText());}
													else if(startIndex+5==counter){sm.setXsf_mc(we.getText());}
													else if(startIndex+6==counter){sm.setXsf_nsrsbh(we.getText());}
													else if(startIndex+7==counter){sm.setXsf_dzdh(we.getText());}
													else if(startIndex+8==counter){sm.setXsf_khhjzh(we.getText());}
												}else{
													if(startIndex+1==counter){sm.setGgxh(strC(sm.getGgxh())+we.getText());}
													else if(startIndex+2==counter){sm.setDw(strC(sm.getDw())+we.getText());}
													else if(startIndex+3==counter){sm.setSl(strC(sm.getSl())+we.getText());}
													else if(startIndex+4==counter){sm.setDj(strC(sm.getDj())+we.getText());}
													else if(startIndex+5==counter){sm.setJe(strC(sm.getJe())+we.getText());}
													else if(startIndex+6==counter){sm.setSlv(strC(sm.getSlv())+we.getText());}
													else if(startIndex+7==counter){sm.setSe(strC(sm.getSe())+we.getText());}
												}
											}
											
										}
										counter++;
									}
								//}
								
								
								
							}
							//检查发票有效性
							System.out.println("LOG-checkFpEffect:" + "检查发票有效性");
							if(SeleniumUtils.doesWebElementExist(driver, By.id("icon_zf"))){
								WebElement zfImgDiv = driver.findElement(By.id("icon_zf"));
								if(zfImgDiv.isDisplayed()){
									sm.setIsValid(Boolean.FALSE);
								}
							}
							
							//两种共有的代码
							System.out.println("LOG-checkFpEffect:" + "获取发票窗口");
							WebElement fpWindow = driver.findElement(By.id("bg_div"));// 发票窗口
							String fpname = CommonHelper.getNowStr("yyyyMMddHHmmss")+"_"+fpxx.getFpdm()+"_"+fpxx.getFphm()+".png";
							String fppath = contextDir +"/fp_pic/"+fpname;
							System.out.println("LOG-checkFpEffect:" + "发票窗口截屏");
							SeleniumUtils.snapshot(driver, fpWindow, fppath);
							
							
							while(true){
								try {
									Thread.sleep(1000);
									if(new File(fppath).exists()){
										System.out.println("LOG-checkFpEffect:" + "发票截图转码");
										sm.setFp_base64(Base64Utils.GetImageStr(fppath));
										File curfile = new File(fppath);
										curfile.delete();
										break;}
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							
							
							sm.setFp_pic(fpname);
							sr.setStatus("1");
							sr.setSaxmsg(sm);
							System.out.println("LOG-checkFpEffect:" + "成功！");
						}
						
					}
				} catch (Exception e) {
					System.out.println("LOG-checkFpEffect:" + "获取发票表单失败");

					//WebElement tableDiv = driver.findElement(By.id("popup_container"));// table
					String alert_msg = "";
					if(SeleniumUtils.doesWebElementExist(driver, By.id("popup_message"))){
						alert_msg = driver.findElement(By.id("popup_message")).getText();
					}else{
						if(e.getMessage().indexOf("TimeOutException")>-1){
							alert_msg = "验证码没刷出来";
						}else{
							alert_msg = "未知错误，记下报文"+fpxx.getFpdm()+","+fpxx.getFphm();
						}
					}
					
					System.out.println("LOG-checkFpEffect:" + "失败原因：" + alert_msg);
					
					if (alert_msg.indexOf("验证码错误") > -1) {
						// 验证失败 发送错误报告
						System.out.println("LOG-checkFpEffect:" + "验证码解码错误，发送错误报告");
						ChaoJiYing.ReportError(username, password,machineCode, picid);
						if(tryCount<10){
							System.out.println("LOG-checkFpEffect:" + "重试");
							System.out.println("LOG-checkFpEffect:" + "======================");
							return this.fillForm(driver, tableid, fpxx, tryCount);
						}else{
							System.out.println("LOG-checkFpEffect:" + "尝试次数超过十次，失败");
							sr.setStatus("2");
							sr.setDes("尝试次数超过十次，失败");
						}
					} else {
						sr.setStatus("2");
						sr.setDes(alert_msg);
						System.out.println(alert_msg);
					}
				}finally{
					//清理验证码图片
					for (String p : paths) {
						File curfile = new File(p);
						if(curfile.exists()){
							curfile.delete();
						}
					}
				}
				
			}else{
				if(tryCount<10){
					System.out.println("LOG-checkFpEffect:" + "提交按钮依然置灰，发送错误报告，重试");
					sr.setStatus("2");
					sr.setDes("按钮置灰，请检查录入信息是否正确");
					//ChaoJiYing.ReportError(username, password,machineCode, picid);
					//System.out.println("LOG-checkFpEffect:" + "======================");
					//return this.fillForm(driver, tableid, fpxx, tryCount);
				}else{
					System.out.println("LOG-checkFpEffect:" + "尝试次数超过十次，失败");
					sr.setStatus("2");
					sr.setDes("按钮置灰，尝试次数超过十次，失败");
				}
			}
		}
		
		return sr;
	}

	
	private SaxResult fillFormByPhantomJS(WebDriver webDriver, String tableid,
			Fpxx fpxx, int tryCount) throws InterruptedException {
		PhantomJSDriver driver = (PhantomJSDriver)webDriver;
		
		String timestamp = new Date().getTime() + "";
		String yzm_filepath = filepath + "_" + timestamp + ".png";
		String yzm_filepath2 = filepath2 + "_" + timestamp + ".jpeg";
		String yzm_fileoutpath = fileoutpath + "_" + timestamp + ".jpeg";
		
		tryCount++;//尝试次数计数器 
		System.out.println("LOG-checkFpEffect:" + "开始第"+tryCount+"次获取发票信息");
		
		SaxResult sr = new SaxResult();
		SaxMsg sm = new SaxMsg();
		
		System.out.println("LOG-checkFpEffect:" + "清理验证码图片");
		//清理验证码图片
		String[] paths = {yzm_filepath,yzm_filepath2,yzm_fileoutpath};
		for (String p : paths) {
			File curfile = new File(p);
			if(curfile.exists()){
				curfile.delete();
			}
		}		
		
		System.out.println("LOG-checkFpEffect:" + "打开网页，赋值表单");
		
		driver.get("https://inv-veri.chinatax.gov.cn");
		
		int count = 0;
		while(true){
			count++;
			if(SeleniumUtils.doesWebElementExist(driver, By.id("fpdm"))){//刷不出网页
				break;
			}else if(count>10){
				System.out.println("LOG-checkFpEffect:" + "网页无法打开，请重试");
				sr.setStatus("2");
				sr.setDes("网页无法打开，请重试");
				return sr;
			}
			Thread.currentThread().sleep(1000);
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);//静待10秒
		
		
		
		WebElement fpdm = driver.findElement(By.id("fpdm"));
		fpdm.sendKeys(fpxx.getFpdm());
		WebElement fphm = driver.findElement(By.id("fphm"));
		fphm.sendKeys(fpxx.getFphm());
		WebElement kprq = driver.findElement(By.id("kprq"));
		kprq.sendKeys(fpxx.getKprq());
		WebElement kjje = driver.findElement(By.id("kjje"));
		kjje.sendKeys(fpxx.getKjje());
		
		System.out.println("LOG-checkFpEffect:" + "等待验证码刷新");
		
		WebElement yzm = null;
		Boolean yzmready = Boolean.FALSE;
		count = 0;
		while(Boolean.TRUE){
			count++;
			if(SeleniumUtils.doesWebElementExist(driver, By.id("yzm_img")) 
					&& driver.findElement(By.id("yzm_img")).getAttribute("src").startsWith("data:image/png;base64,")){
				yzmready = Boolean.TRUE;
				break;
			}else if(count>10){
				break;
			}
			Thread.currentThread().sleep(1000);
		}
		
		if (yzmready) {
			System.out.println("LOG-checkFpEffect:" + "验证码刷新成功");
			yzm = driver.findElement(By.id("yzm"));// 验证码输入框
			WebElement yzm_img = driver.findElement(By.id("yzm_img"));// 验证码base64字符串
			WebElement yzminfo = driver.findElement(By.id("yzminfo"));// 验证码提示
			String yzminfo_context = yzminfo.getText();
			WebElement yzminfoFont = null;
			if(SeleniumUtils.doesWebElementExist(driver, By.cssSelector("td#yzminfo font"))){
				yzminfoFont = driver.findElement(By.cssSelector("td#yzminfo font"));
			}
			
			int i = 0;
			while(true){
				try {
					i++;
					Thread.currentThread().sleep(1000);
					if(i==2){
						System.out.println("LOG-checkFpEffect:" + "全屏截图");
						SeleniumUtils.snapshot(driver, yzm_img, yzm_filepath);
						if(yzminfoFont!=null){
							SeleniumUtils.snapshot(driver, yzminfoFont, yzm_filepath2);
//							TestImage.exportImg1_snapshot("请输入",fileyzmmsA,45);
//							TestImage.exportImg1_snapshot("文字",fileyzmmsB,30);
							String[] tPaths = {fileyzmmsA,yzm_filepath2,fileyzmmsB};
							ImageHandleHelper.mergeImage(tPaths, 1, yzm_filepath2);
						}else{
							SeleniumUtils.snapshot(driver, yzminfo, yzm_filepath2);
						}
					}else if(i>10){break;}//超时跳出
					if(new File(yzm_filepath).exists()){break;}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			
			
			System.out.println("LOG-checkFpEffect:" + "获取验证码要求");
			TestImage.createWhitePic(yzm_fileoutpath);
			ImageHandleHelper.overlapImageA(yzm_fileoutpath, yzm_filepath2, yzm_fileoutpath, "1");
			//TestImage.exportImg1_snapshot(yzminfo_context, yzm_filepath2);// 要求也写到本地
			System.out.println("LOG-checkFpEffect:" + "拼接图片");
			ImageHandleHelper.overlapImageA(yzm_fileoutpath, yzm_filepath, yzm_fileoutpath,"2");// 拼接图片
			System.out.println("LOG-checkFpEffect:" + "拼接结束");
			
			while(true){
				try {
					if(new File(yzm_fileoutpath).exists()){break;}
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("LOG-checkFpEffect:" + "调用超级鹰接口解码");
			//String yzm_res = "{\"err_no\":0,\"err_str\":\"OK\",\"pic_id\":\"262128065301\",\"pic_str\":\"\u8fd9\",\"md5\":\"657907e379e81c494cb11ee1221150c3\",\"str_debug\":\"trans-message\"}";
			String yzm_res = ChaoJiYing.PostPic(username, password,machineCode, codeType, "0", "0", "trans-message",yzm_fileoutpath);
			System.out.println("LOG-checkFpEffect:超级鹰回复:" + yzm_res);
			if(yzm_res.contains("未知错误")){
				throw new RuntimeException("验证码错误");
			}
			
			YzmResult yzmres = JsonUtils.jsonToJavaBean(yzm_res,YzmResult.class);
			// json字符串转对象
			System.out.println(yzmres.getPic_str());
			yzm.sendKeys(yzmres.getPic_str());
			String picid = yzmres.getPic_id();
			System.out.println(picid);
			
			WebElement checkfp = driver.findElement(By.id("checkfp"));// 查验按钮
			
			if (checkfp.getAttribute("style").equals("display: inline-block;")) {
				checkfp.click();
				try {
					
					WebElement tableDiv = null;
					if(SeleniumUtils.doesWebElementExist(driver, By.id(tableid))){
						tableDiv = driver.findElement(By.id(tableid));
						List<WebElement> hrList = tableDiv.findElements(By.cssSelector("table > tbody > tr > td > span")); // By.xpath("//table/tbody/tr/td/span")
						if (CollectionUtils.isNotEmpty(hrList)) {
							System.out.println("LOG-checkFpEffect:" + "获取发票表单成功");
							if(hrList.size()<=4){
								System.out.println("LOG-checkFpEffect:" + "表单只有四项信息-查无此发票");
								WebElement fpWindow = driver.findElement(By.id("con_tent"));// 发票窗口
								String cyjg = driver.findElement(By.id("cyjg")).getText();
								String fpname = CommonHelper.getNowStr("yyyyMMddHHmmss")+"_"+fpxx.getFpdm()+"_"+fpxx.getFphm()+".png";
								String fppath = contextDir +"/fp_pic/"+fpname;
								System.out.println("LOG-checkFpEffect:" + "结果窗口截屏");
								SeleniumUtils.phatomJSSnapshot(driver, fpWindow, fppath);
								while(true){
									try {
										Thread.sleep(1000);
										if(new File(fppath).exists()){
											System.out.println("LOG-checkFpEffect:" + "发票截图转码");
											sm.setFp_base64(Base64Utils.GetImageStr(fppath));
											File curfile = new File(fppath);
											curfile.delete();
											break;}
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
								sr.setStatus("2");
								sr.setSaxmsg(sm);
								sr.setDes(cyjg);
							}else{
								
								
								//可能还会有款项详情的表单，先看显示详情的按钮是否存在
								if(SeleniumUtils.doesWebElementExist(tableDiv, By.id("showmx"))){
									//带有详情按钮的表格
									System.out.println("LOG-checkFpEffect:" + "带有详情按钮的表格");
									int counter = 0;
									//先把普通表格上的信息拿走
									for (WebElement we : hrList) {
										System.out.println(counter+":" + we.getText());
										switch (counter) {
										case 0:{sm.setFpdm(we.getText());break;}
										case 1:{sm.setFphm(we.getText());break;}
										case 2:{sm.setKpsj(we.getText());break;}//开票时间
										case 3:{sm.setJym(we.getText());break;}//校验码
										case 4:{sm.setJqhm(we.getText());break;}//机器号
										case 5:{sm.setGmf_mc(we.getText());break;}//购买方 名称
										case 6:{sm.setGmf_nsrsbh(we.getText());break;}//购买方 纳税人识别号
										case 7:{sm.setGmf_dzdh(we.getText());break;} //地址电话
										case 8:{sm.setGmf_khhjzh(we.getText());break;} //开户行及账号
										case 25:{sm.setJe_dw(we.getText());break;}//金额单位
										case 26:{sm.setSe_dw(we.getText());break;}//税额单位
										case 27:{sm.setJshjdx(we.getText());break;}//大写
										case 29:{sm.setJshjxx(we.getText());break;}//小写
										case 30:{sm.setXsf_mc(we.getText());break;}//销售方 名称
										case 31:{sm.setXsf_nsrsbh(we.getText());break;}//销售方 纳税人识别号
										case 32:{sm.setXsf_dzdh(we.getText());break;}//销售方 地址电话
										case 33:{sm.setXsf_khhjzh(we.getText());break;}//销售方 开户行及账号
										default:
											break;
										}
										counter++;
									}
									
									System.out.println("LOG-checkFpEffect:" + "点击显示详情按钮");
									
									tableDiv.findElement(By.id("showmx")).click();//点击 显示详情
									WebElement mxDiv = null;
									if(SeleniumUtils.doesWebElementExist(driver, By.id("hwmxqd"))){
										mxDiv = driver.findElement(By.id("hwmxqd"));
										List<WebElement> mxList = mxDiv.findElements(By.cssSelector("table > tbody > tr > td "));
										if (CollectionUtils.isNotEmpty(mxList)) {
											System.out.println("LOG-checkFpEffect:" + "获得详情表格");
											int mxcount = 0;
											int startIndex = 15;
											for (WebElement mxwe : mxList) {
												System.out.println(mxcount+":" + mxwe.getText());
												if(mxcount>=15){
													if("小计".equals(mxwe.getText())){
														System.out.println("LOG-checkFpEffect:" + "获取完毕，截屏");
														String fpname = CommonHelper.getNowStr("yyyyMMddHHmmss")+"_"+fpxx.getFpdm()+"_"+fpxx.getFphm()+".png";
														String fppath = contextDir +"/fp_pic/"+fpname;
														SeleniumUtils.phatomJSSnapshot(driver, mxDiv, fppath);
														while(true){
															try {
																Thread.sleep(1000);
																if(new File(fppath).exists()){
																	System.out.println("LOG-checkFpEffect:" + "详情截图转码");
																	sm.setFpxq_pic(fpname);
																	sm.setFpxq_base64(Base64Utils.GetImageStr(fppath));
																	File curfile = new File(fppath);
																	curfile.delete();
																	break;}
															} catch (InterruptedException e) {
																e.printStackTrace();
															}
														}
														System.out.println("LOG-checkFpEffect:" + "关闭详情窗口");
														mxDiv.findElement(By.id("mxclose")).click();//关闭窗口
														break;
													}
													if(startIndex+1==mxcount){sm.setYsmc(strC(sm.getYsmc())+mxwe.getText());}
													else if(startIndex+2==mxcount){sm.setGgxh(strC(sm.getGgxh())+mxwe.getText());}
													else if(startIndex+3==mxcount){sm.setDw(strC(sm.getDw())+mxwe.getText());}
													else if(startIndex+4==mxcount){sm.setSl(strC(sm.getSl())+mxwe.getText());}
													else if(startIndex+5==mxcount){sm.setDj(strC(sm.getDj())+mxwe.getText());}
													else if(startIndex+6==mxcount){sm.setJe(strC(sm.getJe())+mxwe.getText());}
													else if(startIndex+7==mxcount){sm.setSlv(strC(sm.getSlv())+mxwe.getText());}
													else if(startIndex+8==mxcount){sm.setSe(strC(sm.getSe())+mxwe.getText());
													startIndex=mxcount+1;}
												}
												mxcount++;
											}
										}
									}else{
										throw new RuntimeException("详细信息弹窗无法出现");
									}
									
									
									
								}else{
									//正常表格
									System.out.println("LOG-checkFpEffect:" + "正常表格");
									
									boolean flag = Boolean.FALSE;
									int counter = 0;
									
//									if(SeleniumUtils.doesWebElementExist(driver, By.id("tabPage-jsfp"))){
//										System.out.println("LOG-checkFpEffect:" + "卷票");
//										
//										int startIndex = 10;
//										
//										for (WebElement we : hrList) {
//											System.out.println(counter+":" + we.getText());
//											if(counter<10 && counter>-1){
//												switch (counter) {
//													case 0:{sm.setFpdm(we.getText());break;}
//													case 1:{sm.setFphm(we.getText());break;}
//													case 2:{sm.setJdhm(we.getText());break;}
//													case 3:{sm.setJqhm(we.getText());break;}
//													case 4:{sm.setXsf_mc(we.getText());break;}
//													case 5:{sm.setXsf_nsrsbh(we.getText());break;}
//													case 6:{sm.setKpsj(we.getText());break;}
//													case 7:{sm.setShy(we.getText());break;}
//													case 8:{sm.setGmf_mc(we.getText());break;}
//													case 9:{sm.setGmf_nsrsbh(we.getText());break;}
//													default:
//														break;
//												}
//											}else if(counter>=10){
//												if(!flag && (counter==10 || (counter-10)%4==0)){
//													if(we.getText().contains("￥")){
//														sm.setJe_xx(we.getText());
//														flag = Boolean.TRUE;
//													}else{
//														sm.setYsmc(strC(sm.getYsmc())+we.getText());
//													}
//													startIndex = counter;
//												}else{
//													if(flag){//正常
//														if(startIndex+1==counter){sm.setJe_dx(we.getText());}
//														else if(startIndex+2==counter){sm.setJym(we.getText());}
//													}else{
//														if(startIndex+1==counter){sm.setDj(strC(sm.getDj())+we.getText());}
//														else if(startIndex+2==counter){sm.setSl(strC(sm.getSl())+we.getText());}
//														else if(startIndex+3==counter){sm.setJe(strC(sm.getJe())+we.getText());}
//													}
//												}
//											}
//											
//											counter++;
//										}
//										
//									}else{
									System.out.println("LOG-checkFpEffect:" + "普通发票");
									
									int startIndex = 17;
									
									for (WebElement we : hrList) {
										System.out.println(counter+":" + we.getText());
										if(counter<17 && counter>-1){
											switch (counter) {
											case 0:{sm.setFpdm(we.getText());break;}
											case 1:{sm.setFphm(we.getText());break;}
											case 2:{sm.setKpsj(we.getText());break;}
											case 3:{sm.setJym(we.getText());break;}
											case 4:{sm.setJqhm(we.getText());break;}
											case 5:{sm.setGmf_mc(we.getText());break;}
											case 6:{sm.setGmf_nsrsbh(we.getText());break;}
											case 7:{sm.setGmf_dzdh(we.getText());break;}
											case 8:{sm.setGmf_khhjzh(we.getText());break;}
											case 9:{sm.setYsmc(we.getText());break;}	
											case 10:{sm.setGgxh(we.getText());break;}
											case 11:{sm.setDw(we.getText());break;}
											case 12:{sm.setSl(we.getText());break;}
											case 13:{sm.setDj(we.getText());break;}
											case 14:{sm.setJe(we.getText());break;}
											case 15:{sm.setSlv(we.getText());break;}
											case 16:{sm.setSe(we.getText());break;}
											default:
												break;
											}
										}else if(counter>=17){
											
											if(!flag && (counter==17 || (counter-17)%8==0)){
												if(we.getText().contains("￥")){
													sm.setJe_dw(we.getText());
													flag = Boolean.TRUE;
												}else{
													sm.setYsmc(strC(sm.getYsmc())+we.getText());
												}
												startIndex = counter;
											}else{
												if(flag){//正常
													if(startIndex+1==counter){sm.setSe_dw(we.getText());}
													else if(startIndex+2==counter){sm.setJshjdx(we.getText());}
													else if(startIndex+4==counter){sm.setJshjxx(we.getText());}
													else if(startIndex+5==counter){sm.setXsf_mc(we.getText());}
													else if(startIndex+6==counter){sm.setXsf_nsrsbh(we.getText());}
													else if(startIndex+7==counter){sm.setXsf_dzdh(we.getText());}
													else if(startIndex+8==counter){sm.setXsf_khhjzh(we.getText());}
												}else{
													if(startIndex+1==counter){sm.setGgxh(strC(sm.getGgxh())+we.getText());}
													else if(startIndex+2==counter){sm.setDw(strC(sm.getDw())+we.getText());}
													else if(startIndex+3==counter){sm.setSl(strC(sm.getSl())+we.getText());}
													else if(startIndex+4==counter){sm.setDj(strC(sm.getDj())+we.getText());}
													else if(startIndex+5==counter){sm.setJe(strC(sm.getJe())+we.getText());}
													else if(startIndex+6==counter){sm.setSlv(strC(sm.getSlv())+we.getText());}
													else if(startIndex+7==counter){sm.setSe(strC(sm.getSe())+we.getText());}
												}
											}
											
										}
										counter++;
									}
									//}
									
									
									
								}
								//检查发票有效性
								System.out.println("LOG-checkFpEffect:" + "检查发票有效性");
								if(SeleniumUtils.doesWebElementExist(driver, By.id("icon_zf"))){
									WebElement zfImgDiv = driver.findElement(By.id("icon_zf"));
									if(zfImgDiv.isDisplayed()){
										sm.setIsValid(Boolean.FALSE);
									}
								}
								
								//两种共有的代码
								System.out.println("LOG-checkFpEffect:" + "获取发票窗口");
								WebElement fpWindow = driver.findElement(By.id("bg_div"));// 发票窗口
								String fpname = CommonHelper.getNowStr("yyyyMMddHHmmss")+"_"+fpxx.getFpdm()+"_"+fpxx.getFphm()+".png";
								String fppath = contextDir +"/fp_pic/"+fpname;
								System.out.println("LOG-checkFpEffect:" + "发票窗口截屏");
								SeleniumUtils.phatomJSSnapshot(driver, fpWindow, fppath);
								
								
								while(true){
									try {
										Thread.sleep(1000);
										if(new File(fppath).exists()){
											System.out.println("LOG-checkFpEffect:" + "发票截图转码");
											sm.setFp_base64(Base64Utils.GetImageStr(fppath));
											File curfile = new File(fppath);
											curfile.delete();
											break;}
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
								
								
								sm.setFp_pic(fpname);
								sr.setStatus("1");
								sr.setSaxmsg(sm);
								System.out.println("LOG-checkFpEffect:" + "成功！");
							}
							
						}
					}else{
						throw new RuntimeException("发票信息窗口不存在");
					}
					
					
					
				} catch (Exception e) {
					System.out.println("LOG-checkFpEffect:" + "获取发票表单失败");
					
					//WebElement tableDiv = driver.findElement(By.id("popup_container"));// table
					String alert_msg = "";
					if(SeleniumUtils.doesWebElementExist(driver, By.id("popup_message"))){
						alert_msg = driver.findElement(By.id("popup_message")).getText();
					}else{
						if(e.toString().indexOf("TimeOutException")>-1){
							alert_msg = "刷新超时";
						}else if(e.getMessage().contains("超级鹰识别失败")){
							alert_msg = "验证码错误(超级鹰识别失败)";
						}else{
							alert_msg = "未知错误，记下报文"+fpxx.getFpdm()+","+fpxx.getFphm();
						}
					}
					
					System.out.println("LOG-checkFpEffect:" + "失败原因：" + alert_msg);
					
					if (alert_msg.indexOf("验证码错误") > -1) {
						// 验证失败 发送错误报告
						System.out.println("LOG-checkFpEffect:" + "验证码解码错误，发送错误报告");
						ChaoJiYing.ReportError(username, password,machineCode, picid);
						if(tryCount<10){
							System.out.println("LOG-checkFpEffect:" + "重试");
							System.out.println("LOG-checkFpEffect:" + "======================");
							return this.fillFormByPhantomJS(driver, tableid, fpxx, tryCount);
						}else{
							System.out.println("LOG-checkFpEffect:" + "尝试次数超过十次，失败");
							sr.setStatus("2");
							sr.setDes("尝试次数超过十次，失败");
						}
					} else {
						sr.setStatus("2");
						sr.setDes(alert_msg);
						System.out.println(alert_msg);
					}
				}finally{
					//清理验证码图片
					for (String p : paths) {
						File curfile = new File(p);
						if(curfile.exists()){
							curfile.delete();
						}
					}
				}
				
			}else{
				if(tryCount<10){
					System.out.println("LOG-checkFpEffect:" + "提交按钮依然置灰，发送错误报告，重试");
					sr.setStatus("2");
					sr.setDes("按钮置灰，请检查录入信息是否正确");
					//ChaoJiYing.ReportError(username, password,machineCode, picid);
					//System.out.println("LOG-checkFpEffect:" + "======================");
					//return this.fillForm(driver, tableid, fpxx, tryCount);
				}else{
					System.out.println("LOG-checkFpEffect:" + "尝试次数超过十次，失败");
					sr.setStatus("2");
					sr.setDes("按钮置灰，尝试次数超过十次，失败");
				}
			}
		}else{
			System.out.println("LOG-checkFpEffect:" + "验证码刷新失败，请重新获取");
			sr.setStatus("2");
			sr.setDes("验证码刷新失败，请重新获取");
		}
		
		return sr;
	}
	
	@Test
	public void testmethod(){
		SaxResult sr = new SaxResult();
		sr.setStatus("0");
		sr.setDes("没有接收到来自客户端的发票信息");
		System.out.println(JsonUtils.JavaBeanToJson(sr));
	}
	
	private String strC(String input){
		if(StringUtils.isNotBlank(input)){
			return input + "&&";
		}else{
			return StringUtils.EMPTY;
		}
	}

	
}
