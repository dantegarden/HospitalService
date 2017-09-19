package com.dvt.HospitalService.commons.utils;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SeleniumUtils {
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
	
}
