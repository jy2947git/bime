package com.focaplo.myfuse.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.focaplo.myfuse.dao.IGrantDao;
import com.focaplo.myfuse.model.ManagedGrant;
import com.focaplo.myfuse.service.GrantService;

@Service(value="grantManager")
@Transactional(readOnly=true)
public class GrantManager extends UniversalManager implements
		GrantService {
	@Autowired
	private IGrantDao grantDao;
	
	public void setGrantDao(IGrantDao grantDao) {
		this.grantDao = grantDao;
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void deleteGrants(List<Long> toBeDeleted) {
		for(Long id:toBeDeleted){
			this.grantDao.remove(ManagedGrant.class, id);
		}
	}

	public List<ManagedGrant> getGrantsAccessableTo(Long userId) {
		return new ArrayList();
	}

}
