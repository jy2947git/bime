package com.focaplo.myfuse.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.util.StringUtils;
@Entity
@Table(name="resource_user_authorization")
public class ResourceUserAuthorization extends BaseObject implements Serializable{
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
	private Long id;
	
	@Basic
	private String resourceType;
	@Basic
	private Long resourceId;
	@Basic
	private String userName;
	@Basic
	private String permission;
	
	public ResourceUserAuthorization() {
		super();
		
	}
	public ResourceUserAuthorization(Class resourceClass, Long resourceId,
			String userName) {
		super();
		this.resourceType = resourceClass.getName();
		this.resourceId = resourceId;
		this.userName = userName;
	}

	@Transient
	private Set<String> permissionCodes = null;
	
	@Transient
	private void initialization(){
		if(permissionCodes==null){
			//initiate the lookup set
			String[] ps = StringUtils.delimitedListToStringArray(this.getPermission(), ",");
			this.permissionCodes = new HashSet<String>(ps.length);
			for(int i=0;i<ps.length;i++){
				this.permissionCodes.add(ps[i]);
			}
		}
	}
	@Transient
	public void addPermissionCode(String pc){
		this.initialization();
		if(!this.permissionCodes.contains(pc)){
			this.permissionCodes.add(pc);
			//sync the string
			this.setPermission(StringUtils.collectionToDelimitedString(this.permissionCodes, ","));
		}
	}
	
	@Transient
	public void removePermissionCode(String pc){
		this.initialization();
		if(this.permissionCodes.contains(pc)){
			this.permissionCodes.remove(pc);
			//sync the string
			this.setPermission(StringUtils.collectionToDelimitedString(this.permissionCodes, ","));
		}
	}
	
	@Transient
	public boolean hasPermissionCode(String pc){
		this.initialization();
		return this.permissionCodes.contains(pc);
	}
	
	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPermission() {
		return permission;
	}

	private void setPermission(String permission) {
		this.permission = permission;
	}



	public Long getId() {
		return id;
	}

}
