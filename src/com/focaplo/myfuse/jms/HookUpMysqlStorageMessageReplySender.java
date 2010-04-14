package com.focaplo.myfuse.jms;

import javax.jms.JMSException;
import javax.jms.Message;

public class HookUpMysqlStorageMessageReplySender extends QueueMessageSender {

	public void sendReplyToHookUpMysql(String sourceMessageId, String ipAddress, String unitName, String labName){
		HookUpMysqlStorageMessageReply m = new HookUpMysqlStorageMessageReply();
		m.setIpAddress(ipAddress);
		m.setMessageId(sourceMessageId);
		m.setOk(true);
		m.setUnitName(unitName);
		m.setLabName(labName);
		try {
			this.sendObject(m);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
