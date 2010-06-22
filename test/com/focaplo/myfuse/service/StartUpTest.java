package com.focaplo.myfuse.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

import com.focaplo.myfuse.dao.IUniversalDao;

public class StartUpTest extends BaseManagerTestCase{

	@Autowired
	ApplicationContext context;
	@Test
	public void testAppStartUp(){
		
		PropertyPlaceholderConfigurer placeholder = (PropertyPlaceholderConfigurer)context.getBean("ServicePropertyConfigurer");
		System.out.println(placeholder.toString());
		System.out.println(context.getBeanNamesForType(IUniversalDao.class));
		System.out.println(context.getBeanNamesForType(LookupService.class).length);
		System.out.println(context.getBeanNamesForType(InventoryService.class).length);
		System.out.println(context.getBean("lookupManager"));
	}
}
