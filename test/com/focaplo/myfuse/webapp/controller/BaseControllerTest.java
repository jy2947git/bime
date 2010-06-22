package com.focaplo.myfuse.webapp.controller;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.HandlerAdapter;

import com.focaplo.myfuse.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:WebContent/WEB-INF/dispatcher-servlet.xml","file:WebContent/WEB-INF/applicationContext-security.xml","file:config/applicationContext-service.xml","file:config/applicationContext-dao.xml","file:config/applicationContext-datasource.xml"})
public class BaseControllerTest {
	@Autowired
	ApplicationContext applicationContext;
	MockHttpServletRequest request;
	MockHttpServletResponse response;
	HandlerAdapter handlerAdapter;
	SecurityContext mockSecurityContext;
	@Before
	public void setUp(){
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		handlerAdapter = (HandlerAdapter) applicationContext.getBean("annotationMethodHandlerAdapter");
		GrantedAuthority[] grantedAuthorities = new GrantedAuthority[]{new GrantedAuthorityImpl("ROLE_USER")};
		User user = new User();
		user.setUsername("user");
		UserDetails userDetails =user;
		Authentication authentication = new TestingAuthenticationToken(userDetails, "test", grantedAuthorities);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
}
