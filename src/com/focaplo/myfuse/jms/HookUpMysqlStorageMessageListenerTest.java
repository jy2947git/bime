package com.focaplo.myfuse.jms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext*.xml")
public class HookUpMysqlStorageMessageListenerTest {
	@Test
	public void testListen(){
		//loading spring will trigger it
		for(;;){
		
		}
		
	}
}
