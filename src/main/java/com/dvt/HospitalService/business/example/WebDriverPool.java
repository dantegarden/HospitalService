package com.dvt.HospitalService.business.example;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

public class WebDriverPool {
	private static final Logger logger = LoggerFactory.getLogger(WebDriverPool.class);
	
	private int CAPACITY = 10;
    private AtomicInteger refCount = new AtomicInteger(0);
    private static final String DRIVER_PHANTOMJS = "phantomjs";
    
    private static List<String> ua = ImmutableList.of(
			"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0",
			"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:44.0) Gecko/20100101 Firefox/44.0",
			"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:45.0) Gecko/20100101 Firefox/45.0",
			"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0",
			"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0",
			"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0",
			"Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0",
			"Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:51.0) Gecko/20100101 Firefox/51.0",
			"Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:52.0) Gecko/20100101 Firefox/52.0");
    
    /**存储webdriver*/
    private BlockingDeque<WebDriver> innerQueue = new LinkedBlockingDeque<WebDriver>(CAPACITY);
    
    private static String PHANTOMJS_PATH;
    private static DesiredCapabilities dcaps = DesiredCapabilities.phantomjs();
    
    static{
    	//共通配置
    	Properties prop = new Properties();
    	try {
			prop.load(WebDriverPool.class.getClassLoader().getResourceAsStream("deploy.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	PHANTOMJS_PATH = prop.getProperty("phantomjs.path"); //"E://chrome//phantomjs-2.1.1-windows//bin//phantomjs.exe";
    	
		//ssl证书支持
        dcaps.setCapability("acceptSslCerts", true);
        //截屏支持
        dcaps.setCapability("takesScreenshot", true);
        //css搜索支持
        dcaps.setCapability("cssSelectorsEnabled", true);
        //js支持
        dcaps.setJavascriptEnabled(true);
        //允许加载图片
        dcaps.setCapability("phantomjs.page.settings.loadImages", true);
        //驱动支持
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, PHANTOMJS_PATH);
        //禁止证书错误提示
        String [] phantomJsArgs = {"--ignore-ssl-errors=true","--ssl-protocol=any"};
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, phantomJsArgs);
    }
    
    public WebDriverPool() {
    }

    public WebDriverPool(int poolsize) {
        this.CAPACITY = poolsize;
        innerQueue = new LinkedBlockingDeque<WebDriver>(poolsize);
    }
    public WebDriver get() throws InterruptedException {
    	WebDriver poll = innerQueue.poll();//拿走排在队列第一位的元素
    	if (poll != null) {
            return poll;
        }
    	if (refCount.get() < CAPACITY) {
    		synchronized (innerQueue) {
    			if (refCount.get() < CAPACITY) {
    				//伪装浏览器头
    				Random random = new Random();
    				String userAgent = ua.get(random.nextInt(ua.size()));
    				dcaps.setCapability("phantomjs.page.settings.userAgent", userAgent);
    				dcaps.setCapability("phantomjs.page.customHeaders.User-Agent", userAgent);
    				//实例化驱动
    				WebDriver mDriver = new PhantomJSDriver(dcaps);
    				mDriver.manage().window().maximize();
    				mDriver.manage().timeouts().pageLoadTimeout(60000, TimeUnit.SECONDS);//访问网页超时
    				mDriver.manage().timeouts().setScriptTimeout(10000, TimeUnit.SECONDS);//设置脚本超时
    				innerQueue.add(mDriver);
                    refCount.incrementAndGet();//自增
    			}
    		}
    	}
    	return innerQueue.take();//返回第一个，返回的元素会从类表中移除
    }
    
    public void returnToPool(WebDriver webDriver) {
        // webDriver.quit();
        // webDriver=null;
        innerQueue.add(webDriver);
    }
    
    public void close(WebDriver webDriver) {
        refCount.decrementAndGet();
        webDriver.close();
        webDriver.quit();
        webDriver = null;
    }
    
    public void shutdown() {
        try {
            for (WebDriver driver : innerQueue) {
                close(driver);
            }
            innerQueue.clear();
        } catch (Exception e) {
//            e.printStackTrace();
            logger.warn("WebDriverpool关闭失败",e);
        }
    }
}
