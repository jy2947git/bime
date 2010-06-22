package com.focaplo.myfuse.service;

import static org.junit.Assert.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.focaplo.myfuse.service.impl.DynDataSourceRegister;

public class DynDataSourceRegisterTest extends BaseManagerTestCase{
	protected final Log log = LogFactory.getLog(getClass());

	@Test
	public void testScan(){
 
		dynDataSourceRegister.scan();

	}
	
	@Test
	public void testFileMonitor(){
		DynDataSourceRegister register = new DynDataSourceRegister();
		
	}
		
}
