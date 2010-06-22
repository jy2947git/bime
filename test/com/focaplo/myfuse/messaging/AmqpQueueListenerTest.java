package com.focaplo.myfuse.messaging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class AmqpQueueListenerTest {
	protected Log log = LogFactory.getLog(getClass());
	@Test
	public void testMessageDrivenBean(){
		AmqpQueueListener listener = new AmqpQueueListener();
		listener.start("192.168.8.128", 5672, "guest","guest","/","monitor", "error", "error", new SimpleMessageHandler(){

			public String handle(String message) {
				log.info("get it!" + message);
				return "done";
			}});
		log.info("done");
	}
}
