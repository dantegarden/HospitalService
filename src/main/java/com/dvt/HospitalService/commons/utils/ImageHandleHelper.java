package com.dvt.HospitalService.commons.utils;

import java.awt.Graphics2D;  
import java.awt.Rectangle;  
import java.awt.image.BufferedImage;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.InputStream;  
import java.util.Arrays;
import java.util.Iterator;  
  


import javax.imageio.ImageIO;  
import javax.imageio.ImageReadParam;  
import javax.imageio.ImageReader;  
import javax.imageio.stream.ImageInputStream;  

import com.sun.prism.paint.Color;


public class ImageHandleHelper {
	/** 
     * @Description:鎴浘 
     * @author:liuyc 
     * @time:2016骞�鏈�7鏃�涓婂崍10:18:23 
     * @param srcFile婧愬浘鐗囥�targetFile鎴ソ鍚庡浘鐗囧叏鍚嶃�startAcross 寮�鎴彇浣嶇疆妯潗鏍囥�StartEndlong寮�鎴浘浣嶇疆绾靛潗鏍囥�width鎴彇鐨勯暱锛宧ight鎴彇鐨勯珮 
     */  
    public static void cutImage(String srcFile, String targetFile, int startAcross, int StartEndlong, int width,  
            int hight) throws Exception {  
        // 鍙栧緱鍥剧墖璇诲叆鍣� 
        Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("jpg");  
        ImageReader reader = readers.next();  
        // 鍙栧緱鍥剧墖璇诲叆娴� 
        InputStream source = new FileInputStream(srcFile);  
        ImageInputStream iis = ImageIO.createImageInputStream(source);  
        reader.setInput(iis, true);  
        // 鍥剧墖鍙傛暟瀵硅薄  
        ImageReadParam param = reader.getDefaultReadParam();  
        Rectangle rect = new Rectangle(startAcross, StartEndlong, width, hight);  
        param.setSourceRegion(rect);  
        BufferedImage bi = reader.read(0, param);  
        ImageIO.write(bi, targetFile.split("\\.")[1], new File(targetFile));  
    }  
  
    /** 
     * @Description:鍥剧墖鎷兼帴 锛堟敞鎰忥細蹇呴』涓ゅ紶鍥剧墖闀垮涓�嚧鍝︼級 
     * @author:liuyc 
     * @time:2016骞�鏈�7鏃�涓嬪崍5:52:24 
     * @param files 瑕佹嫾鎺ョ殑鏂囦欢鍒楄〃 
     * @param type1  妯悜鎷兼帴锛�2 绾靛悜鎷兼帴 
     */  
    public static void mergeImage(String[] files, int type, String targetFile) {  
        int len = files.length;  
        if (len < 1) {  
            throw new RuntimeException("图片小于两张");  
        }  
        File[] src = new File[len];  
        BufferedImage[] images = new BufferedImage[len];  
        int[][] ImageArrays = new int[len][];  
        for (int i = 0; i < len; i++) {  
            try {  
            	System.out.println(files[i]);
                src[i] = new File(files[i]);  
                images[i] = ImageIO.read(src[i]);  
            } catch (Exception e) {  
                throw new RuntimeException(e);  
            }  
            int width = images[i].getWidth();  
            int height = images[i].getHeight();  
            ImageArrays[i] = new int[width * height];  
            ImageArrays[i] = images[i].getRGB(0, 0, width, height, ImageArrays[i], 0, width);  
        }  
        int newHeight = 0;  
        int newWidth = 0;  
        for (int i = 0; i < images.length; i++) {  
            // 妯悜  
            if (type == 1) {  
                newHeight = newHeight > images[i].getHeight() ? newHeight : images[i].getHeight();  
                newWidth += images[i].getWidth();  
            } else if (type == 2) {// 绾靛悜  
                newWidth = newWidth > images[i].getWidth() ? newWidth : images[i].getWidth();  
                newHeight += images[i].getHeight();  
            }  
        }  
        if (type == 1 && newWidth < 1) {  
            return;  
        }  
        if (type == 2 && newHeight < 1) {  
            return;  
        }  
  
        // 鐢熸垚鏂板浘鐗� 
        try {  
            BufferedImage ImageNew = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);  
            int height_i = 0;  
            int width_i = 0;  
            for (int i = 0; i < images.length; i++) {  
                if (type == 1) {  
                    ImageNew.setRGB(width_i, 0, images[i].getWidth(), images[i].getHeight(), ImageArrays[i], 0,  
                            images[i].getWidth());  
                    width_i += images[i].getWidth();  
                } else if (type == 2) {  
                    ImageNew.setRGB(0, height_i, newWidth, images[i].getHeight(), ImageArrays[i], 0, newWidth);  
                    height_i += images[i].getHeight();  
                }  
            }  
            //杈撳嚭鎯宠鐨勫浘鐗� 
            File file = new File(targetFile);     
            ImageIO.write(ImageNew, "jpeg", file);  
  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    /** 
     * @Description:灏忓浘鐗囪创鍒板ぇ鍥剧墖褰㈡垚涓�紶鍥�鍚堟垚) 
     * @author:liuyc 
     * @time:2016骞�鏈�7鏃�涓嬪崍5:51:20 
     */  
    public static final void overlapImageA(String bigPath, String smallPath, String outFile, String heightType) {  
        try {  
            BufferedImage big = ImageIO.read(new File(bigPath));  
            BufferedImage small = ImageIO.read(new File(smallPath));  
            Graphics2D g = big.createGraphics();  
            int x = (big.getWidth() - small.getWidth()) / 2;  
            int y = 0;
            if("1".equals(heightType)){
            	y = big.getHeight()-small.getHeight();//(big.getHeight() - small.getHeight()) / 2;  
            }else if("2".equals(heightType)){
            	y = 0;//(big.getHeight() - small.getHeight()) / 2;  
            }
            g.drawImage(small, x, y, small.getWidth(), small.getHeight(), null);  
            g.dispose();  
            ImageIO.write(big, "jpeg", new File(outFile));
            //Thread.sleep(1000);
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }
    
    public static final void overlapImage(String bigPath, String smallPath, String outFile) {  
        try {  
            BufferedImage big = ImageIO.read(new File(bigPath));  
            BufferedImage small = ImageIO.read(new File(smallPath));  
            Graphics2D g = big.createGraphics();  
            int x = (big.getWidth() - small.getWidth()) / 2;  
            int y = 0;
//            if("1".equals(heightType)){
//            	y = big.getHeight()-small.getHeight();//(big.getHeight() - small.getHeight()) / 2;  
//            }else if("2".equals(heightType)){
//            	y = 0;//(big.getHeight() - small.getHeight()) / 2;  
//            }
            g.drawImage(small, x, y, small.getWidth(), small.getHeight(), null);  
            g.dispose();  
            ImageIO.write(big, "jpeg", new File(outFile));
            //Thread.sleep(1000);
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }
}
