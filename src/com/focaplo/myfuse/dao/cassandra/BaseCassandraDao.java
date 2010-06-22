package com.focaplo.myfuse.dao.cassandra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cassandra.thrift.AuthenticationRequest;
import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ColumnPath;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.KeyRange;
import org.apache.cassandra.thrift.KeySlice;
import org.apache.cassandra.thrift.Mutation;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SliceRange;
import org.apache.cassandra.thrift.SuperColumn;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.cassandra.thrift.Cassandra.Client;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.stereotype.Repository;

@Repository
public class BaseCassandraDao {
	protected final Log log = LogFactory.getLog(getClass());
	String host;
	int port=9160;
	
	public BaseCassandraDao() {
		super();
	}

	public BaseCassandraDao(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}

	interface CassandraOperation{
		abstract void doOperation(Cassandra.Client client, Map<String,Object> parameters, Map res);
	}
	
	public Map executeCassandra(CassandraOperation operation, Map<String,Object> parameters){
//		if(log.isDebugEnabled()){
//			log.debug("---------- parameters --------");
//			if(parameters!=null){
//				for(Map.Entry<String,Object> entry:parameters.entrySet()){
//					log.debug(entry.getKey()+"="+entry.getValue());
//				}
//			}
//			log.debug("---------- end of parameters --------");
//		}
		TTransport transport = null;
		Map res = new HashMap();
		String json = "";
		try{
			transport = new TSocket(host, port);
			TProtocol protocol = new TBinaryProtocol(transport);
			Cassandra.Client client = new Cassandra.Client(protocol);
			transport.open();
			operation.doOperation(client,parameters,res);
			res.put("success", Boolean.TRUE);
			
		}catch(Exception e){
			e.printStackTrace();
			res.put("error", e.getMessage());
			res.put("success", Boolean.FALSE);
		}
		finally{
			try {
				if(transport!=null){
					transport.flush();
					transport.close();
				}
			} catch (TTransportException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			ObjectMapper mapper = new ObjectMapper();
			
//			try {
//				json = mapper.writeValueAsString(res);
//			} catch (Exception e) {
//				e.printStackTrace();
//				res.put("error", res.get("error")==null?e.getMessage():((String)res.get("error"))+"\n"+e.getMessage());
//			} 
			return res;
		}
		
	}


	public Map removeRow(String keySpace,String columnFamily, String rowKey){
		return this.remove(keySpace, columnFamily, rowKey, null, null);
	}
	
	
	
	public Map remove(String keySpace,String columnFamily, String rowKey, String superColumnName, String columnName){
		Map<String, Object> parameters = new HashMap<String,Object>();
		parameters.put("keySpace", keySpace);
		parameters.put("columnFamily", columnFamily);
		parameters.put("rowKey", rowKey);
		parameters.put("superColumnName", superColumnName);
		parameters.put("columnName", columnName);
		return this.executeCassandra(new CassandraOperation(){

			@Override
			public void doOperation(Client client,
					Map<String, Object> parameters, Map res) {
				//first find the SuperColumn or Column
				ColumnPath cpath = new ColumnPath();
				cpath.setColumn_family((String) parameters.get("columnFamily"));
				if(parameters.get("superColumnName")!=null){
					cpath.setSuper_column(((String)parameters.get("superColumnName")).getBytes());
				}
				if(parameters.get("columnName")!=null){
					cpath.setColumn(((String)parameters.get("columnName")).getBytes());
				}
				try {
					if(cpath.getSuper_column()==null && cpath.getColumn()==null){
						//remove the whole row
						client.remove((String) parameters.get("keySpace"), (String) parameters.get("rowKey"), cpath, System.currentTimeMillis() , ConsistencyLevel.QUORUM);
					}else{
							//find the column or super column
							ColumnOrSuperColumn csc = client.get((String) parameters.get("keySpace"), (String) parameters.get("rowKey"), cpath, ConsistencyLevel.QUORUM);
							if(csc.isSetColumn()){
								client.remove((String) parameters.get("keySpace"), (String) parameters.get("rowKey"), cpath, csc.getColumn().getTimestamp() , ConsistencyLevel.QUORUM);
							}else if(csc.isSetSuper_column()){
								client.remove((String) parameters.get("keySpace"), (String) parameters.get("rowKey"), cpath, System.currentTimeMillis(), ConsistencyLevel.QUORUM);
							}
					} 
				}
				catch (Exception e) {
					throw new RuntimeException(e);
				}
				
			}
			
		}, parameters);
	}
	
	public Map getMultipleSlice(String keySpace,String columnFamily, String superColumnName, List<String> rowKeys, String startColumnName, String finishColumnName, Integer count, Boolean reverse){
		Map<String, Object> parameters = new HashMap<String,Object>();
		parameters.put("keySpace", keySpace);
		parameters.put("columnFamily", columnFamily);
		parameters.put("rowKeys", rowKeys);
		parameters.put("superColumnName",superColumnName);
		parameters.put("startColumnName", startColumnName);
		parameters.put("finishColumnName", finishColumnName);
		parameters.put("count", count);
		parameters.put("reverse", reverse);
		return this.executeCassandra(new CassandraOperation(){

			@Override
			public void doOperation(Client client,
					Map<String, Object> parameters, Map res) {
				ColumnParent cp = new ColumnParent();
				cp.setColumn_family((String)parameters.get("columnFamily"));
				if(parameters.get("superColumnName")!=null){
					cp.setSuper_column(((String)parameters.get("superColumnName")).getBytes());
				}
				SlicePredicate sp = new SlicePredicate();
//				sp.setColumn_names(arg0);
				SliceRange sr = new SliceRange();
				if(parameters.get("startColumnName")!=null){
					sr.setStart(((String)parameters.get("startColumnName")).getBytes());
				}
				if(parameters.get("finishColumnName")!=null){
					sr.setFinish(((String)parameters.get("finishColumnName")).getBytes());
				}
				if(parameters.get("count")!=null){
					sr.setCount(((Integer)parameters.get("count")).intValue());
				}
				if(parameters.get("reverse")!=null){
					sr.setReversed(((Boolean)parameters.get("reverse")).booleanValue());
				}
				sp.setSlice_range(sr);
				try{
//				client.multiget(arg0, arg1, arg2, arg3);
					Map<String, List<ColumnOrSuperColumn>> rowToSlices = client.multiget_slice((String) parameters.get("keySpace"), (List<String>)parameters.get("rowKeys"), cp, sp, ConsistencyLevel.QUORUM);
					res.put("rowToSlices", rowToSlices);
			}catch (Exception e) {
				throw new RuntimeException(e);
			}
			}
			
		}, parameters);
	}
	

	
	public Map login(){
		Map<String, Object> parameters = new HashMap<String,Object>();

		return this.executeCassandra(new CassandraOperation(){

			@Override
			public void doOperation(Client client,
					Map<String, Object> parameters, Map res) {
				AuthenticationRequest request = new AuthenticationRequest();
//				request.setCredentials(parameters);
//				client.login(arg0, arg1)
				
			}
			
		}, parameters);
	}
	
	public Map getSlice(String keySpace,String columnFamily, String superColumnName, String rowKey, List<String> columnNames){
		Map<String, Object> parameters = new HashMap<String,Object>();
		parameters.put("keySpace", keySpace);
		parameters.put("columnFamily", columnFamily);
		parameters.put("rowKey", rowKey);
		parameters.put("superColumnName",superColumnName);
		List<byte[]> bytesNames = new ArrayList<byte[]>();
		for(String name:columnNames){
			bytesNames.add(name.getBytes());
		}
		parameters.put("columnNames", bytesNames);
		
		return this.executeCassandra(new CassandraOperation(){

			@SuppressWarnings("unchecked")
			@Override
			public void doOperation(Client client,
					Map<String, Object> parameters, Map res) {
				try{
					ColumnParent cp = new ColumnParent();
					cp.setColumn_family((String)parameters.get("columnFamily"));
					if(parameters.get("superColumnName")!=null){
						cp.setSuper_column(((String)parameters.get("superColumnName")).getBytes());
					}
					SlicePredicate sp = new SlicePredicate();
					sp.setColumn_names((List<byte[]>) parameters.get("columnNames"));
					List<ColumnOrSuperColumn> columnOrSuperColums = client.get_slice((String) parameters.get("keySpace"), (String) parameters.get("rowKey"), cp, sp, ConsistencyLevel.QUORUM);
					res.put("columnOrSuperColumns", columnOrSuperColums);
				}catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			
		}, parameters);
		
	}
	
	public Map getRow(String keySpace,String columnFamily, String superColumnName, String rowKey, Integer count, Boolean reverse){
		return this.getSlice(keySpace, columnFamily, superColumnName, rowKey, "", "", count, reverse);
	}
	
	public Map getSlice(String keySpace,String columnFamily, String superColumnName, String rowKey, String startColumnName, String finishColumnName, Integer count, Boolean reverse){
		Map<String, Object> parameters = new HashMap<String,Object>();
		parameters.put("keySpace", keySpace);
		parameters.put("columnFamily", columnFamily);
		parameters.put("rowKey", rowKey);
		parameters.put("startColumnName", startColumnName);
		parameters.put("finishColumnName", finishColumnName);
		parameters.put("count", count);
		parameters.put("reverse", reverse);
		parameters.put("superColumnName",superColumnName);
		return this.executeCassandra(new CassandraOperation(){

			@SuppressWarnings("unchecked")
			@Override
			public void doOperation(Client client,
					Map<String, Object> parameters, Map res) {
				try{
					ColumnParent cp = new ColumnParent();
					cp.setColumn_family((String)parameters.get("columnFamily"));
					if(parameters.get("superColumnName")!=null){
						cp.setSuper_column(((String)parameters.get("superColumnName")).getBytes());
					}
					SlicePredicate sp = new SlicePredicate();
	//				sp.setColumn_names(arg0);
					SliceRange sr = new SliceRange();
					if(parameters.get("startColumnName")!=null){
						sr.setStart(((String)parameters.get("startColumnName")).getBytes());
					}
					if(parameters.get("finishColumnName")!=null){
						sr.setFinish(((String)parameters.get("finishColumnName")).getBytes());
					}
					if(parameters.get("count")!=null){
						sr.setCount(((Integer)parameters.get("count")).intValue());
					}
					if(parameters.get("reverse")!=null){
						sr.setReversed(((Boolean)parameters.get("reverse")).booleanValue());
					}
					sp.setSlice_range(sr);
					List<ColumnOrSuperColumn> columnOrSuperColums = client.get_slice((String) parameters.get("keySpace"), (String) parameters.get("rowKey"), cp, sp, ConsistencyLevel.QUORUM);
					res.put("columnOrSuperColumns", columnOrSuperColums);
				}catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			
		}, parameters);
	}
	
	public Map getRangeSlices(String keySpace,String columnFamily, String startRowKey, String endRowKey, String superColumnName,  String startColumnName, String finishColumnName, Integer count, Boolean reverse){
		Map<String, Object> parameters = new HashMap<String,Object>();
		parameters.put("keySpace", keySpace);
		parameters.put("columnFamily", columnFamily);
		parameters.put("superColumnName",superColumnName);
		parameters.put("startRowKey", startRowKey);
		parameters.put("endRowKey", endRowKey);
		parameters.put("startColumnName", startColumnName);
		parameters.put("finishColumnName", finishColumnName);
		parameters.put("count", count);
		parameters.put("reverse", reverse);
		return this.executeCassandra(new CassandraOperation(){

			@Override
			public void doOperation(Client client,
					Map<String, Object> parameters, Map res) {
				ColumnParent cp = new ColumnParent();
				cp.setColumn_family((String)parameters.get("columnFamily"));
				if(parameters.get("superColumnName")!=null){
					cp.setSuper_column(((String)parameters.get("superColumnName")).getBytes());	
				}
				SlicePredicate sp = new SlicePredicate();
//				sp.setColumn_names(arg0);
				SliceRange sr = new SliceRange();
				if(parameters.get("startColumnName")!=null){
					sr.setStart(((String)parameters.get("startColumnName")).getBytes());
				}
				if(parameters.get("finishColumnName")!=null){
					sr.setFinish(((String)parameters.get("finishColumnName")).getBytes());
				}
				if(parameters.get("count")!=null){
					sr.setCount(((Integer)parameters.get("count")).intValue());
				}
				if(parameters.get("reverse")!=null){
					sr.setReversed(((Boolean)parameters.get("reverse")).booleanValue());
				}
				sp.setSlice_range(sr);;
				
				KeyRange kr = new KeyRange();
				kr.setStart_key((String)parameters.get("startRowKey"));
				kr.setEnd_key((String)parameters.get("endRowKey"));
				try{
					List<KeySlice> slices = client.get_range_slices((String) parameters.get("keySpace"), cp, sp, kr, ConsistencyLevel.QUORUM);
					res.put("slices", slices);
				} catch (Exception e) {
					throw new RuntimeException(e);
				} 
				
			}
			
		}, parameters);
	}
	

	
	public Map getCount(String keySpace,String columnFamily, String rowKey, String superColumnName){
		Map<String, Object> parameters = new HashMap<String,Object>();
		parameters.put("keySpace", keySpace);
		parameters.put("columnFamily", columnFamily);
		parameters.put("rowKey", rowKey);
		parameters.put("superColumnName", superColumnName);
		return this.executeCassandra(new CassandraOperation(){

			@Override
			public void doOperation(Client client,
					Map<String, Object> parameters, Map res) {
				ColumnParent cp = new ColumnParent();
				cp.setColumn_family((String)parameters.get("columnFamily"));
				if(parameters.get("superColumnName")!=null){
					cp.setSuper_column(((String)parameters.get("superColumnName")).getBytes());	
				}
				try{
					int count = client.get_count((String) parameters.get("keySpace"), (String) parameters.get("rowKey"), cp, ConsistencyLevel.QUORUM);
					res.put("count", count);
				} catch (Exception e) {
					throw new RuntimeException(e);
				} 
			}
			
		}, parameters);
	}
	
	public Map update(String keySpace,String columnFamily, String rowKey, ColumnOrSuperColumn csc){
		Map<String, Object> parameters = new HashMap<String,Object>();
		parameters.put("keySpace", keySpace);
		parameters.put("columnFamily", columnFamily);
		parameters.put("rowKey", rowKey);
		parameters.put("columnOrSuperColumn", csc);
		return this.executeCassandra(new CassandraOperation(){

			@Override
			public void doOperation(Client client,
					Map<String, Object> parameters, Map res) {
				Map<String,Map<String,List<Mutation>>> map = new HashMap<String,Map<String,List<Mutation>>>();
				List<Mutation> mutationsInColumnFamily = new ArrayList<Mutation>();
				Mutation m = new Mutation();
				m.setColumn_or_supercolumn((ColumnOrSuperColumn)parameters.get("columnOrSuperColumn"));
				mutationsInColumnFamily.add(m);
				Map<String, List<Mutation>> updatesPerRow = new HashMap<String, List<Mutation>>();
				updatesPerRow.put((String) parameters.get("columnFamily"), mutationsInColumnFamily);
				
				map.put((String) parameters.get("rowKey"), updatesPerRow);
				try{
					client.batch_mutate((String) parameters.get("keySpace"), map, ConsistencyLevel.QUORUM);
				} catch (Exception e) {
					throw new RuntimeException(e);
				} 
			}
			
		}, parameters);
	}
	
	public Map insertBatchSimple(String keySpace, String rowKey, String[] columnFamilies, String[] names, String[] values){
		Map<String, List<ColumnOrSuperColumn>> columnFamilytToColumns = new HashMap<String, List<ColumnOrSuperColumn>>();
		for(int i=0;i<columnFamilies.length;i++){
			List<ColumnOrSuperColumn> columns = new ArrayList<ColumnOrSuperColumn>();
			{
				ColumnOrSuperColumn csc = new ColumnOrSuperColumn();
				Column c = new Column();
				c.setName(names[i].getBytes());
				c.setValue(values[i].getBytes());
				csc.setColumn(c);
				columns.add(csc);
			}
			columnFamilytToColumns.put(columnFamilies[i], columns);
		}
		return this.insertBatch(keySpace, rowKey, columnFamilytToColumns);
	}
	public Map insertBatch(String keySpace, String rowKey, Map<String, List<ColumnOrSuperColumn>> columnFamilytToColumns){
		Map<String, Object> parameters = new HashMap<String,Object>();
		parameters.put("keySpace", keySpace);
		
		parameters.put("rowKey", rowKey);
		parameters.put("columnFamilytToColumns", columnFamilytToColumns);
		return this.executeCassandra(new CassandraOperation(){

			@Override
			public void doOperation(Client client,
					Map<String, Object> parameters, Map res) {
				
				try{
					client.batch_insert((String)parameters.get("keySpace"), (String)parameters.get("rowKey"), (Map<String, List<ColumnOrSuperColumn>>)parameters.get("columnFamilytToColumns"), ConsistencyLevel.QUORUM);
				} catch (Exception e) {
					throw new RuntimeException(e);
				} 
			}}, parameters);
	}
	
	public Map get(String keySpace,String columnFamily, String rowKey, String superColumnName, String columnName){
		Map<String, Object> parameters = new HashMap<String,Object>();
		parameters.put("keySpace", keySpace);
		parameters.put("columnFamily", columnFamily);
		parameters.put("rowKey", rowKey);
		parameters.put("superColumnName", superColumnName);
		parameters.put("columnName", columnName);
		return this.executeCassandra(new CassandraOperation(){

			@Override
			public void doOperation(Client client,
					Map<String, Object> parameters, Map res) {
				ColumnPath cpath = new ColumnPath();
				cpath.setColumn_family((String) parameters.get("columnFamily"));
				if(parameters.get("superColumnName")!=null){
					cpath.setSuper_column(((String)parameters.get("superColumnName")).getBytes());
				}
				if(parameters.get("columnName")!=null){
					cpath.setColumn(((String)parameters.get("columnName")).getBytes());
				}
				try {
					ColumnOrSuperColumn csc = client.get((String) parameters.get("keySpace"), (String) parameters.get("rowKey"), cpath, ConsistencyLevel.QUORUM);
					res.put("found", Boolean.TRUE);
					if(csc.isSetColumn()){
						res.put("type","column");
						res.put("column", csc.getColumn());
					}else if(csc.isSetSuper_column()){
						res.put("type","superColumn");
//						List<Map<String,String>> columns = new ArrayList<Map<String,String>>();
//						
//						SuperColumn sc = csc.getSuper_column();
//						for(Column c:sc.getColumns()){
//							Map<String,String> cmap = new HashMap<String,String>();
//							cmap.put(new String(c.getName()), new String(c.getValue()));
//							columns.add(cmap);
//						}
						res.put("superColumn", csc.getSuper_column());
					}
				} catch (InvalidRequestException e) {
					throw new RuntimeException(e);
				} catch (NotFoundException e) {
					//do not error out
					res.put("found", Boolean.FALSE);
					
				} catch (UnavailableException e) {
					throw new RuntimeException(e);
				} catch (TimedOutException e) {
					throw new RuntimeException(e);
				} catch (TException e) {
					throw new RuntimeException(e);
				}
				
			}}, parameters);
	}
	
	public Map insertSuperColumn(String keySpace, String columnFamily, String rowKey, String superColumnName, String[] columnNames, String[] columnValues){
		Map<String, Object> parameters = new HashMap<String,Object>();
		parameters.put("keySpace", keySpace);
		parameters.put("columnFamily", columnFamily);
		parameters.put("rowKey", rowKey);
		Map<String,String> columns = new HashMap<String,String>();
		for(int i=0;i<columnNames.length;i++){
			columns.put(columnNames[i], columnValues[i]);
		}
		parameters.put("columns", columns);
		parameters.put("superColumnName", superColumnName);
		return this.executeCassandra(new CassandraOperation(){

			@Override
			public void doOperation(Client client,
					Map<String, Object> parameters, Map res) {
				
				
				for(Map.Entry<String,Object> column:((Map<String,Object>)parameters.get("columns")).entrySet()){
					ColumnPath cpath = new ColumnPath();
					cpath.setColumn_family((String) parameters.get("columnFamily"));
					cpath.setSuper_column(((String)parameters.get("superColumnName")).getBytes());
					cpath.setColumn(column.getKey().getBytes());
					try{
						client.insert((String) parameters.get("keySpace"), (String) parameters.get("rowKey"), cpath, ((String)column.getValue()).getBytes(), System.currentTimeMillis(), ConsistencyLevel.QUORUM);
					} catch (Exception e) {
						throw new RuntimeException(e);
					} 
				}
				
			}}, parameters);
	}
	
	public Map insertColumn(String keySpace, String columnFamily, String rowKey, String name, String value){
		Map<String, Object> parameters = new HashMap<String,Object>();
		parameters.put("keySpace", keySpace);
		parameters.put("columnFamily", columnFamily);
		parameters.put("rowKey", rowKey);
		parameters.put("name", name);
		parameters.put("value", value);
		return this.executeCassandra(new CassandraOperation(){

			@Override
			public void doOperation(Client client, Map parameters, Map res) {
				try{
					String rk = (String) parameters.get("rowKey");
					ColumnPath cpath = new ColumnPath();
					cpath.setColumn_family((String) parameters.get("columnFamily"));
					cpath.setColumn(((String)parameters.get("name")).getBytes());
	
			        client.insert((String) parameters.get("keySpace"), rk, cpath, ((String)parameters.get("value")).getBytes(), System.currentTimeMillis(), ConsistencyLevel.QUORUM);
				} catch (Exception e) {
					throw new RuntimeException(e);
				} 
			}}, parameters);
		
		
	}
	
	
	public Map describeCassandra(String keySpace){
		Map<String, Object> parameters = new HashMap<String,Object>();
		parameters.put("keySpace", keySpace);
		return this.executeCassandra(new CassandraOperation(){

			@Override
			public void doOperation(Client client,
					Map<String, Object> parameters, Map res) {
				try{
					res.put("version", client.describe_version());
//					res.put("ring", client.describe_ring(parameters.get("keySpace")));
					res.put("cluster", client.describe_cluster_name());
					res.put("keySpaces",client.describe_keyspaces());
					
				} catch (Exception e) {
					throw new RuntimeException(e);
				} 
				
			}}, parameters);
	}
	
	public Map listKeySpaces(){
		return this.executeCassandra(new CassandraOperation(){

			@Override
			public void doOperation(Client client, Map parameters, Map res) {
				try {
					for(String sp:client.describe_keyspaces()){
						System.out.println("key space:"+sp);
						//
						Map<String,Map<String,String>> info = client.describe_keyspace(sp);
						for(Map.Entry<String,Map<String,String>> entry:info.entrySet()){
							System.out.println("------------column family " + entry.getKey()+ "-----------");
							
							for(Map.Entry<String, String> e2:entry.getValue().entrySet()){
								System.out.println(e2.getKey()+"->"+e2.getValue());
							}
						}
						res.put(sp, info);
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				} 
			
			}
			
		}, null);
	}
	
//	ONLY available in version 0.7
//	public String addKeySpace(String keySpace, String columnFamily){
//		Map<String, String> parameters = new HashMap<String,String>();
//		parameters.put("keySpace", keySpace);
//		parameters.put("columnFamily", columnFamily);
//		return this.executeCassandra(new CassandraOperation(){

//			@Override
//			public void doOperation(Client client, Map parameters, Map res) {
//				System.out.println("trying to add key space " + parameters.get("keySpace"));
				//
//				KsDef kd = new KsDef();
//				kd.setName((String) parameters.get("keySpace"));
//				kd.setReplication_factor(replication_factor);
//				kd.setStrategy_class("org.apache.cassandra.locator.RackUnawareStrategy");
//				kd.setStrategy_classIsSet(true);
//				kd.setNameIsSet(true);
//				CfDef cd = new CfDef();
//				cd.setTable((String) parameters.get("keySpace"));
//				cd.setName((String) parameters.get("columnFamily"));
//				cd.setComparator_type("UTF8Type");
//				kd.addToCf_defs(cd);
//				try {
//					client.system_add_keyspace(kd);
//				} catch (Exception e) {
//					throw new RuntimeException(e);
//				}
//				
//			}}, parameters);
//	
//	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	
	public static void prettyPrint(Map data) {
		StringBuffer buf = new StringBuffer();
		prettyPrintMap(data, buf, 1);
		System.out.println("map:"+ buf.toString());
	}
	public static void prettyPrintMap(Map data, StringBuffer buf, int level){
//		ObjectMapper mapper = new ObjectMapper();
//		String json = mapper.writeValueAsString(data);
//		Map dataMap = mapper.readValue(json, Map.class);
		
		for(Object key:data.keySet()){
			buf.append("\n");
			for(int i=0;i<level*5;i++){
				buf.append(" ");
			}
			buf.append(key + "->");
			buf.append(" (" + (data.get(key)==null?"":data.get(key).getClass().getSimpleName())+")");
			prettyPrint(data.get(key), buf, level+1);
		}
		
	}
	
	public static void prettyPrint(Object data, StringBuffer buf, int level) {
		if(data==null){
			buf.append("NULL");
			return;
		}
		
		if(data instanceof Map){
			prettyPrintMap((Map)data, buf, level+1);
		}else if(data instanceof List){
			buf.append(" count: " + ((List)data).size());
			for(Object o:(List)data){
				buf.append("\n");
				for(int i=0;i<level*5;i++){
					buf.append(" ");
				}
				buf.append(" (" + o.getClass().getSimpleName()+")");
				prettyPrint(o, buf, level+1);
			}
		}else if(data instanceof byte[]){
			buf.append(" " + new String((byte[])data));
		}else if(data instanceof Column){
			buf.append(new String(((Column)data).getName()) + "->" + new String(((Column)data).getValue()));
		}else if(data instanceof SuperColumn){
			buf.append(new String(((SuperColumn)data).getName())+"->");
			prettyPrint(((SuperColumn)data).getColumns(), buf, level);
		}else if(data instanceof ColumnOrSuperColumn){
			ColumnOrSuperColumn csc = (ColumnOrSuperColumn)data;
			if(csc.isSetColumn()){
				prettyPrint(csc.getColumn(), buf, level);
			}else if(csc.isSetSuper_column()){
				prettyPrint(csc.getSuper_column(), buf, level);
			}
		}else if(data instanceof KeySlice){
			KeySlice ks = (KeySlice)data;
			ks.getColumnsSize();
			buf.append(" " + ks.getKey() + " (" + ks.getColumnsSize()+")");
			List<ColumnOrSuperColumn> columns = ks.getColumns();
			prettyPrint(columns, buf, level);
		}else{
			buf.append(" " + data.toString());
		}
	}
}
