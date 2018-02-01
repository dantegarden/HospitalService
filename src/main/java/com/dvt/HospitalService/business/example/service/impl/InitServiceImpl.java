package com.dvt.HospitalService.business.example.service.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Lists;
import com.dvt.HospitalService.business.example.dto.Fpxx;
import com.dvt.HospitalService.business.example.dto.PassResult;
import com.dvt.HospitalService.business.example.dto.SaxResult;
import com.dvt.HospitalService.business.example.service.InitService;
import com.dvt.HospitalService.business.example.service.NewSaxService;
import com.dvt.HospitalService.commons.utils.CommonHelper;
import com.dvt.HospitalService.commons.utils.HttpHelper;
import com.dvt.HospitalService.commons.utils.JsonUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;
@Service
public class InitServiceImpl implements InitService {
	
	private static final Logger logger = LoggerFactory.getLogger(InitServiceImpl.class);
	
	@Autowired
	private NewSaxService newSaxService;
	
	private final static String EXCHANGE_NAME;
	private final static String QUEUE_NAME;
	private final static String CONSUMER_NUM;
	private final static String MQ_HOST;
	private final static String MQ_PORT;
	private final static String MQ_MANAGER_NAME;
	private final static String MQ_MANAGER_PWD;
	private final static String MQ_VHOST;
	private final static String SYS_HOSTNAME;
	private final static String ODOO_INTERFACE;
	private final static String WAIT_TIME;
	private final static List<ExecutorService> pools;
	private final static List<Boolean> poolInterupteds;
	static{
		Properties prop = new Properties();
		try {
			prop.load(InitServiceImpl.class.getClassLoader().getResourceAsStream("deploy.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		EXCHANGE_NAME = prop.getProperty("rmq.exchange.name");
		QUEUE_NAME = prop.getProperty("rmq.queue.name");
		CONSUMER_NUM = prop.getProperty("rmq.consumer.num");
		MQ_HOST = prop.getProperty("rmq.host");
		MQ_PORT = prop.getProperty("rmq.port");
		MQ_MANAGER_NAME = prop.getProperty("rmq.manager.user");
		MQ_MANAGER_PWD = prop.getProperty("rmq.manager.password");
		MQ_VHOST = prop.getProperty("rmq.vhost");
		ODOO_INTERFACE = prop.getProperty("odoo.interface");
		SYS_HOSTNAME = getHostNameForLiunx();
		WAIT_TIME = prop.getProperty("wait.time");
		pools = Lists.newArrayList();
		for (int i = 0; i < Integer.valueOf(CONSUMER_NUM); i++) {
			pools.add(Executors.newSingleThreadExecutor());
		}
		poolInterupteds = Lists.newArrayList();
		for (int i = 0; i < Integer.valueOf(CONSUMER_NUM); i++) {
			poolInterupteds.add(false);
		}
		//Executors.newFixedThreadPool(Integer.valueOf(CONSUMER_NUM));
	}
	
	public static ExecutorService newFixedThreadPool(int nThreads) {
		ThreadPoolExecutor pool = new ThreadPoolExecutor(nThreads, nThreads, 300L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(Integer.valueOf(CONSUMER_NUM)));
		pool.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
		return pool;
	}
	
	@Override
	public void init() {
		Integer num = 1;
		try {
			num = Integer.valueOf(this.CONSUMER_NUM);
		} catch (Exception e) {
			logger.info("默认启动1个消费者");
		}
		
		try {
			for (int i = 1; i <= num; i++) {
				logger.info("消费者"+i+"开始监听队列"+this.QUEUE_NAME);
				this.listenQueue(i);
			}
			logger.info("已启动所有消费者");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void listenQueue(Integer index) throws IOException{
		// 创建连接工厂  
        ConnectionFactory factory = new ConnectionFactory();  
        //设置RabbitMQ地址  
        factory.setHost(this.MQ_HOST);
        factory.setPort(Integer.valueOf(this.MQ_PORT));
        //创建一个新的连接  
        Connection connection = factory.newConnection();  
        //创建一个频道  
        Channel channel = connection.createChannel();
        
        boolean durable = true;//打开持久化
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", durable);  
        Map<String, Object> arg=new HashMap<String, Object>();  
        arg.put("x-max-priority", 10);
        
        channel.queueDeclare(QUEUE_NAME, durable, false, false, arg);  
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");  
        logger.info(SYS_HOSTNAME + " Worker ["+index+"] Waiting for messages in Queue ["+ this.QUEUE_NAME +"].");
        //每次从队列中获取数量
        channel.basicQos(1);
        //DefaultConsumer类实现了Consumer接口，通过传入一个频道，告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery 
        Consumer consumer = new DefaultConsumer(channel){
        	
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope,
					BasicProperties properties, byte[] body) throws IOException {
				//super.handleDelivery(consumerTag, envelope, properties, body);
				String message = new String(body, "UTF-8"); 
				logger.info(SYS_HOSTNAME + " Worker ["+index+"] said : Received '" + message + "' Priority("+properties.getPriority()+") at "+ CommonHelper.getNowStr(CommonHelper.DF_DATE_TIME));
				try {
					//testWork(message);
					//开始处理信息，启动爬虫
					consume(message, index);
					
				} catch (Exception e) {
					logger.error(SYS_HOSTNAME + " Worker ["+index+"] said : Error When Do Message ["+message+"] ");
					//TODO 调ODOO接口返回错误
					String fpid = JsonUtils.jsonToJavaBean(message, Fpxx.class).getId();
					String backJson = JsonUtils.JavaBeanToJson(new SaxResult(fpid, "2", "爬虫执行报错:"+e.getMessage(), null));
					invokeOdooInPost(backJson, index, message);
					e.printStackTrace();
				} finally {
					logger.info(SYS_HOSTNAME + " Worker ["+index+"] said : Finished Message ["+message+"] ");
					// 消息处理完成确认
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
        };
        // 消息消费完成确认
        channel.basicConsume(QUEUE_NAME, false, consumer); 
	}
	
	private void consume(String message, Integer index) throws Exception {
		System.out.println("LOG-init: Worker["+ index +"]开始消费");
		Thread mainThread = Thread.currentThread();//主线程

		NewSaxServiceImpl saxService = new NewSaxServiceImpl();
		saxService.setWorkerIndex(String.valueOf(index));
		saxService.setEwmJson(message);
//		Thread saxThread = new Thread(saxService); //子线程
//		saxThread.start();
		System.out.println("LOG-init: Worker["+ index +"]提交任务");
		
		ExecutorService pool = pools.get(index-1);
		if(poolInterupteds.get(index-1)){
			pools.set(index-1, Executors.newSingleThreadExecutor());
			pool = pools.get(index-1);
			
			poolInterupteds.set(index-1, false);
		}
		
		Future<String> future = pool.submit(saxService);
		try {
			System.out.println("LOG-init: Worker["+ index +"]获取线程，执行任务");
			String backJson = future.get(Integer.valueOf(InitServiceImpl.WAIT_TIME), TimeUnit.SECONDS);  
			invokeOdooInPost(backJson, index, message);
        } catch (InterruptedException e) {
        	System.out.println("LOG-init: Worker["+ index +"] InterruptedException 超时"+InitServiceImpl.WAIT_TIME+"秒");
        	//killThread(future, saxService, index);
        	throw new TimeoutException("获取超时"+InitServiceImpl.WAIT_TIME+"秒");
        } catch (ExecutionException e) {
        	System.out.println("LOG-init: Worker["+ index +"] InterruptedException 超时"+InitServiceImpl.WAIT_TIME+"秒");
        	//killThread(future, saxService, index);
        	throw new TimeoutException("获取超时"+InitServiceImpl.WAIT_TIME+"秒");
        } catch (TimeoutException e) {  
        	System.out.println("LOG-init: Worker["+ index +"] InterruptedException 超时"+InitServiceImpl.WAIT_TIME+"秒");
        	//killThread(future, saxService, index);
        	throw new TimeoutException("获取超时"+InitServiceImpl.WAIT_TIME+"秒");
        } finally{
        	if(!future.isDone() && !future.isCancelled()){
        		killThread(future, saxService, index);//强制结束线程
        	}
        	System.out.println("LOG-init: Worker["+ index +"]结束消费");
        }
		
//		while(!future.isDone()&&!future.isCancelled()){
//			mainThread.sleep(1000);
//		}
		
//		SaxResult sr = this.newSaxService.checkFpEffect(message);
//		String backJson = JsonUtils.JavaBeanToJson(sr);
		//TODO 调odoo接口返回数据
		//invokeOdooInPost(backJson, index, message);
	}
	
	@SuppressWarnings("deprecation")
	private void killThread(Future future, NewSaxService newSaxService, Integer index) throws TimeoutException{
		future.cancel(true); 
    	Field runner;
		try {
			runner = future.getClass().getDeclaredField("runner");
			runner.setAccessible(true);  
	        Thread execThread = (Thread) runner.get(future);  
	        
	        System.out.println("LOG-init: Worker["+ index +"]强制杀死线程["+execThread.getId()+"]");
	        
	        pools.get(index-1).shutdownNow();

	        //强杀火狐
	        WebDriver firefoxDriver = newSaxService.getFireFoxDriver();
	        if(firefoxDriver!=null){
	        	firefoxDriver.quit();
	        	System.out.println("LOG-init: Worker["+ index +"]关闭火狐浏览器");
	        }
	        //execThread.stop();  
	        //execThread = null;  
	        
	        // 为了防止句柄泄漏，这里催促jvm进行gc回收，那是因为进行gc回收时，可以收回被stop的线程所占用的句柄  
	        //System.gc();  
	        
		} catch (Exception e) {
			e.printStackTrace();
			throw new TimeoutException("关闭线程失败");
		} finally{
			poolInterupteds.set(index-1, true);
			// 为了防止句柄泄漏，这里催促jvm进行gc回收，那是因为进行gc回收时，可以收回被stop的线程所占用的句柄
			System.gc();
		}
	}
	
	private void invokeOdooInPost(String backJson, Integer index, String message) throws IOException{
		Map<String,String> params = new ImmutableMap.Builder<String,String>()
				.put("result", backJson)
				.build();
		logger.info(SYS_HOSTNAME + " Worker ["+index+"] said : Pass Message To Odoo ["+message+"] ");
		String passJson = HttpHelper.startPost(ODOO_INTERFACE, params);
		PassResult pr = JsonUtils.jsonToJavaBean(passJson, PassResult.class);
		if(pr.getState().equals("0")){
			logger.info(SYS_HOSTNAME + " Worker ["+index+"] said : Pass Message Over ["+message+"] ");
		}
	}
	
	private static void testWork(String task) {
        try {
            Thread.sleep(5000); // 暂停5秒钟
        } catch (InterruptedException _ignored) {
            Thread.currentThread().interrupt();
        }
    }
	
	public static String getHostNameForLiunx() {  
        try {  
            return (InetAddress.getLocalHost()).getHostName();  
        } catch (UnknownHostException uhe) {  
            String host = uhe.getMessage(); // host = "hostname: hostname"  
            if (host != null) {  
                int colon = host.indexOf(':');  
                if (colon > 0) {  
                    return host.substring(0, colon);  
                }  
            }  
            return "UnknownHost";  
        }  
    }  
}
