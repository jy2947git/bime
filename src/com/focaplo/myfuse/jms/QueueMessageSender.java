package com.focaplo.myfuse.jms;

import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hornetq.api.core.TransportConfiguration;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class QueueMessageSender {
	protected final Log log = LogFactory.getLog(getClass());
	@Autowired
	ConnectionFactory jmsConnectionFactory;
	
	Queue queue;
	String hostName;
	String queueName;
	
//	public QueueMessageSender(String hostName, String queueName) {
//		super();
//		this.hostName = hostName;
//		this.queueName = queueName;
//	}
//	public void init() throws NamingException, JMSException{
//		Hashtable params = new Hashtable();
//		params.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
//		params.put("java.naming.provider.url", "jnp://" + hostName+ ":1099");
//		params.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
//		InitialContext ic = new InitialContext(params);
//		cf = (ConnectionFactory)ic.lookup("/ConnectionFactory");
////		TransportConfiguration transportConfiguration = 
////            new TransportConfiguration(NettyConnectorFactory.class.getName()); 
//		orderQueue = (Queue)ic.lookup("/queue/" + queueName);
//		log.info("Inited to send message to queue " + orderQueue.getQueueName());
//	}
	public void sendText(final String m) throws JMSException {
		Connection connection = this.jmsConnectionFactory.createConnection();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageProducer producer = session.createProducer(queue);
		connection.start();
		TextMessage message = session.createTextMessage(m);
		producer.send(message);
		session.close();
		connection.close();
    }

	public void sendObject(final Message m) throws JMSException {
		Connection connection = this.jmsConnectionFactory.createConnection();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageProducer producer = session.createProducer(queue);
		connection.start();
		ObjectMessage message = session.createObjectMessage(m);
		producer.send(message);
		session.close();
		connection.close();
    }

	public ConnectionFactory getJmsConnectionFactory() {
		return jmsConnectionFactory;
	}


	public void setJmsConnectionFactory(ConnectionFactory jmsConnectionFactory) {
		this.jmsConnectionFactory = jmsConnectionFactory;
	}


	public Queue getQueue() {
		return queue;
	}


	public void setQueue(Queue queue) {
		this.queue = queue;
	}


	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}


}
