package com.focaplo.myfuse.service;

import java.io.IOException;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import com.focaplo.myfuse.messaging.JmsQueueSender;

public class JmsQueueSenderTest extends BaseManagerTestCase{


	JmsQueueSender sender;
	@Before
	public void loadQueueSettings(){
		sender = (JmsQueueSender)context.getBean("queueSender");
		DynQueueFactoryRegister register  = this.context.getBean(DynQueueFactoryRegister.class);
		//add queue connection factory
		register.registerQueueFactoryBean("localhost", new Integer("5445"));
		ConnectionFactory cf  = (ConnectionFactory)this.context.getBean("remoteCachingConnectionFactory");

		sender.setConnectionFactory(cf);
	}
	
	
	@Test
	public void sendSpringCommand() throws JMSException, JsonGenerationException, JsonMappingException, IOException{
		SpringCommand sc = new SpringCommand();
		sc.setBeanName("dummyManager");
		sc.setMethodName("doSomething");
		sc.setArguments(new Object[]{"please", "do", "it"});
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(sc);
		sender.sendMessage("queue.incoming.command", json);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
