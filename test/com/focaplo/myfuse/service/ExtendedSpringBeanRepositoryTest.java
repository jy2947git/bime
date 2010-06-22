package com.focaplo.myfuse.service;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.focaplo.myfuse.service.SpringCommand;
import com.focaplo.myfuse.service.impl.IncomingCommandsListener;

public class ExtendedSpringBeanRepositoryTest extends BaseManagerTestCase{

	@Autowired
	ApplicationContext context;
	
	@Autowired
	ExtendedSpringBeanRepository extendedBeans;
	
	@Test
	public void testLocalQueueListenerLoaded(){
//		extendedBeans.checkAndLoadOptionalBeans();
		assertNotNull(context.getBean("localConnectionFactory"));
	}
	
	@Test
	public void testOutgoingQueueSenderLoaded(){
//		extendedBeans.checkAndLoadOptionalBeans();
		assertNotNull(context.getBean("jmsQueueSender"));
	}
	
	@Test
	public void testIncomingCommandsListenerExec() throws JsonGenerationException, JsonMappingException, IOException{
		IncomingCommandsListener listener = (IncomingCommandsListener) extendedBeans.getBean("incomingCommandsListener");
		assertNotNull(listener);
		SpringCommand sc = new SpringCommand();
		sc.setBeanName("dummyManager");
		sc.setMethodName("doSomething");
		sc.setArguments(new Object[]{"please", "do", "it"});
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(sc);
		listener.exec(json);
	}
}
