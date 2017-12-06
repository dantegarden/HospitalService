package com.dvt.HospitalService.commons.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.json.JSONObject;



public class HttpHelper {
	
	/**
	 * 通用POST方法
	 * @param url 		请求URL
	 * @param param 	请求参数，如：username=test&password=1
	 * @return			response
	 * @throws IOException
	 */
	public static String startPost(String url, Map<String,String> params)
			throws IOException {
		OutputStreamWriter out = null;
        BufferedReader in = null;        
        HttpURLConnection conn = null;
        String finalResult = null;
        StringBuilder result = new StringBuilder(); 
        try {
            URL _url = new URL(url);
            conn = (HttpURLConnection)_url.openConnection();
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            // 发送POST请求必须设置为true
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //设置连接超时时间和读取超时时间
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(60000);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();
            
            
            
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");  
            // POST的请求参数写在正文中
            if(params!=null&&!params.isEmpty()){
            	JSONObject obj = new JSONObject();
            	for(String paramkey:params.keySet()){
            		 out.write(paramkey+"="+params.get(paramkey));
            	}
            	//out.write(obj.toString());
            }
            out.flush();  
            out.close();
            // 取得输入流，并使用Reader读取  
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            finalResult = result.toString();
        } catch (Exception e) {
        	System.out.println("Error:post方法失败，接口未能调通");
            e.printStackTrace();
            //return HttpHelper.startPost(url, params);
        }
        //关闭输出流、输入流
        finally{
            try{
            	if(in!=null){
            		in.close();
            	}
                if(out!=null){
                    out.close();
                }
                if(conn!=null){
                    conn.disconnect();
                }
                
                if(finalResult!=null){
                	return finalResult;
                }else{
                	Thread.sleep(60000);
                	return HttpHelper.startPost(url, params);
                }
                
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
		return finalResult;
	}
	
	
	/**
	 * 通用Get方法
	 * @param url 		请求URL
	 * @param param 	请求参数，如：username=test&password=1
	 * @return			response
	 * @throws IOException
	 */
	public static String startGet(String url, Map<String,String> params)
			throws IOException {
		HttpURLConnection conn = null;
		BufferedReader in = null;        
        StringBuilder result = new StringBuilder(); 
        String finalResult = null;
        try {
            //GET请求直接在链接后面拼上请求参数
        	if(params!=null&&!params.isEmpty()){
        		url += "?";
        		for (String paramKey : params.keySet()) {
					url += paramKey + "=" + params.get(paramKey) + "&";
				}
        	}
            
            URL _url = new URL(url);
            conn = (HttpURLConnection)_url.openConnection();
            conn.setRequestMethod("GET");
            //Get请求不需要DoOutPut
            conn.setDoOutput(false);
            conn.setDoInput(true);
            //设置连接超时时间和读取超时时间
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("connection", "keep-alive");
            
            conn.connect();  
            
            Map<String, List<String>> map = conn.getHeaderFields();
            
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            
            //连接服务器  
            // 取得输入流，并使用Reader读取  
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            finalResult = result.toString();
        } catch (Exception e) {
            System.out.println("Error:方法失败，接口未能调通");
        	e.printStackTrace();
            //return HttpHelper.startGet(url, params);
        }
        //关闭输入流
        finally{
            try{
                if(in!=null){
            		in.close();
            	}
                
                if(conn!=null){
                    conn.disconnect();
                }
                
                if(finalResult!=null){
                	return finalResult;
                }else{
                	return HttpHelper.startGet(url, params);
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
		return finalResult;
	}
	
	public static String getAuth() {
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        // 官网获取的 API Key 更新为你注册的
        String clientId = "MNZaWLrXH0PiG4h4GyymzwSu";
        // 官网获取的 Secret Key 更新为你注册的
        String clientSecret = "KaTGGGSGh8sGirNTsaPb16dntx6dCt9X";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + clientId
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + clientSecret;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /**
             * 返回结果示例
             */
            System.out.println("result:" + result);
            JSONObject jsonObject = new JSONObject(result);
            String access_token = jsonObject.getString("access_token");
            return access_token;
        } catch (Exception e) {
            System.out.printf("获取token失败！");
            e.printStackTrace();
        }
        return null;
    }
}
