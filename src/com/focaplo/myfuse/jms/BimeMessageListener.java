package com.focaplo.myfuse.jms;

import javax.jms.JMSException;

import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;

public abstract class BimeMessageListener implements MessageListener {
	protected final Log log = LogFactory.getLog(getClass());
//	Class<Message> classOfMessageObject;
	abstract void handleObjectMessage(Message m);
	abstract void handleTextMessage(String m);
//	abstract void setClassOfMessageObject();
	public void onMessage(javax.jms.Message message) {
		if (message instanceof TextMessage) {
            try {
            	String messageBody = ((TextMessage) message).getText();
//            	//convert text to java object
//            	Gson gson = new Gson();
//        		
//        		Message s = gson.fromJson(messageBody, classOfMessageObject);
//                this.handleMessage(s);
            	this.handleTextMessage(messageBody);
            }
            catch (JMSException ex) {
                throw new RuntimeException(ex);
            }
        }
        else if(message instanceof ObjectMessage){
        	try {
        		this.handleObjectMessage((Message) ((ObjectMessage)message).getObject());
        	 }
            catch (JMSException ex) {
                throw new RuntimeException(ex);
            }
        }else{
            throw new IllegalArgumentException("Message must be of type TextMessage");
        }

	}

}
