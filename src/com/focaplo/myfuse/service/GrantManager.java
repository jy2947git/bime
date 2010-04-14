package com.focaplo.myfuse.service;

import java.util.List;

import com.focaplo.myfuse.model.ManagedGrant;

public interface GrantManager extends UniversalManager {

	void deleteGrants(List<Long> toBeDeleted);

	

	List<ManagedGrant>  getGrantsAccessableTo(Long userId);

}
