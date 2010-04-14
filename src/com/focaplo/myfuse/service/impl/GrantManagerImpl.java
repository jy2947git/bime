package com.focaplo.myfuse.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.focaplo.myfuse.dao.GrantDao;
import com.focaplo.myfuse.model.ManagedGrant;
import com.focaplo.myfuse.service.GrantManager;

public class GrantManagerImpl extends UniversalManagerImpl implements
		GrantManager {
	private GrantDao grantDao;
	
	public void setGrantDao(GrantDao grantDao) {
		this.grantDao = grantDao;
	}

	public void deleteGrants(List<Long> toBeDeleted) {
		for(Long id:toBeDeleted){
			this.grantDao.remove(ManagedGrant.class, id);
		}
	}

	public List<ManagedGrant> getGrantsAccessableTo(Long userId) {
		return new ArrayList();
	}

}
