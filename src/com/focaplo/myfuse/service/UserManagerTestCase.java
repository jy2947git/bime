package com.focaplo.myfuse.service;

import java.util.List;

import org.junit.Test;

import com.focaplo.myfuse.model.Role;
import com.focaplo.myfuse.model.User;

public class UserManagerTestCase extends BaseManagerTestCase {
	
	@Test
	public void testDeleteUser(){
		
		userManager.removeUser("1");
//		this.setComplete();
	}
	@Test
	public void testSaveUser() throws UserExistsException{
		User user = new User();
		user.setFirstName("admin");
		user.setLastName("bime");
		user.setUsername("admin");
		user.setPassword("admin");
		user.addRole(roleManager.getRole("ROLE_ADMIN"));
		user.setEnabled(true);
		user.setAccountExpired(false);
		user.setAccountLocked(false);
		user.setCredentialsExpired(false);
		userManager.saveUser(user);
//		this.setComplete();
	}
	
	@Test
	public void testGetAllUsers(){
		List<User> allUsers = userManager.getAll(User.class);
		log.info("find " + allUsers.size());
	}
}
