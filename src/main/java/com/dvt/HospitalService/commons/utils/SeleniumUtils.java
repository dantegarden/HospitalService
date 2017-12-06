package com.dvt.HospitalService.commons.utils;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.RemoteWebElement;

public class SeleniumUtils {
	//直接获取验证码
	//TODO
	public static void getUnionYzm(PhantomJSDriver driver){
		
	}
	
	public static void phatomJSSnapshot(PhantomJSDriver driver,WebElement wel,String outputPath) throws InterruptedException{
		String id = wel.getAttribute("id");
		Boolean WeblHeightUnlock = Boolean.FALSE;
		if(SeleniumUtils.doesWebElementExist(driver, By.id("floatwin"))
				&&("bg_div".equals(id)||"hwmxqd".equals(id))){
			WebElement floatwinDiv = driver.findElement(By.id("floatwin"));
			driver.executeScript(" $('#floatwin')[0].style.overflow = 'visible'", floatwinDiv);
			int count = 0;
			while (true) {//等待js生效
				count++;
				if("visible".equals(floatwinDiv.getCssValue("overflow"))){
					WeblHeightUnlock = Boolean.TRUE;
					break;
				}else if(count>10){
					break;
				}else{
					Thread.currentThread().sleep(1000);
				}
			}
			
		}
		if(WeblHeightUnlock){
			File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			try {
				while(true){
	        		if(srcFile.exists()){
	        			break;
	        		}else{
	        			Thread.currentThread().sleep(1000);
	        		}
	        	}
				
				Point p = wel.getLocation();
				int width = wel.getSize().getWidth();
				int height = wel.getSize().getHeight();
				
				int top = wel.getLocation().getY();
				int left = wel.getLocation().getX();
				int right = left + width;
				int bottom = top + height;
				
				Rectangle rect = new Rectangle(width, height);
				BufferedImage img = ImageIO.read(srcFile);
				BufferedImage dest = img.getSubimage(p.getX(), p.getY(), rect.width, rect.height);
				ImageIO.write(dest, "png", srcFile);
				Thread.sleep(1000);
				File fng = new File(outputPath);
				if(fng.exists()){
					fng.delete();
				}
				FileUtils.copyFile(srcFile,fng);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void snapshot(WebDriver driver,WebElement wel,String outputPath){
		String id = wel.getAttribute("id");
		
		if(SeleniumUtils.doesWebElementExist(driver, By.id("floatwin"))
				&&("bg_div".equals(id)||"hwmxqd".equals(id))){
			WebElement floatwinDiv = driver.findElement(By.id("floatwin"));
			JavascriptExecutor js = (JavascriptExecutor) driver;
			Object obj = js.executeScript("$('#floatwin')[0].style.cssText = 'display:block;overflow:unset!important;'");
		}
		
        File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try {
        	while(true){
        		if(srcFile.exists()){
        			break;
        		}else{
        			Thread.currentThread().sleep(1000);
        		}
        	}
        	
        	Point p = wel.getLocation();
        	int width = wel.getSize().getWidth();
        	int height = wel.getSize().getHeight();

        	int top = wel.getLocation().getY();
        	int left = wel.getLocation().getX();
        	int right = left + width;
        	int bottom = top + height;
        	
        	Rectangle rect = new Rectangle(width, height);
            BufferedImage img = ImageIO.read(srcFile);
            BufferedImage dest = img.getSubimage(p.getX(), p.getY(), rect.width, rect.height);
            ImageIO.write(dest, "png", srcFile);
            Thread.sleep(1000);
            File fng = new File(outputPath);
            if(fng.exists()){
                fng.delete();
            }
            FileUtils.copyFile(srcFile,fng);
            
        } catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static boolean doesWebElementExist(WebDriver driver, By selector){
		try {
			 driver.findElement(selector);
			 return true; 
		} catch (NoSuchElementException  e) {
			return false; 
		}
	}
	
	public static boolean doesWebElementExist(WebElement we, By selector){
		try {
			 we.findElement(selector);
			 return true; 
		} catch (NoSuchElementException  e) {
			return false; 
		}
	}
	
	/**
     * 滚动窗口。
     * @param driver
     * @param height
     */
    public static void scroll(WebDriver driver,int height){
        ((JavascriptExecutor)driver).executeScript("window.scrollTo(0,"+height+" );");    
    }
    
    /**
     * 重新调整窗口大小，以适应页面，需要耗费一定时间。建议等待合理的时间。
     * @param driver
     */
    public static void loadAll(WebDriver driver){
        Dimension od=driver.manage().window().getSize();
        int width=driver.manage().window().getSize().width;
        //尝试性解决：https://github.com/ariya/phantomjs/issues/11526问题
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS); 
        long height=(Long)((JavascriptExecutor)driver).executeScript("return document.body.scrollHeight;");
        driver.manage().window().setSize(new Dimension(width, (int)height));
        driver.navigate().refresh();
    }
    
    /**截全屏
     * @param saveFile 保存到文件 
     * **/
    public static void taskScreenShot(WebDriver driver,File saveFile){
        File src=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(src, saveFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void changeWindow(WebDriver driver){
        // 获取当前页面句柄
        String handle = driver.getWindowHandle();
        // 获取所有页面的句柄，并循环判断不是当前的句柄，就做选取switchTo()
        for (String handles : driver.getWindowHandles()) {
            if (handles.equals(handle))
                continue;
            driver.switchTo().window(handles);
        }
    }
    
    public static void changeWindowTo(WebDriver driver,String handle){
        for (String tmp : driver.getWindowHandles()) {
            if (tmp.equals(handle)){
                driver.switchTo().window(handle);
                break;
            }
        }
    }
    
    /**
     * 打开一个新tab页，返回该tab页的windowhandle
     * @param driver
     * @param url
     * @return
     */
    public static String openNewTab(WebDriver driver,String url){
        Set<String> strSet1=driver.getWindowHandles();
        ((JavascriptExecutor)driver).executeScript("window.open('"+url+"','_blank');");
        sleep(1000);
        Set<String> strSet2=driver.getWindowHandles();
        for(String tmp:strSet2){
            if(!strSet1.contains(tmp)){
                return tmp;
            }
        }
        return null;
    }
    public static void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 操作关闭模态窗口
     * @param driver
     * @param type 如Id,ClassName
     * @param sel 选择器
     */
    public static void clickModal(WebDriver driver,String type,String sel){
        String js="document.getElementsBy"+type+"('"+sel+"')[0].click();";
        ((JavascriptExecutor)driver).executeScript(js);
    }
}
