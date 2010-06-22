package com.focaplo.myfuse.messaging;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hornetq.api.jms.HornetQJMSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


/**
 * To send message to JMS queue.
 * 
 *
 */
@Service(value="queueSender")
public class JmsQueueSender {
	protected Log log = LogFactory.getLog(getClass());
	

	ConnectionFactory connectionFactory;

	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
	
	
	public void sendMessage(String queueName, String textMessage) throws JMSException{
		Connection connection = null;
	    if(this.connectionFactory==null){
	    	throw new RuntimeException("JMS Connection factory is NULL! are you sure you have called the setConnectinFactory() method before the send()?");
	    }
	      try
	      {

		Queue queue = HornetQJMSClient.createQueue(queueName);
	 connection  = this.connectionFactory.createConnection();
		 // Step 5. Create a JMS Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Step 6. Create a JMS Message Producer
        MessageProducer producer = session.createProducer(queue);

        // Step 7. Create a Text Message
        TextMessage message = session.createTextMessage(textMessage);

        

        // Step 8. Send the Message
        producer.send(message);
        log.debug("Sent message: " + message.getText());
	 }
    finally
    {

       if (connection != null)
       {
          connection.close();
       }
    }
	}
}
