package com.focaplo.myfuse.service;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hornetq.api.jms.HornetQJMSClient;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.focaplo.myfuse.service.impl.IncomingCommandsListener;

public class IncomingCommandsListenerTest{
	Logger log = LogManager.getLogger(this.getClass());
	ClassPathXmlApplicationContext extendedBeans;
	@Before
	public void loadQueueSettings(){
		//extendedBeans = new XmlBeanFactory(new ClassPathResource("applicationContext-messaging-incoming.xml.optional"));
		extendedBeans = new ClassPathXmlApplicationContext("applicationContext-messaging-incoming.xml.optional");
		IncomingCommandsListener listener = (IncomingCommandsListener) extendedBeans.getBean("incomingCommandsListener");
		log.info(listener);
	}
	@Test
	public void test(){
		//need to load the optional beans by setting the bime.property bime.local.queue.listener=true
	
		Thread.yield();
		
		log.info("done");
	}
	
	@Test
	public void testReadMessage(){
		Connection connection = null;
	
		try{
		ConnectionFactory connectionFactory = (ConnectionFactory) extendedBeans.getBean("localCachingConnectionFactory");
		
		Queue queue = HornetQJMSClient.createQueue("queue.incoming.command");
		connection  = connectionFactory.createConnection();
			 // Step 5. Create a JMS Session
	        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		// Step 9. Create a JMS Message Consumer
        MessageConsumer messageConsumer = session.createConsumer(queue);

        // Step 10. Start the Connection
        connection.start();

        // Step 11. Receive the message
        TextMessage messageReceived = (TextMessage)messageConsumer.receive(5000);
        
        System.out.println("Received message: " + (messageReceived==null?"null":messageReceived.getText()));
		}catch(Exception e){
			log.error("error",e);	
		}finally{

           if (connection != null)
           {
              try {
				connection.close();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           }
        }

	}
}
