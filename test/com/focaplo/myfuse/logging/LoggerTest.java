package com.focaplo.myfuse.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class LoggerTest {

	@Test
	public void testSimple(){
		Log log = LogFactory.getLog(getClass());
		for(int i=0;i<2000;i++){
			log.info("I am here " + + i + System.currentTimeMillis());
		}
	}
	
	@Test
	public void testPerformance(){
		Log log = LogFactory.getLog(getClass());
		int total = 1000000;
		long startTime = System.currentTimeMillis();
		for(int i=0;i<total;i++){
			log.info("I am here " + + i + System.currentTimeMillis());
		}
		long endTime=System.currentTimeMillis();
		System.out.println("total time:" + (endTime-startTime) + " ms" + " for " + total + " log events");
	}
}
