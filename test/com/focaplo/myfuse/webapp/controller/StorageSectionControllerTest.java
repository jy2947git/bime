package com.focaplo.myfuse.webapp.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.webapp.controller.inventory.StorageSectionController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:WebContent/WEB-INF/dispatcher-servlet.xml","file:WebContent/WEB-INF/applicationContext-security.xml","file:config/applicationContext-service.xml","file:config/applicationContext-dao.xml","file:config/applicationContext-datasource.xml"})
public class StorageSectionControllerTest {
	
	@Autowired
	private ApplicationContext applicationContext;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private HandlerAdapter handlerAdapter;
	
	@Before
	public void setUp(){
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		handlerAdapter = (HandlerAdapter) applicationContext.getBean("annotationMethodHandlerAdapter");
		
	}
	@Test
	public void testJson() throws Exception{
		StorageSectionController controller = applicationContext.getBean(StorageSectionController.class);
		request.setRequestURI("/inventory/chemicalShelve/4/sections/list.json");
		request.setMethod("GET");
		ModelAndView mav = handlerAdapter.handle(request, response, controller);
		System.out.println(mav.getModelMap());
	}

}
