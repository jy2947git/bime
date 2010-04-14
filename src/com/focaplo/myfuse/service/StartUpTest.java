package com.focaplo.myfuse.service;

import org.junit.Test;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartUpTest {

	@Test
	public void testAppStartUp(){
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"applicationContext*.xml"
		});
		PropertyPlaceholderConfigurer placeholder = (PropertyPlaceholderConfigurer)context.getBean("ServicePropertyConfigurer");
		System.out.println(placeholder.toString());
	}
}
