package com.focaplo.myfuse.webapp.controller;

import static org.junit.Assert.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.webapp.controller.system.SetupMessagingQueueController;

public class SetupMessagingQueueControllerTest  extends BaseControllerTest{
	protected Log log = LogFactory.getLog(getClass());
	@Test
	public void testSetUpQueueConnectionFactory(){
		RestTemplate restTempalte = new RestTemplate();
		
		ResponseEntity<String> response = restTempalte.postForEntity("http://localhost:8080/bime/support/messaging/setupQueueConnection.html?host=localhost&port=5445&ajax=true", null, String.class);
		log.info("response:" + response);
		assertEquals("done", response.getBody());
	}
	
	@Test
	public void testDisplayQueueuConnectionFactory(){
		RestTemplate restTempalte = new RestTemplate();
		String response = restTempalte.getForObject("http://localhost:8080/bime/support/messaging/showQueueConnection.html", String.class);
		log.info("response:" + response);
		assertNotNull(response);
	}
	
	@Test
	public void testStartUpListener(){
		RestTemplate restTempalte = new RestTemplate();
		String response = restTempalte.postForObject("http://localhost:8080/bime/support/messaging/startUpListener.html?queueName=queue.incoming.command&delegateBeanName=incomingCommandsListener&deletgateMethodName=exec", null, String.class);
		log.info("response:" + response);
		assertEquals("done", response);
	}
	
	@Test
	public void testDisplayListeners(){
		RestTemplate restTempalte = new RestTemplate();
		String response = restTempalte.getForObject("http://localhost:8080/bime/support/messaging/showListeners.html", String.class);
		log.info("response:" + response);
		assertNotNull(response);
	}
	
	@Test
	public void testQueueConnectionFactory() throws Exception{
		SetupMessagingQueueController controller = applicationContext.getBean(SetupMessagingQueueController.class);
		request.setRequestURI("/support/messaging/setupQueueConnection.html");
		request.setMethod("POST");
		request.addParameter("host", "localhost");
		request.addParameter("port","5445");
		request.addParameter("ajax","true");
	
		//request.setQueryString("orderItemCategoryId=2&orderItemMaker=4&orderItemAmount=44&orderItemUnitPrice=4444&orderItemTotalCost=195536&orderItemSupplier=444444");
		handlerAdapter.handle(request, response, controller);
		System.out.println(response.getContentAsString());
		
		//display
		MockHttpServletResponse response2 = new MockHttpServletResponse();
		request.removeAllParameters();
		request.setRequestURI("/support/messaging/showQueueConnection.html");
		request.addParameter("ajax", "true");
		request.setMethod("GET");
		handlerAdapter.handle(request, response2, controller);
		System.out.println(response2.getContentAsString());
		//start up listener
		MockHttpServletResponse response3 = new MockHttpServletResponse();
		request.removeAllParameters();
		request.setRequestURI("/support/messaging/startUpListener.html");
		//queueName=queue.incoming.command&delegateBeanName=incomingCommandsListener&deletgateMethodName=exec
		request.addParameter("queueName","queue.incoming.command");
		request.addParameter("delegateBeanName","incomingCommandsListener");
		request.addParameter("deletgateMethodName","deletgateMethodName");
		request.addParameter("ajax", "true");
		request.setMethod("POST");
		handlerAdapter.handle(request, response3, controller);
		System.out.println(response3.getContentAsString());
		//display
		MockHttpServletResponse response4 = new MockHttpServletResponse();
		request.removeAllParameters();
		request.setRequestURI("/support/messaging/showListeners.html");
		request.addParameter("ajax", "true");
		request.setMethod("GET");
		handlerAdapter.handle(request, response4, controller);
		System.out.println(response4.getContentAsString());
	}
}
