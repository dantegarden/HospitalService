package com.dvt.HospitalService.commons.utils;

import java.awt.Color;  
import java.awt.Font;  
import java.awt.Graphics;  
import java.awt.Graphics2D;  
import java.awt.Image;  
import java.awt.color.ColorSpace;
import java.awt.font.FontRenderContext;  
import java.awt.geom.Rectangle2D;  
import java.awt.image.BufferedImage;  
import java.awt.image.ColorConvertOp;
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;  
import java.text.ParseException;  
import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  
  





import javax.imageio.ImageIO;  
import javax.swing.ImageIcon;  
  





import com.sun.image.codec.jpeg.ImageFormatException;  
import com.sun.image.codec.jpeg.JPEGCodec;  
import com.sun.image.codec.jpeg.JPEGImageDecoder;  
import com.sun.image.codec.jpeg.JPEGImageEncoder; 

public class TestImage {
	/** 
     * @param args 
     * @throws ParseException  
     */  
    public static void main(String[] args) throws ParseException {  
    	String dir = System.getProperty("user.dir");
    	//exportImg1("请输入验证码图片中蓝色文字",dir+"\\yzm_pic\\yzm_ms.jpeg");
    	//exportImg1_snapshot("请输入",dir+"\\yzm_pic\\yzm_ms.jpeg",45);
    	//exportImg1_snapshot("文字",dir+"\\yzm_pic\\yzm_ms2.jpeg",30);
    	String[] paths = {"E:\\Program Files\\apache-tomcat-8.0.41\\webapps\\SaxService\\yzm_pic\\yzm_ms1.jpeg",
    			"E:\\Program Files\\apache-tomcat-8.0.41\\webapps\\SaxService\\yzm_pic\\yzm_ms2.jpeg",
    			"E:\\Program Files\\apache-tomcat-8.0.41\\webapps\\SaxService\\yzm_pic\\yzm_ms3.jpeg"};
    	ImageHandleHelper.mergeImage(paths, 1, dir+"\\yzm_pic\\yzm_out.jpeg");
    	//ImageHandleHelper.overlapImage(dir+"\\yzm_pic\\yzm_ms.jpeg", dir+"\\yzm_pic\\yzm.png", dir+"\\yzm_pic\\union.jpeg");
    	//exportImg2("######","d://yzm.jpeg");  
    }  
  
      
    public static void exportImg1(String words,String path){  
        int width = 200;     
        int height = 70;     
        String s = words;     
             
        File file = new File(path);     
             
        Font font = new Font("微软雅黑", Font.BOLD, 10);     
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);     
        Graphics2D g2 = (Graphics2D)bi.getGraphics();     
        g2.setBackground(Color.WHITE);     
        g2.clearRect(0, 0, width, height);     
        g2.setPaint(Color.black);     
             
        FontRenderContext context = g2.getFontRenderContext();     
        Rectangle2D bounds = font.getStringBounds(s, context);     
        double x = (width - bounds.getWidth()) / 2;     
        double y = (height - bounds.getHeight()) / 2 + 20;     
        double ascent = -bounds.getY();     
        double baseY = y + ascent;     
             
        g2.drawString(s, (int)x, (int)baseY);     
             
        try {  
            ImageIO.write(bi, "jpeg", file);  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }   
    }  
    
    public static void createWhitePic(String path){
    	int width = 120;     
        int height = 80;     
             
        File file = new File(path);     
             
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);     
        Graphics2D g2 = (Graphics2D)bi.getGraphics();     
        g2.setBackground(Color.WHITE);     
        g2.clearRect(0, 0, width, height);     
        g2.setPaint(Color.black);     
             
        FontRenderContext context = g2.getFontRenderContext();     
             
        try {  
            ImageIO.write(bi, "jpeg", file);  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }   
    }
    
    public static void exportImg1_snapshot(String words,String path,int pwitdh){  
        int width = pwitdh;     
        int height = 25;     
        String s = words;     
             
        File file = new File(path);     
             
        Font font = new Font("微软雅黑", Font.BOLD, 14);     
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);     
        Graphics2D g2 = (Graphics2D)bi.getGraphics();     
        g2.setBackground(Color.WHITE);     
        g2.clearRect(0, 0, width, height);     
        g2.setPaint(Color.black);     
        g2.setFont(font);     
        FontRenderContext context = g2.getFontRenderContext();     
        Rectangle2D bounds = font.getStringBounds(s, context);     
        double x = 0;//(width - bounds.getWidth()) / 2;     
        double y = (height - bounds.getHeight()) / 2 ;     
        double ascent = -bounds.getY();     
        double baseY = y + ascent;     
             
        g2.drawString(s, (int)x, (int)baseY);     
             
        try {  
            ImageIO.write(bi, "jpeg", file);  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }   
    }
    
    public static void exportImg2(String username,String headImg){  
        try {  
            //1.jpg是你的 主图片的路径  
            InputStream is = new FileInputStream("d://yzm_ms.jpeg");  
              
              
            //通过JPEG图象流创建JPEG数据流解码器  
            JPEGImageDecoder jpegDecoder = JPEGCodec.createJPEGDecoder(is);  
            //解码当前JPEG数据流，返回BufferedImage对象  
            BufferedImage buffImg = jpegDecoder.decodeAsBufferedImage();  
            //得到画笔对象  
            Graphics g = buffImg.getGraphics();  
              
            //创建你要附加的图象。  
            //小图片的路径  
            ImageIcon imgIcon = new ImageIcon(headImg);   
              
            //得到Image对象。  
            Image img = imgIcon.getImage();  
              
            //将小图片绘到大图片上。  
            //5,300 .表示你的小图片在大图片上的位置。  
            g.drawImage(img,400,15,null);  
              
            //设置颜色。  
            g.setColor(Color.BLACK);  
              
              
            //最后一个参数用来设置字体的大小  
            Font f = new Font("宋体",Font.PLAIN,25);  
            Color mycolor = Color.red;//new Color(0, 0, 255);  
            g.setColor(mycolor);  
            g.setFont(f);  
              
            //10,20 表示这段文字在图片上的位置(x,y) .第一个是你设置的内容。  
            g.drawString(username,100,335);  
              
            g.dispose();  
              
              
            OutputStream os;  
          
            os = new FileOutputStream("d://union.jpg");  
            //String shareFileName = "\\upload\\" + System.currentTimeMillis() + ".jpg";  
            //os = new FileOutputStream(shareFileName);  
             //创键编码器，用于编码内存中的图象数据。            
            JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(os);  
            en.encode(buffImg);           
              
            is.close();  
            os.close();  
        } catch (FileNotFoundException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (ImageFormatException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
          
    }  
    
    /** 
     * * 转换图片 * * 
     */  
    public static void changeImge(File img) {  
        try {  
            Image image = ImageIO.read(img);  
            int srcH = image.getHeight(null);  
            int srcW = image.getWidth(null);  
            BufferedImage bufferedImage = new BufferedImage(srcW, srcH,BufferedImage.TYPE_3BYTE_BGR);  
            bufferedImage.getGraphics().drawImage(image, 0,0, srcW, srcH, null);  
            bufferedImage=new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY),null).filter (bufferedImage,null);   
            FileOutputStream fos = new FileOutputStream(img);  
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);  
            encoder.encode(bufferedImage);  
            fos.close();  
            // System.out.println("转换成功...");  
        } catch (IOException e) {  
            e.printStackTrace();  
            throw new IllegalStateException("图片转换出错！", e);  
        }  
    }  
    
    /** 
     * * 转换图片 * * 
     */  
    public static void changeImge2(File img) {  
        try {  
            //Image image = ImageIO.read(img);  
        	BufferedImage image = ImageIO.read(img);    
            int srcH = image.getHeight(null);  
            int srcW = image.getWidth(null);  
            int[] pixels = new int[srcW * srcH];
            image.getRGB(0, 0, srcW, srcH, pixels, 0, srcW);
            int newPixels[] = new int[srcW * srcH];
            for(int i = 0; i < srcW * srcH; i++) {
                HSLColor hslColor = new HSLColor(HSLColor.fromRGB(new Color(pixels[i])));
                newPixels[i] = hslColor.adjustSaturation(0).getRGB();
            }
            BufferedImage bi = new BufferedImage(srcW, srcH, BufferedImage.TYPE_INT_RGB);
            bi.setRGB(0, 0, srcW, srcH, newPixels, 0, srcW);
//            BufferedImage bufferedImage = new BufferedImage(srcW, srcH,BufferedImage.TYPE_3BYTE_BGR);  
//            bufferedImage.getGraphics().drawImage(image, 0,0, srcW, srcH, null);  
//            bufferedImage=new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY),null).filter (bufferedImage,null);   
            FileOutputStream fos = new FileOutputStream(img);  
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);  
            encoder.encode(bi);  
            fos.close();  
            // System.out.println("转换成功...");  
        } catch (IOException e) {  
            e.printStackTrace();  
            throw new IllegalStateException("图片转换出错！", e);  
        }  
    }
    
    
    public static void binaryImage(File file) throws IOException{  
        BufferedImage image = ImageIO.read(file);  
        int width = image.getWidth();  
        int height = image.getHeight();  
          
        BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);//重点，技巧在这个参数BufferedImage.TYPE_BYTE_BINARY  
        for(int i= 0 ; i < width ; i++){  
            for(int j = 0 ; j < height; j++){  
            int rgb = image.getRGB(i, j);  
            grayImage.setRGB(i, j, rgb);  
            }  
        }  
          
        ImageIO.write(grayImage, "jpg", file);  
    }
}
