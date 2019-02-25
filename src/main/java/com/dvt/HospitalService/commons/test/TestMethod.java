package com.dvt.HospitalService.commons.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.junit.Test;

import com.aliyun.oss.OSSClient;
import com.dvt.HospitalService.business.example.service.impl.InitServiceImpl;
import com.dvt.HospitalService.business.example.service.impl.NewSaxServiceImpl;
import com.dvt.HospitalService.commons.utils.HttpHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TestMethod {
	//@Test
	public void test(){
		Connection c = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:test.db");
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Opened database successfully");
	}
	
	//@Test
	public void test2() throws Exception{
		final String URL = "http://localhost:8069"; 
	    //final String DB = "hospital-mh";  
	    //final int USERID = 1;  
	    //final String PASS = "123456";  
	    List<String> emptyList = Lists.newArrayList();
		
	    XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();  
		XmlRpcClient client = new XmlRpcClient();
		
	    config.setServerURL(new URL(String.format("%s/xmlrpc/2/common", URL)));  
	    client.setConfig(config);
		Object obj = client.execute(config, "authenticate", Arrays.asList(
        "hospital-mh", "admin", "123456", Maps.newHashMap()));
		 
        
        if(obj != null){
        	System.out.println(obj.toString());
        }
	}
	
//	@Test
//	public void testmethod() throws IOException{
//		Map<String,String> params = Maps.newHashMap();
//		params.put("result", "{xxxx:'12312312'}");
//		String reply = HttpHelper.startPost("http://localhost:8069/messageTest", params);
//		System.out.println(reply);
//	}
	private final static ExecutorService pool;
	private final static String WAIT_TIME = "1200";
	static{
		pool = Executors.newFixedThreadPool(Integer.valueOf("2"));
	}
	
	@Test
	public void test1() throws Exception{
		for (int i = 0; i < 3; i++) {
			try {
				testmethod(i);
			} catch (Exception e) {
				System.out.println(i+"超时了");
				e.printStackTrace();
			}
		}
	}
	
	
	public void testmethod(int i) throws Exception{
		
		Thread mainThread = Thread.currentThread();//主线程
		
		System.out.println("我是消费线程"+i);
		NewSaxServiceImpl saxService = new NewSaxServiceImpl();
		
		String ewmJson = "{\"kjje\": \"803775\", \"fphm\": \"11198956\", \"kprq\": \"20180411\", \"fpdm\": \"1100174320\"}";
		saxService.setEwmJson(ewmJson);
		Future<String> future = pool.submit(saxService);
		try {  
			String backJson = future.get(Integer.valueOf(WAIT_TIME), TimeUnit.SECONDS);  
			//invokeOdooInPost(backJson, index, message);
        } catch (InterruptedException e) {
        	System.out.println("InterruptedException 超时10秒");
        	future.cancel(true);
        	throw new TimeoutException("获取超时"+WAIT_TIME+"秒");
        } catch (ExecutionException e) {
        	System.out.println("ExecutionException 超时10秒");
        	future.cancel(true);
        	throw new TimeoutException("获取超时"+WAIT_TIME+"秒");
        } catch (TimeoutException e) {  
        	System.out.println("TimeoutException 超时10秒");
        	future.cancel(true); 
        	throw new TimeoutException("获取超时"+WAIT_TIME+"秒");
        }
		while(!future.isDone()&&!future.isCancelled()){
			mainThread.sleep(1000);
		}
		System.out.println("1111");   
	}
}
