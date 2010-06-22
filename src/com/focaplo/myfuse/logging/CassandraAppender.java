package com.focaplo.myfuse.logging;

import java.util.List;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import scribe.thrift.LogEntry;

import com.focaplo.myfuse.dao.cassandra.BaseCassandraDao;

public class CassandraAppender extends AppenderSkeleton {
	private List<LogEntry> logEntries;
	private String categroy="event";
	private String cassandraHost="localhost";
	private int cassandraPort=9160;
	private String key;
	private String columnFamily;
	private String keySpace;
	private static BaseCassandraDao dao;
	
//	private void connect() throws IllegalStateException, PoolExhaustedException, Exception{
//		if(client==null){
//			CassandraClientPool pool = CassandraClientPoolFactory.INSTANCE.get();
//			synchronized(this) {
//				if(client==null){
//					client = pool.borrowClient(cassandraHost, cassandraPort);
//				}
//			}
//		}
//		else if(client.isClosed()){
//			client=null;
//			CassandraClientPool pool = CassandraClientPoolFactory.INSTANCE.get();
//			synchronized(this) {
//				if(client==null){
//					client = pool.borrowClient(cassandraHost, cassandraPort);
//				}
//			}
//		}
//	}
	@Override
	protected void append(LoggingEvent loggingEvent) {
		try{
			if(dao==null){
				dao = new BaseCassandraDao(cassandraHost, cassandraPort);
			}
			dao.insertColumn(keySpace, columnFamily,(String) loggingEvent.getMDC(key), Long.toString(System.currentTimeMillis()), layout.format(loggingEvent));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		

	}

	@Override
	public boolean requiresLayout() {
		return true;
	}

	public String getCassandraHost() {
		return cassandraHost;
	}

	public void setCassandraHost(String cassandraHost) {
		this.cassandraHost = cassandraHost;
	}

	public int getCassandraPort() {
		return cassandraPort;
	}

	public void setCassandraPort(int cassandraPort) {
		this.cassandraPort = cassandraPort;
	}

	public String getCategroy() {
		return categroy;
	}

	public void setCategroy(String categroy) {
		this.categroy = categroy;
	}

	public List<LogEntry> getLogEntries() {
		return logEntries;
	}

	public void setLogEntries(List<LogEntry> logEntries) {
		this.logEntries = logEntries;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getColumnFamily() {
		return columnFamily;
	}

	public void setColumnFamily(String columnFamily) {
		this.columnFamily = columnFamily;
	}

	public String getKeySpace() {
		return keySpace;
	}

	public void setKeySpace(String keySpace) {
		this.keySpace = keySpace;
	}

}
