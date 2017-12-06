package com.dvt.HospitalService.business.example.webservice.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dvt.HospitalService.business.example.dto.PriorityMsg;
import com.dvt.HospitalService.business.example.webservice.PublishService;
import com.dvt.HospitalService.commons.utils.JsonUtils;
import com.google.common.collect.ImmutableList;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AMQP.BasicProperties.Builder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class PublishServiceImpl implements PublishService{
	private static final Logger logger = LoggerFactory.getLogger(PublishServiceImpl.class);
	
	private final static String EXCHANGE_NAME;
	private final static String QUEUE_NAME;
	private final static String CONSUMER_NUM;
	private final static String MQ_HOST;
	private final static String MQ_PORT;
	private final static String MQ_MANAGER_NAME;
	private final static String MQ_MANAGER_PWD;
	private final static String MQ_VHOST;
	
	static{
		Properties prop = new Properties();
		try {
			prop.load(PublishServiceImpl.class.getClassLoader().getResourceAsStream("deploy.properties"));
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
	}
	
	@Test
	public void test(){
		List<PriorityMsg> aa = ImmutableList.of(
				new PriorityMsg("{\"kjje\": \"52608.89\", \"fphm\": \"03716062\", \"kprq\": \"20170719\", \"fpdm\": \"3100164130\"}", 8),
				new PriorityMsg("{\"kjje\": \"52608.89\", \"fphm\": \"03716062\", \"kprq\": \"20170719\", \"fpdm\": \"3100164130\"}", 8),
				new PriorityMsg("{\"kjje\": \"52608.89\", \"fphm\": \"03716062\", \"kprq\": \"20170719\", \"fpdm\": \"3100164130\"}", 8),
				new PriorityMsg("{\"kjje\": \"52608.89\", \"fphm\": \"03716062\", \"kprq\": \"20170719\", \"fpdm\": \"3100164130\"}", 8),
				new PriorityMsg("{\"kjje\": \"52608.89\", \"fphm\": \"03716062\", \"kprq\": \"20170719\", \"fpdm\": \"3100164130\"}", 8),
				new PriorityMsg("{\"kjje\": \"52608.89\", \"fphm\": \"03716062\", \"kprq\": \"20170719\", \"fpdm\": \"3100164130\"}", 8),
				new PriorityMsg("{\"kjje\": \"52608.89\", \"fphm\": \"03716062\", \"kprq\": \"20170719\", \"fpdm\": \"3100164130\"}", 8),
				new PriorityMsg("{\"kjje\": \"52608.89\", \"fphm\": \"03716062\", \"kprq\": \"20170719\", \"fpdm\": \"3100164130\"}", 8),
				new PriorityMsg("{\"kjje\": \"52608.89\", \"fphm\": \"03716062\", \"kprq\": \"20170719\", \"fpdm\": \"3100164130\"}", 8)
				);
		 String mm = publish2MQ(JsonUtils.JavaBeanToJson(aa));
		 System.out.println(mm);
	}
	
	@Override
	public String publish2MQ(String ewmJson) {
		
		List<PriorityMsg> fpxxList = JsonUtils.jsonToList(ewmJson, PriorityMsg.class);
		
		// 创建连接工厂  
        ConnectionFactory factory = new ConnectionFactory();  
        //设置RabbitMQ地址  
        factory.setHost(MQ_HOST);
        factory.setPort(Integer.valueOf(MQ_PORT));
        
        Connection connection = null;
        Channel channel = null;
        try {
        	//创建一个新的连接  
            connection = factory.newConnection();  
            //创建一个频道  
            channel = connection.createChannel();  
            //创建一个交换器
            boolean durable = true; //队列持久化开关
            channel.exchangeDeclare(EXCHANGE_NAME, "direct",durable);  
            
            Map<String,Object> arg = new HashMap<String, Object>();
            arg.put("x-max-priority",10);//队列优先级只能声明一次，不可改变(涉及到硬盘、内存存储方式) 
            
            //声明一个队列 -- 在RabbitMQ中，队列声明是幂等性的（一个幂等操作的特点是其任意多次执行所产生的影响均与一次执行的影响相同），也就是说，如果不存在，就创建，如果存在，不会对已经存在的队列产生任何影响。   
            channel.queueDeclare(QUEUE_NAME, durable, false, false, arg);  
            
            // 为转发器指定队列，设置binding  
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");  
            
            for (PriorityMsg priorityMsg : fpxxList) {
            	Builder properties=new BasicProperties.Builder();  
            	properties.priority(priorityMsg.getPriority());
            	//发送消息到队列中  
            	channel.basicPublish(EXCHANGE_NAME, "",   properties.build(), priorityMsg.getMsg().getBytes());
            	System.out.println("Producer [x] Sent '" + priorityMsg.getMsg() + "' priority("+ priorityMsg.getPriority() +")");  
			}
            return "{\"state\":0}";
            
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
			//关闭频道和连接  
			try {
				if(channel!=null){
					channel.close();
				}
				if(connection!=null){
					connection.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}  
		}
        return "{\"state\":1}";  
        
	}

}
