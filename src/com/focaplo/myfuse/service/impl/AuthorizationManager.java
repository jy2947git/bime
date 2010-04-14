package com.focaplo.myfuse.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.focaplo.myfuse.model.BaseObject;
import com.focaplo.myfuse.model.ResourceUserAuthorization;
import com.focaplo.myfuse.model.Securable;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.AuthorizationService;

public class AuthorizationManager extends UniversalManagerImpl implements AuthorizationService{
	protected final Log log = LogFactory.getLog(getClass());
	/*
	 * key: user-name
	 * value: resource-permission-map
	 *           key:resouceType(classname)_resourceId
	 *           value:ResourceUserAuthorization object
	 */
	private Map<String,Map<String, ResourceUserAuthorization>> permissionLookupTable = new HashMap<String,Map<String, ResourceUserAuthorization>>();
	


	public boolean getHasPermission(Class resourceClass, Long resourceId, String userName, String permission){
		if(userName==null || userName.equals("") || resourceId==null){
			throw new IllegalArgumentException("Empty user name or resource id " + userName + " , " + resourceId + " , " + permission);
		}
		boolean authorized=false;
		Map<String, ResourceUserAuthorization> userPermissions = this.permissionLookupTable.get(userName);
		if(userPermissions==null){
			authorized = false;
		}else{
			String resourceKey = resourceClass.getName()+"_"+resourceId;
			ResourceUserAuthorization us = userPermissions.get(resourceKey);
			if(us==null){
				authorized = false;
			}else{
				authorized = us.hasPermissionCode(permission);
			}
		}
		log.debug("user " + userName + " to resource " + resourceClass.getName() + " id:" + resourceId + " permission " + permission + " " + authorized );
		return authorized;
	}


	public synchronized void refresh(){
		log.debug("loading the user-resource permissions...");
		if(permissionLookupTable==null){
			 permissionLookupTable = new HashMap<String,Map<String, ResourceUserAuthorization>>();
		}
		permissionLookupTable.clear();
		List<ResourceUserAuthorization> permissions = this.labDao.loodLabResourcePermission();
		//convert to a easy-to-check lookup map
		for(ResourceUserAuthorization r:permissions){
			String userId = r.getUserName();
			String resourceType = r.getResourceType();
			Long resourceId = r.getResourceId();
			String resourceKey = resourceType+"_"+resourceId;
			String permission=r.getPermission();
			log.debug("find user " + userId + " resource " + resourceKey + " permission:" + permission);
			if(!this.permissionLookupTable.containsKey(userId)){
				this.permissionLookupTable.put(userId, new HashMap<String, ResourceUserAuthorization>());
			}
			
			Map<String, ResourceUserAuthorization> userPermissions = this.permissionLookupTable.get(userId);
			if(userPermissions.containsKey(resourceKey)){
				//should not happen!
				log.error("For a single user " + userId + " found the resource " + resourceKey + " show up more than one times!");
			}else{
				//
				userPermissions.put(resourceKey, r);
			}
		}
		//print
		if(this.log.isDebugEnabled()){
			StringBuffer buf = new StringBuffer();
			for(Map.Entry<String, Map<String, ResourceUserAuthorization>> e:this.permissionLookupTable.entrySet()){
				buf.append(e.getKey() + ":\n" );
				for(Map.Entry<String, ResourceUserAuthorization> f:e.getValue().entrySet()){
					buf.append("        " + f.getKey() + "-" + f.getValue().getPermission() + "\n");
				}
				buf.append("\n");
				log.debug(buf.toString());
				buf.delete(0, buf.length());
			}
		}
	}



	public synchronized void turnOnPermission(Class resourceClass, Long resourceId,
			String permission, String userName) {
		if(userName==null || userName.equals("") || resourceId==null){
			throw new IllegalArgumentException("Empty user name or resource id " + userName + " , " + resourceId + " , " + permission);
		}
		ResourceUserAuthorization us = this.labDao.getResourceUserAuthorizationByUserAndResource(resourceClass.getName(), resourceId, userName);
		if(us==null){
			us = new ResourceUserAuthorization(resourceClass, resourceId, userName);
		}
		us.addPermissionCode(permission);
		this.labDao.save(us);
		//
		this.refresh();
	}


	public synchronized void turnOffPermission(Class resourceClass, Long resourceId,
			String permission, String userName) {
		if(userName==null || userName.equals("") || resourceId==null){
			throw new IllegalArgumentException("Empty user name or resource id " + userName + " , " + resourceId + " , " + permission);
		}
		ResourceUserAuthorization us = this.labDao.getResourceUserAuthorizationByUserAndResource(resourceClass.getName(), resourceId, userName);
		if(us==null){
			us = new ResourceUserAuthorization(resourceClass, resourceId, userName);
		}
		us.removePermissionCode(permission);
		this.labDao.save(us);
		//
		this.refresh();
	}


	public synchronized void turnOnPermission(Long userAuthorizationId, String permission) {
		ResourceUserAuthorization us = (ResourceUserAuthorization) this.labDao.get(ResourceUserAuthorization.class, userAuthorizationId);
		us.addPermissionCode(permission);
		this.labDao.save(us);
		this.refresh();
	}


	public synchronized void turnOffPermission(Long userAuthorizationId, String permission) {
		ResourceUserAuthorization us = (ResourceUserAuthorization) this.labDao.get(ResourceUserAuthorization.class, userAuthorizationId);
		us.removePermissionCode(permission);
		this.labDao.save(us);
		//
		this.refresh();
	}



	public void injectResourceCollectionsPermissionForUser(Collection<? extends BaseObject> objects,
			Class resourceType, String userName) {
		
		for(BaseObject obj:objects){
			this.injectResourcePermissionForUser(obj, resourceType, userName);
		}
	}


	public void updatePermissionToResource(Class clazz,
			Long id, String permission, Collection<User> users) {
		//first remove all the existing records
		Set<String> currentAccess = (Set<String>) this.getUsersWithPermission(clazz, id, permission);
		for(String userId:currentAccess){
			ResourceUserAuthorization auth = this.labDao.getResourceUserAuthorizationByUserAndResource(clazz.getName(), id, userId);
			auth.removePermissionCode(permission);
			this.labDao.saveOrUpdate(auth);
			log.debug("removed permission " + permission + " to " + auth.getResourceType() + "_" + auth.getResourceId() + " from user " + userId);
		}
		//then add one by one
		for(User u:users){
			ResourceUserAuthorization us = this.labDao.getResourceUserAuthorizationByUserAndResource(clazz.getName(), id, u.getUsername());
			if(us==null){
				us = new ResourceUserAuthorization(clazz, id, u.getUsername());
			}
			us.addPermissionCode(permission);
			this.labDao.save(us);
		}
		this.refresh();
	}

	private Collection getUsersWithPermission(Class clazz,
			Long id, String permission){
		Set<String> users = new HashSet<String>();
		for(Map.Entry<String, Map<String, ResourceUserAuthorization>> e:this.permissionLookupTable.entrySet()){
			String userId = e.getKey();
			for(Map.Entry<String, ResourceUserAuthorization> f:e.getValue().entrySet()){
				String resourceKey = f.getKey();
				ResourceUserAuthorization auth = f.getValue();
				
				if((clazz.getName() + "_" + id).equalsIgnoreCase(resourceKey) && auth.hasPermissionCode(permission)){
					users.add(userId);
				}
			}
			
		}
		return users;
	}


	public void injectResourcePermissionForUser(BaseObject obj,
			Class resourceType, String userName) {
		Securable securedObject = (Securable)obj;
		securedObject.setCanAuthorize(this.getHasPermission(resourceType, obj.getId(), userName, Securable.authorize));
	
		securedObject.setCanEdit(this.getHasPermission(resourceType, obj.getId(), userName, Securable.edit));
		securedObject.setCanDelete(this.getHasPermission(resourceType, obj.getId(), userName, Securable.delete));

	}



}
