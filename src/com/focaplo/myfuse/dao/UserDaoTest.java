package com.focaplo.myfuse.dao;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.focaplo.myfuse.Constants;
import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.model.Role;
import com.focaplo.myfuse.model.User;

public class UserDaoTest extends BaseDaoTestCase {
	protected final Log log = LogFactory.getLog(getClass());
	
	
	
public void setUniversalDao(UniversalDao universalDao) {
		this.universalDao = universalDao;
	}

	public void setUserDao(UserDao userDao) {
		
		userDao = userDao;
	}
@Test
	public void testSaveNewUser(){
		Long id = this.saveNewUser();
		assertNotNull(id);
		log.info("saved " + id);
		
	}
	private Long saveNewUser(){
		
		User user = new User();
		user.setFirstName("test" + System.currentTimeMillis());
		user.setLastName("tester");
		user.setUsername(user.getFirstName()+" "+user.getLastName());
		user.addRole((Role)this.universalDao.get(Role.class,"ROLE_USER"));
		user.setAccountExpired(false);
		user.setAccountLocked(false);
		user.setCredentialsExpired(true);
		user.setEnabled(true);
		user.setPassword(user.getUsername());
		
//		Lab lab = (Lab)universalDao.get(Lab.class, new Long("1"));
//		user.setLab(lab);
		
		userDao.saveUser(user);
		log.info("user id:" + user.getId());
		return user.getId();
	}
	@Test
	public void testDeleteUser(){
		Long userId = this.saveNewUser();
		
		universalDao.remove(User.class, userId);
		
	}
	@Test
	public void testGetUserByUserName(){
		
	}
	
	@Test
	public void testGetUsersByRole(){
		List<User> superUsers = userDao.getUsersWithRole(Constants.SUPER_USER_ROLE);
		log.info("find " + superUsers.size());
	}
	
	@Test
	public void testGetLabUsers(){
		List<User> users = userDao.getLabUsers();
		for(User u:users){
			log.info(u.getFullName());
		}
	}
}
