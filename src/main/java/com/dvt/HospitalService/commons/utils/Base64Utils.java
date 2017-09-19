package com.dvt.HospitalService.commons.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Utils {
	public static String GetImageStr(String filepath) {  
        String imgFile = filepath;
        InputStream in = null;  
        byte[] data = null;  
        try   
        {  
            in = new FileInputStream(imgFile);          
            data = new byte[in.available()];  
            in.read(data);  
            in.close();  
        }   
        catch (IOException e)   
        {  
            e.printStackTrace();  
        }  
        BASE64Encoder encoder = new BASE64Encoder();  
        return encoder.encode(data);
    }  
      
    public static boolean GenerateImage(String imgStr,String filepath) {    
        if (imgStr == null) 
            return false;  
        BASE64Decoder decoder = new BASE64Decoder();  
        try   
        {  
            byte[] b = decoder.decodeBuffer(imgStr);  
            for(int i=0;i<b.length;++i)  
            {  
                if(b[i]<0)  
                {
                    b[i]+=256;  
                }  
            }  
              
            String imgFilePath = filepath;  
            OutputStream out = new FileOutputStream(imgFilePath);      
            out.write(b);  
            out.flush();  
            out.close();  
            return true;  
        }   
        catch (Exception e)   
        {  
            return false;  
        }  
    }  
}
