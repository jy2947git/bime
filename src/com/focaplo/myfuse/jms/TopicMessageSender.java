package com.focaplo.myfuse.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class TopicMessageSender {
	protected final Log log = LogFactory.getLog(getClass());
	@Autowired
	ConnectionFactory jmsConnectionFactory;
	Topic topic;
	
	public void sendText(final String m) throws JMSException {
		Connection connection = this.jmsConnectionFactory.createConnection();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageProducer producer = session.createProducer(topic);
		connection.start();
		TextMessage message = session.createTextMessage(m);
		producer.send(message);
		session.close();
		connection.close();
    }

	public void sendObject(final Message m) throws JMSException {
		Connection connection = this.jmsConnectionFactory.createConnection();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageProducer producer = session.createProducer(topic);
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

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

}
