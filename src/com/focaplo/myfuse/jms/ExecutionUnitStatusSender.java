package com.focaplo.myfuse.jms;

import java.util.Set;

import javax.jms.JMSException;


/**
 * this element collects the execution unit(server instance) status information
 * and send to controller. It is scheduled to run every minute.
 *
 */
public class ExecutionUnitStatusSender extends QueueMessageSender{



	public void sendStatus(String ipAddress, String unitName, String messageId, String status, Set<HostedLab> hostedLabs){
		ExecutionUnitStatusMessage s = new ExecutionUnitStatusMessage();
		s.setIpAddress(ipAddress);
		s.setMessageId(messageId);
		s.setOk(true);
		s.setUnitName(unitName);
		s.getHostedLabs().addAll(hostedLabs);
		try {
			this.sendObject(s);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
