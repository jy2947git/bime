package com.focaplo.myfuse.dao;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.cassandra.thrift.KeySlice;
import org.apache.cassandra.thrift.SuperColumn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import com.focaplo.myfuse.dao.cassandra.BaseCassandraDao;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations="classpath:applicationContext*.xml")
public class CassandraDaoTest {
	protected final Log log = LogFactory.getLog(getClass());
//	@Autowired
	BaseCassandraDao dao;
	
	@Before
	public void setup(){
		dao = new BaseCassandraDao("192.168.179.129", 9160);
	}
	@Test
	public void testListKeySpace(){
		Map res = dao.listKeySpaces();
		assertTrue(res.get("success")!=null);
		assertTrue(((Boolean)res.get("success")).booleanValue());
		dao.prettyPrint(res);
	}
	
//	@Test
//	public void testAddKeySpace(){
//		System.out.println(dao.addKeySpace("bime", "log_by_ip"));
//	}
	
	@Test
	public void testInsertStandardColumn(){
		try{
			Map res = dao.insertColumn("Keyspace1", "Standard1", "test1",  "1.2.3.4", "logged in");
			assertTrue(res.get("success")!=null);
			assertTrue(((Boolean)res.get("success")).booleanValue());
			dao.prettyPrint(res);
		} finally{
			dao.remove("Keyspace1", "Super1", "test1", null, null);
		}
	}
	
	@Test
	public void testDescribeCassandra(){
		Map res = dao.describeCassandra("Keysapce1");
		assertTrue(res.get("success")!=null);
		assertTrue(((Boolean)res.get("success")).booleanValue());
		dao.prettyPrint(res);
	}
	
	@Test
	public void testGetStandardColumnValue(){
		dao.insertColumn("Keyspace1", "Standard1", "test1",  "1.2.3.4", "logged in");
		try{
			Map res = dao.get("Keyspace1", "Standard1", "test1", null, "1.2.3.4");
			assertTrue(res.get("success")!=null);
			assertTrue(((Boolean)res.get("success")).booleanValue());
			dao.prettyPrint(res);
		} finally{
			dao.remove("Keyspace1", "Super1", "test1", null, null);
		}
	}
	
	@Test
	public void testInsertSuperColumn(){
		try{
			Map res = dao.insertSuperColumn("Keyspace1", "Super1", "test1", "1.2.3.4", new String[]{"1","2","3","4"}, new String[]{"a","b","c","d"});
			assertTrue(res.get("success")!=null);
			assertTrue(((Boolean)res.get("success")).booleanValue());
			dao.prettyPrint(res);
		} finally{
			dao.remove("Keyspace1", "Super1", "test1", null, null);
		}
	}
	
	@Test
	public void testGetSuperColumnValue(){
		dao.insertSuperColumn("Keyspace1", "Super1", "test1", "1.2.3.4", new String[]{"1","2","3","4"}, new String[]{"a","b","c","d"});
		try{
			Map res = dao.get("Keyspace1", "Super1", "test1", "1.2.3.4", null);
			assertTrue(res.get("success")!=null);
			assertTrue(((Boolean)res.get("success")).booleanValue());
			dao.prettyPrint(res);
		} finally{
			dao.remove("Keyspace1", "Super1", "test1", null, null);
		}
	}
	
	@Test
	public void testGetColumnOfSuperColumnValue(){
		dao.insertSuperColumn("Keyspace1", "Super1", "test1", "1.2.3.4", new String[]{"1","2","3","4"}, new String[]{"a","b","c","d"});
		try{
			Map res = dao.get("Keyspace1", "Super1", "test1", "1.2.3.4", "1");
			assertTrue(res.get("success")!=null);
			assertTrue(((Boolean)res.get("success")).booleanValue());
			dao.prettyPrint(res);
		} finally{
			dao.remove("Keyspace1", "Super1", "test1", null, null);
		}
	}
	
	@Test
	public void testNotFound(){
		Map res = dao.get("Keyspace1", "Super1", "test1", "1.2.3.54", null);
		assertTrue(res.get("success")!=null);
		assertTrue(((Boolean)res.get("success")).booleanValue());
		dao.prettyPrint(res);
	}
	
	@Test
	public void testRemoveStandardColumn(){
		dao.insertColumn("Keyspace1", "Standard1", "test1",  "1.2.3.4", "logged in");
		try{
			Map res = dao.remove("Keyspace1", "Standard1", "test1", null, "1.2.3.4");
			assertTrue(res.get("success")!=null);
			assertTrue(((Boolean)res.get("success")).booleanValue());
			dao.prettyPrint(res);
		} finally{
			dao.remove("Keyspace1", "Standard1", "test1", null, null);
		}
	}
	
	@Test
	public void testRemoveSuperColumn(){
		dao.insertSuperColumn("Keyspace1", "Super1", "test1", "1.2.3.4", new String[]{"1","2","3","4"}, new String[]{"a","b","c","d"});
		try{
			Map res=dao.remove("Keyspace1", "Super1", "test1", "1.2.3.4", null);
			assertTrue(res.get("success")!=null);
			assertTrue(((Boolean)res.get("success")).booleanValue());
			dao.prettyPrint(res);
		} finally{
			dao.remove("Keyspace1", "Super1", "test1", null, null);
		}
	}
	
	@Test
	public void testRemoveColumnOfSuperColumn(){
		dao.insertSuperColumn("Keyspace1", "Super1", "test1", "1.2.3.4", new String[]{"1","2","3","4"}, new String[]{"a","b","c","d"});
		try{
			Map res = dao.remove("Keyspace1", "Super1", "test1", "1.2.3.4", "1");
			assertTrue(res.get("success")!=null);
			assertTrue(((Boolean)res.get("success")).booleanValue());
			dao.prettyPrint(res);
		} finally{
			dao.remove("Keyspace1", "Super1", "test1", null, null);
		}
	}
	
	@Test
	public void testRemoveRowSuper(){
		dao.insertSuperColumn("Keyspace1", "Super1", "test1", "1.2.3.4", new String[]{"1","2","3","4"}, new String[]{"a","b","c","d"});
		
		Map res = dao.remove("Keyspace1", "Super1", "test1", null, null);
		assertTrue(res.get("success")!=null);
		assertTrue(((Boolean)res.get("success")).booleanValue());
		dao.prettyPrint(res);
	}
	
	@Test
	public void testRemoveRowStandard(){
		dao.insertColumn("Keyspace1", "Standard1", "test1",  "1234", "logged in 1234");
		Map res = dao.remove("Keyspace1", "Standard1", "test1", null, null);
		assertTrue(res.get("success")!=null);
		assertTrue(((Boolean)res.get("success")).booleanValue());
		dao.prettyPrint(res);
	}
	
	@Test
	public void testGetRow(){
		
		try {
			dao.insertColumn("Keyspace1", "Standard1", "test1",  "1234", "logged in 1234");
			dao.insertColumn("Keyspace1", "Standard1", "test1",  "1235", "logged in 1235");
			dao.insertColumn("Keyspace1", "Standard1", "test1",  "1236", "logged in 1236");
			Map userData = dao.getRow("Keyspace1", "Standard1", null, "test1", null, null);
			assertTrue(userData.get("success")!=null);
			assertTrue(((Boolean)userData.get("success")).booleanValue());
			
			dao.prettyPrint(userData);
			
		} finally{
			dao.remove("Keyspace1", "Standard1", "test1", null, null);
		}
	}
	
	@Test
	public void testGetSliceOfColumnsFromStartToEnd2(){
		dao.remove("Keyspace1", "Standard1", "test1", null, null);
		try {
			dao.insertColumn("Keyspace1", "Standard1", "test1",  "1234", "logged in 1234");
			dao.insertColumn("Keyspace1", "Standard1", "test1",  "1235", "logged in 1235");
			dao.insertColumn("Keyspace1", "Standard1", "test1",  "1236", "logged in 1236");
			Map userData = dao.getSlice("Keyspace1", "Standard1", null, "test1", "1234", "1235", null, null);
			assertTrue(userData.get("success")!=null);
			assertTrue(((Boolean)userData.get("success")).booleanValue());
			
			dao.prettyPrint(userData);
			
		} finally{
			dao.remove("Keyspace1", "Standard1", "test1", null, null);
		}
	}
	
	@Test
	public void testGetSliceOfColumnsByNames(){
		dao.remove("Keyspace1", "Standard1", "test1", null, null);
		try {
			dao.insertColumn("Keyspace1", "Standard1", "test1",  "1234", "logged in 1234");
			dao.insertColumn("Keyspace1", "Standard1", "test1",  "1235", "logged in 1235");
			dao.insertColumn("Keyspace1", "Standard1", "test1",  "1236", "logged in 1236");
			List<String> columns = new ArrayList<String>();
			columns.add("1234");
			columns.add("1235");
			Map userData = dao.getSlice("Keyspace1", "Standard1", null,  "test1", columns);
			assertTrue(userData.get("success")!=null);
			assertTrue(((Boolean)userData.get("success")).booleanValue());
			
			dao.prettyPrint(userData);
			
		} finally{
			dao.remove("Keyspace1", "Standard1", "test1", null, null);
		}
	}
	
	@Test
	public void testUpdateStandardColumn(){
		dao.insertColumn("Keyspace1", "Standard1", "test1",  "1.2.3.4", "logged in");
		try{
			Map res = dao.get("Keyspace1", "Standard1", "test1", null, "1.2.3.4");
			dao.prettyPrint(res);
			assertTrue(res.get("success")!=null);
			assertTrue(((Boolean)res.get("success")).booleanValue());
			Column c = (Column) res.get("column");
			c.setValue("logged in again".getBytes());
			ColumnOrSuperColumn csc = new ColumnOrSuperColumn();
			csc.setColumn(c);
			res = dao.update("Keyspace1", "Standard1", "test1", csc);
			assertTrue(res.get("success")!=null);
			assertTrue(((Boolean)res.get("success")).booleanValue());
			//get the value again
			res = dao.get("Keyspace1", "Standard1", "test1", null, "1.2.3.4");
			dao.prettyPrint(res);
		} finally{
			dao.remove("Keyspace1", "Super1", "test1", null, null);
		}
	}
	
	
	@Test
	public void testInsertBatch(){
		Map<String, List<ColumnOrSuperColumn>> columnFamilytToColumns = new HashMap<String, List<ColumnOrSuperColumn>>();
		{
			List<ColumnOrSuperColumn> columns = new ArrayList<ColumnOrSuperColumn>();
			{
				ColumnOrSuperColumn csc = new ColumnOrSuperColumn();
				Column c = new Column();
				c.setName("c1".getBytes());
				c.setValue("1000".getBytes());
				csc.setColumn(c);
				columns.add(csc);
			}
			columnFamilytToColumns.put("Standard1", columns);
		}
		{
			List<ColumnOrSuperColumn> columns = new ArrayList<ColumnOrSuperColumn>();
			{
				ColumnOrSuperColumn csc = new ColumnOrSuperColumn();
				SuperColumn sc = new SuperColumn();
				
				sc.setName("sc1".getBytes());
				{
					Column c = new Column();
					c.setName("sc1".getBytes());
					c.setValue("1000".getBytes());
					sc.addToColumns(c);
				}
				{
					Column c = new Column();
					c.setName("sc2".getBytes());
					c.setValue("3000".getBytes());
					sc.addToColumns(c);
				}
				csc.setSuper_column(sc);
				columns.add(csc);
			}
			columnFamilytToColumns.put("Super1", columns);
		}
		try{
			Map res = dao.insertBatch("Keyspace1", "test11", columnFamilytToColumns);
			
			assertTrue(res.get("success")!=null);
			assertTrue(((Boolean)res.get("success")).booleanValue());
			
			res = dao.getRow("Keyspace1", "Super1", "sc1", "test11", null, null);
			dao.prettyPrint(res);
			res = dao.getRow("Keyspace1", "Standard1", null, "test11", null, null);
			dao.prettyPrint(res);
			
		}finally{
			dao.remove("Keyspace1", "Super1", "test1", null, null);
			dao.remove("Keyspace1", "Standard1", "test1", null, null);
		}
	}
	
	@Test
	public void testInsertBatchSimple(){
		try{
			Map res = dao.insertBatchSimple("Keyspace1", "test13", new String[]{"Standard1", "Standard2"}, new String[]{"s1","s2"}, new String[]{"v1", "v2"});
			assertTrue(res.get("success")!=null);
			assertTrue(((Boolean)res.get("success")).booleanValue());
			dao.prettyPrint(res);
			res = dao.getRow("Keyspace1", "Standard2", null, "test13", null, null);
			dao.prettyPrint(res);
			res = dao.getRow("Keyspace1", "Standard1", null,  "test13", null, null);
			dao.prettyPrint(res);
		}finally{
			dao.remove("Keyspace1", "Standard2", "test13", null, null);
			dao.remove("Keyspace1", "Standard1", "test13", null, null);
		}
	}

	
	
	@Test
	public void testGetCountFromColumnFamily(){
		try{
			dao.insertColumn("Keyspace1", "Standard1", "test14",  "1.2.3.4", "logged in");
			dao.insertColumn("Keyspace1", "Standard1", "test14",  "1.2.3.5", "logged in");
			dao.insertColumn("Keyspace1", "Standard1", "test14",  "1.2.3.6", "logged in");
			Map res = dao.getCount("Keyspace1", "Standard1", "test14", null);
			assertTrue(res.get("success")!=null);
			assertTrue(((Boolean)res.get("success")).booleanValue());
			assertTrue("3".equalsIgnoreCase(res.get("count").toString()));
			res = dao.getRow("Keyspace1", "Standard1", null, "test14", null, null);
			dao.prettyPrint(res);
		}finally{
			dao.remove("Keyspace1", "Standard1", "test14", null, null);
		}
	}
	
	@Test
	public void testGetCountFromSuperColumn(){
		try{
			dao.insertSuperColumn("Keyspace1", "Super1", "test15",  "super100", new String[]{"a","b","c","d"}, new String[]{"1","2","3","4"});
			
			Map res = dao.getCount("Keyspace1", "Super1", "test15", "super100");
			dao.prettyPrint(res);
			assertTrue(res.get("success")!=null);
			assertTrue(((Boolean)res.get("success")).booleanValue());
			assertTrue("4".equalsIgnoreCase(res.get("count").toString()));
			res = dao.getRow("Keyspace1", "Super1", "super100", "test15", null, null);
			dao.prettyPrint(res);
		}finally{
			dao.remove("Keyspace1", "Super1", "test15", null, null);
		}
	}
	
	@Test
	public void testGetRangeSlicesSimple(){
		try{
			dao.insertColumn("Keyspace1", "Standard1", "test16",  "1.2.3.4", "logged in");
			dao.insertColumn("Keyspace1", "Standard1", "test16",  "1.2.3.5", "logged in");
			dao.insertColumn("Keyspace1", "Standard1", "test17",  "1.2.3.6", "logged in");
			dao.insertColumn("Keyspace1", "Standard1", "test17",  "1.2.3.4", "logged in");
			dao.insertColumn("Keyspace1", "Standard1", "test18",  "1.2.3.5", "logged in");
			dao.insertColumn("Keyspace1", "Standard1", "test18",  "1.2.3.6", "logged in");
			
			Map res = dao.getRangeSlices("Keyspace1", "Standard1", "", "", null, "1.2.3.4", "1.2.3.5", null, null);
			assertTrue(res.get("success")!=null);
			assertTrue(((Boolean)res.get("success")).booleanValue());
			dao.prettyPrint(res);
		}finally{
			dao.remove("Keyspace1", "Standard1", "test16", null, null);
			dao.remove("Keyspace1", "Standard1", "test17", null, null);
			dao.remove("Keyspace1", "Standard1", "test18", null, null);
		}
	}
	
	@Test
	public void testGetRangeSlicesSuper(){
		try{
			dao.insertSuperColumn("Keyspace1", "Super1", "test19",  "super300", new String[]{"a","b","c","d"}, new String[]{"1","2","3","4"});
			Map res = dao.getRow("Keyspace1", "Super1", "super300", "test19", null, null);
			dao.prettyPrint(res);
			res = dao.getSlice("Keyspace1", "Super1", "super300", "test19", "a", "c", null, null);
			dao.prettyPrint(res);
			dao.insertSuperColumn("Keyspace1", "Super1", "test20",  "super300", new String[]{"a","b","c","d"}, new String[]{"1","2","3","4"});
			dao.insertSuperColumn("Keyspace1", "Super1", "test21",  "super300", new String[]{"a","b","c","d"}, new String[]{"1","2","3","4"});
			dao.insertSuperColumn("Keyspace1", "Super1", "test22",  "super300", new String[]{"a","b","c","d"}, new String[]{"1","2","3","4"});
			dao.insertSuperColumn("Keyspace1", "Super1", "test23",  "super300", new String[]{"a","b","c","d"}, new String[]{"1","2","3","4"});
			res = dao.getRangeSlices("Keyspace1", "Super1", "test19", "test22", "super300", "a", "b", null, null);
			assertTrue(res.get("success")!=null);
			assertTrue(((Boolean)res.get("success")).booleanValue());
			dao.prettyPrint(res);
		}finally{
			dao.remove("Keyspace1", "Super1", "test19", null, null);
			dao.remove("Keyspace1", "Super1", "test20", null, null);
			dao.remove("Keyspace1", "Super1", "test21", null, null);
			dao.remove("Keyspace1", "Super1", "test22", null, null);
			dao.remove("Keyspace1", "Super1", "test23", null, null);
		}
	}
	@Test
	public void testGetMultipleSlicesSimple(){
		try{
			dao.insertColumn("Keyspace1", "Standard1", "test30",  "1.2.3.4", "logged in");
			dao.insertColumn("Keyspace1", "Standard1", "test31",  "1.2.3.5", "logged in");
			dao.insertColumn("Keyspace1", "Standard1", "test32",  "1.2.3.6", "logged in");
			dao.insertColumn("Keyspace1", "Standard1", "test33",  "1.2.3.4", "logged in");
			dao.insertColumn("Keyspace1", "Standard1", "test34",  "1.2.3.5", "logged in");
			dao.insertColumn("Keyspace1", "Standard1", "test35",  "1.2.3.4", "logged in");
			dao.insertColumn("Keyspace1", "Standard1", "test35",  "1.2.3.5", "logged in");
			dao.insertColumn("Keyspace1", "Standard1", "test35",  "1.2.3.6", "logged in");
			List<String> rowKeys = new ArrayList<String>();
			rowKeys.add("test30");
			rowKeys.add("test32");
			rowKeys.add("test35");
			Map res = dao.getMultipleSlice("Keyspace1", "Standard1", null, rowKeys, "1.2.3.4", "1.2.3.6", null, null);
			assertTrue(res.get("success")!=null);
			assertTrue(((Boolean)res.get("success")).booleanValue());
			dao.prettyPrint(res);
		}finally{
			dao.remove("Keyspace1", "Standard1", "test30", null, null);
			dao.remove("Keyspace1", "Standard1", "test31", null, null);
			dao.remove("Keyspace1", "Standard1", "test32", null, null);
			dao.remove("Keyspace1", "Standard1", "test33", null, null);
			dao.remove("Keyspace1", "Standard1", "test34", null, null);
			dao.remove("Keyspace1", "Standard1", "test35", null, null);
		}
	}
	
	
}