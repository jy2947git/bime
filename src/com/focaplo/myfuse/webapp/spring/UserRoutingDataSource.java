package com.focaplo.myfuse.webapp.spring;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


import com.focaplo.myfuse.dao.MasterDao;
import com.focaplo.myfuse.model.User;

public class UserRoutingDataSource extends AbstractRoutingDataSource {
	protected final Log log = LogFactory.getLog(getClass());

	Map<String,DataSource> newDataSources = new HashMap<String,DataSource>();
//	private MasterDao masterDao;
	
	@Override
	protected Object determineCurrentLookupKey() {
		//return the lab name of the current user
//		SecurityContext sc = SecurityContextHolder.getContext();
//		if(sc==null || sc.getAuthentication()==null || sc.getAuthentication().getPrincipal()==null){
//			log.debug("first time login, check master db to figure out the partition");
//			String partitionId = ThreadBoundContext.getValue();
//			log.debug("find thread local partition " + partitionId);
//			return partitionId;
//		}
//		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		log.debug("find user " + user.getUsername() + " lab " + user.getLab());
//		return user.getLab().getName();
		String partitionId = ThreadBoundContext.getValue();
		log.debug("find thread local partition " + partitionId);
		return partitionId;
	}
	
	public void addTargetDataSource(String key, DataSource ds){
		this.newDataSources.put(key, ds);
		log.info("added " + key + " -> " + ds + " to routing");
	}

	public void removeTargetDataSource(String key){
		if(this.newDataSources.containsKey(key)){
			this.newDataSources.remove(key);
		}
		log.info("removed " + key + " from routing");
	}
	@Override
	protected DataSource determineTargetDataSource() {
		Object currentLookupKey = this.determineCurrentLookupKey();
		//first check the new ones
		DataSource newDs =  this.newDataSources.get(currentLookupKey);
		
		if(newDs!=null){
			log.info("Found new data source with lookup " + currentLookupKey + "  and  " + newDs);
			return newDs;
		}else{
			return super.determineTargetDataSource();
		}
	}

	
//
//	public MasterDao getMasterDao() {
//		return masterDao;
//	}
//
//	public void setMasterDao(MasterDao masterDao) {
//		this.masterDao = masterDao;
//	}


}
