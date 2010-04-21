package com.focaplo.myfuse.dao;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.focaplo.myfuse.webapp.spring.ThreadBoundContext;

public class MasterDao implements UserDetailsService{
	protected final Log log = LogFactory.getLog(getClass());
	private SimpleJdbcTemplate simpleJdbcTemplate;
	private IUserDao userDao;
	
	public void setDataSource(DataSource dataSource) {
	    this.simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
	}

	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}
	
	public IUserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	public String getPartitionIdFromUserId(String userId){
		Map resultMap = this.simpleJdbcTemplate.queryForMap("select partition_id from user_partition where user_id=?", userId);
		if(resultMap.isEmpty()){
			return null;
		}else{
			return (String)resultMap.get("partition_id");
		}
	}
	
	public void saveUserPartition(String userId, String partitionId){
		this.simpleJdbcTemplate.update("insert into user_partition(user_id,partition_id) values(?,?)",userId, partitionId);
	}

	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException, DataAccessException {
		String partitionId = this.getPartitionIdFromUserId(userName);
		if(partitionId!=null){
			//set the thread local to pass to the routing data source
			ThreadBoundContext.setValue(partitionId);
			//get the user dao to do the real work
			return this.getUserDao().loadUserByUsername(userName);
		}else{
			//
			log.debug("no such user name " + userName + " in partition table!");
			return null;
		}
	}
}
