package com.focaplo.myfuse.dao;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class LabDaoDaoTest extends BaseDaoTestCase {

	@Test
	public void testQueryTablesWithIdColumn(){
		List<String> tables = this.labDao.findAllTablesWithIdColumn();
		for(String t:tables){
			log.info(t);
		}
	}
}
