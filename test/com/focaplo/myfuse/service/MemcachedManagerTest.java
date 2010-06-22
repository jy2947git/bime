package com.focaplo.myfuse.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MemcachedManagerTest extends BaseManagerTestCase{

	@Test
	public void testSpring(){
		
	}
	
	@Test
	public void testSimple(){
		memcachedManager.turnOn();
		memcachedManager.set("test", 100, "this is it");
		String s = (String)memcachedManager.get("test");
		assertNotNull(s);
		
	}
}
