package com.focaplo.myfuse.messaging;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConnectionParameters;

public class AmqpQueueSender {
	protected Log log = LogFactory.getLog(getClass());
	public void sendMessage(String hostName, int portNumber, String userName, String password, String virtualHost, String exchange, String routingKey, String message){
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
	        byte[] messageBodyBytes = message.getBytes();
	        ch.basicPublish(exchange, routingKey, null, messageBodyBytes);
	        
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
	    }
	}
}
