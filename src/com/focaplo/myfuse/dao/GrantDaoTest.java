package com.focaplo.myfuse.dao;

import org.junit.Test;

import com.focaplo.myfuse.model.ManagedGrant;

public class GrantDaoTest extends BaseDaoTestCase {

	@Test
	public void testSaveNewGrant(){
		ManagedGrant grant = new ManagedGrant();
		grant.setName("test me");
		this.grantDao.save(grant);
		
	}
}
