package com.focaplo.myfuse.messaging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class AmqpQueueSenderTest {
	protected Log log = LogFactory.getLog(getClass());
	@Test
	public void testSend(){
		AmqpQueueSender sender = new AmqpQueueSender();
		sender.sendMessage("192.168.8.128", 5672, "guest","guest","/","monitor", "error", "something bad happened!");
	}
}
