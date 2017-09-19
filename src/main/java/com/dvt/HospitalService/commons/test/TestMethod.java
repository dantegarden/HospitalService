package com.dvt.HospitalService.commons.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.junit.Test;

import com.dvt.HospitalService.commons.utils.XmlRpcUtils;
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
	
	@Test
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
	
	
}
