package com.focaplo.myfuse.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.focaplo.myfuse.dao.IGrantDao;
import com.focaplo.myfuse.model.ManagedGrant;
import com.focaplo.myfuse.service.GrantService;

@Service(value="grantManager")
public class GrantManager extends UniversalManager implements
		GrantService {
	private IGrantDao grantDao;
	
	public void setGrantDao(IGrantDao grantDao) {
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
