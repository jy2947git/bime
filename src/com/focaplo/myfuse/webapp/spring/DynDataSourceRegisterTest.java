package com.focaplo.myfuse.webapp.spring;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class DynDataSourceRegisterTest {
	protected final Log log = LogFactory.getLog(getClass());
	@Test
	public void testSearchFiles(){
		DynDataSourceRegister tester = new DynDataSourceRegister();
		tester.setScanRootDirectory("/usr/local/bime-home");
		List<String> files = tester.searchSpringContextFiles("/usr/local/bime-home");
		for(String file:files){
			log.info(file);
		}
	}
}
