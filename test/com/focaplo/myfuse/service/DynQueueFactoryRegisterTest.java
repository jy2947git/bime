package com.focaplo.myfuse.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.focaplo.myfuse.messaging.JmsQueueSender;
import com.focaplo.myfuse.service.SpringCommand;

public class DynQueueFactoryRegisterTest extends BaseManagerTestCase {

	@Test
	public void testRegister(){
		DynQueueFactoryRegister register  = this.context.getBean(DynQueueFactoryRegister.class);
		//add queue connection factory
		register.registerQueueFactoryBean("localhost", new Integer("5445"));
		//retrieve bean
		ConnectionFactory cf  = (ConnectionFactory)this.context.getBean("remoteCachingConnectionFactory");
		Connection conn = null;
		try {
			conn = cf.createConnection();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				conn=null;
			}
		}
		
	}
	
	@Test
	public void testRemove(){
		DynQueueFactoryRegister register  = this.context.getBean(DynQueueFactoryRegister.class);
		register.registerQueueFactoryBean("localhost", new Integer("5445"));
		assertTrue(this.context.containsBean("remoteCachingConnectionFactory"));
		register.removeQueueFactoryBeanIfExists();
		assertFalse(this.context.containsBean("remoteCachingConnectionFactory"));
	}
	@Test
	public void testUpdate(){
		DynQueueFactoryRegister register  = this.context.getBean(DynQueueFactoryRegister.class);
		//add queue connection factory
		register.registerQueueFactoryBean("localhost", new Integer("5445"));
		//update
		try{
			register.updateQueueConnectionFactory("whatever", new Integer("1234"));
			ConnectionFactory cf  = (ConnectionFactory)this.context.getBean("remoteCachingConnectionFactory");
			cf.createConnection();
		}catch(Exception e){
			log.error(e);
			//assertTrue(e.getMessage().indexOf("SEVERE: Failed to create netty connection")>-1);
		}
	}
	
	@Test
	public void testStartIncomingMessageListener(){
		DynQueueFactoryRegister register  = this.context.getBean(DynQueueFactoryRegister.class);
		//register connection factory first
		register.registerQueueFactoryBean("localhost", new Integer("5445"));
		assertTrue(this.context.containsBean("remoteCachingConnectionFactory"));
		//register listener and container
		register.startMessageListener("queue.incoming.command", "incomingCommandsListener", "exec");
		//now send a message
		JmsQueueSender sender = (JmsQueueSender)this.context.getBean("queueSender");
		sender.setConnectionFactory((ConnectionFactory) this.context.getBean("remoteCachingConnectionFactory"));
		SpringCommand sc = new SpringCommand();
		sc.setBeanName("dummyManager");
		sc.setMethodName("doSomething");
		sc.setArguments(new Object[]{"please", "do", "it"});
		ObjectMapper mapper = new ObjectMapper();
		String json;
		try {
			json = mapper.writeValueAsString(sc);
			sender.sendMessage("queue.incoming.command", json);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done");
	}
}
