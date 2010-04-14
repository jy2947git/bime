package com.focaplo.myfuse.service;

import java.util.Collection;
import java.util.List;

import com.focaplo.myfuse.model.BaseObject;
import com.focaplo.myfuse.model.ManagedProject;
import com.focaplo.myfuse.model.User;

public interface AuthorizationService {

	public boolean getHasPermission(Class resourceClass, Long resourceId, String userName, String permission);
	
	
//	public void turnOnPermission(Long userAuthorizationId, String permission);
//	
//	public void turnOnPermission(Long userAuthorizationId, String permission);
//	
	public void turnOnPermission(Class resourceType, Long resourceId, String permission, String userName);
	
	public void turnOffPermission(Class resourceType, Long resourceId, String permission, String userName);

	public void injectResourceCollectionsPermissionForUser(Collection<? extends BaseObject> objects,
			Class resourceClass, String username);

	public void refresh();


	public void updatePermissionToResource(Class clazz,
			Long id, String permission, Collection<User> users);


	public void injectResourcePermissionForUser(BaseObject project,
			Class commandClass, String username);
	
}
