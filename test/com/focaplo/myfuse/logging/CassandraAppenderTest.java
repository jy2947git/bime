package com.focaplo.myfuse.logging;


import java.util.List;
import java.util.Map;

import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.junit.Test;

import com.focaplo.myfuse.dao.cassandra.BaseCassandraDao;

public class CassandraAppenderTest {

	@Test
	public void testLogging(){
		Log log = LogFactory.getLog(getClass());
		String[] users = new String[]{"jy2947", "ym7135", "tt0303"};
		
		for(int i=0;i<2000;i++){
			MDC.put("user", users[(int)(Math.random()*3)]);
			log.info("this is test " + i);
			
		}
		
	}
	
	@Test
	public void check(){
		BaseCassandraDao dao = new BaseCassandraDao("192.168.179.129", 9160);
		Map res = dao.getRow("Keyspace1", "Standard1", null, "jy2947", null, null);
		dao.prettyPrint(res);
	}
	
	@Test
	public void browse(){
		BaseCassandraDao dao = new BaseCassandraDao("192.168.179.129", 9160);
		Map res = dao.getRow("Keyspace1", "Standard1", null, "jy2947", null, null);
		List<ColumnOrSuperColumn> first100Columns = (List<ColumnOrSuperColumn>) res.get("columnOrSuperColumns");
		Column firstColumn = first100Columns.get(0).getColumn();
		
		
	}
}
