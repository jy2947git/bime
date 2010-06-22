package com.focaplo.myfuse.messaging;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionParameters;
import com.rabbitmq.client.QueueingConsumer;

public class AmqpQueueListener {
	protected Log log = LogFactory.getLog(getClass());
	void start(String hostName, int portNumber, String userName, String password, String virtualHost, String exchange, String queueName, String routingKey, SimpleMessageHandler callback){
       
        Connection conn = null;
        Channel ch = null;
        try{
        	ConnectionParameters params = new ConnectionParameters();
        	params.setUsername(userName);
        	params.setPassword(password);
        	params.setVirtualHost(virtualHost);
        	params.setRequestedHeartbeat(0);
        	ConnectionFactory factory = new ConnectionFactory(params);
        	//TODO? only one connection?
        	conn = factory.newConnection(hostName, portNumber);

	
	        ch = conn.createChannel();
	
	        
	        ch.exchangeDeclare(exchange, "direct");
	        ch.queueDeclare(queueName);
	        ch.queueBind(queueName, exchange, routingKey);
	        
	        QueueingConsumer consumer = new QueueingConsumer(ch);
	        ch.basicConsume(queueName, consumer);
	        while (true) {
	            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
	            String msg = new String(delivery.getBody());
	            log.info("Message: " + msg);
	            //handle the message
	            callback.handle(msg);
	            ch.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
	        }
	    } catch (Exception ex) {
	        System.err.println("Main thread caught exception: " + ex);
	        ex.printStackTrace();
	        System.exit(1);
	    }finally{
	    	if(conn!=null&&conn.isOpen()){
	    		try {
					conn.close();
				} catch (IOException e) {
					log.error(e);
				}
	    		
	    	}
	    	conn=null;
	    	if(ch!=null&&ch.isOpen()){
	    		try {
					ch.close();
				} catch (IOException e) {
					log.error(e);
				}
	    		
	    	}
	    	ch=null;
	    	log.info("I am totally done");
	    }
	}
}
