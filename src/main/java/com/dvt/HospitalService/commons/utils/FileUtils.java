package com.dvt.HospitalService.commons.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Acore
 *
 */
public class FileUtils extends org.apache.commons.io.FileUtils{
	/**
     * 是否存在
     * @param file
     * @return
     */
    public static boolean isExists(File file){
        return file!=null&&file.exists();
    }
	 /**
     * 输出文件
     * @param file
     * @throws IOException 
     */
    public static void outputFile(File file,OutputStream stream) throws IOException{
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            bos = new BufferedOutputStream(stream);
            while ((bytesRead = bis.read(buffer, 0, 8192)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            bos.flush();
        } finally {
            bis.close();
            bos.close();
        }
    }
	/**
     * 
     * @param file
     * @param fileName
     * @throws IOException
     */
    public static  void downloadFile(File file,String fileName,HttpServletRequest request,HttpServletResponse response) throws IOException{
        // 弹出下载对话框(以附件形式打开文件流)
        String agent = (String) request.getHeader("USER-AGENT");
        if (agent != null && agent.toUpperCase().indexOf("FIREFOX") >-1) {
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=" +  
                    new String(fileName.getBytes("utf-8"),"ISO8859_1"));
        } else {
            response.setHeader( 
                    "Content-Disposition", 
                    "attachment; filename=" +
                            toUtf8String(fileName));
        }
 
        OutputStream stream=response.getOutputStream();
        outputFile(file, stream);
    }
    /**
     * @param request
     * @param response
     * @param fileName
     * @return
     * @throws IOException
     */
    public static OutputStream initDownload(HttpServletRequest request,HttpServletResponse response,String fileName) throws IOException{
    	 // 弹出下载对话框(以附件形式打开文件流)
        String agent = (String) request.getHeader("USER-AGENT");
        if (agent != null && agent.toUpperCase().indexOf("FIREFOX") >-1) {
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=" +  
                    new String(fileName.getBytes("utf-8"),"ISO8859_1"));
        } else {
            response.setHeader( 
                    "Content-Disposition", 
                    "attachment; filename=" +
                            toUtf8String(fileName));
        }
        response.setCharacterEncoding("UTF-8");
        return response.getOutputStream();
    }
    /**
     * 转码
     * @param s
     * @return
     */
    private static String toUtf8String(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<s.length();i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = Character.toString(c).getBytes("utf-8");
                } catch (Exception ex) {
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0) k += 256;
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
 
    }
	/**
     * 关闭资源
     * @param is
     * @param os
     */
    public static void close(InputStream is,OutputStream os){
        if(is!=null){
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        if(os!=null){
            try {
                os.close();
            } catch (IOException e) {
            }
        }
    }
}
