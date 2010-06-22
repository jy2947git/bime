package com.focaplo.myfuse.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.focaplo.myfuse.exception.UnauthorizedAccessException;
import com.focaplo.myfuse.model.BaseObject;
import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.model.Securable;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.AuthorizationService;
import com.focaplo.myfuse.service.CacheService;
import com.focaplo.myfuse.service.GrantService;
import com.focaplo.myfuse.service.InventoryService;
import com.focaplo.myfuse.service.LabService;
import com.focaplo.myfuse.service.OrderService;
import com.focaplo.myfuse.service.ProjectService;
import com.focaplo.myfuse.service.RoleService;
import com.focaplo.myfuse.service.UniversalService;
import com.focaplo.myfuse.service.UserService;
import com.focaplo.myfuse.webapp.spring.ThreadBoundContext;

@Controller
public abstract class BimeListController {
	@Autowired
	@Qualifier(value="universalManager")
	protected UniversalService universalManager;
	protected Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	protected AuthorizationService authorizationManager;
	@Autowired
	protected ProjectService projectManager;
    @Autowired
    protected OrderService orderManager;
	@Autowired
	protected LabService labManager;

	@Autowired
	protected GrantService grantManager;
	@Autowired
	protected InventoryService inventoryManager;
	@Autowired
	protected UserService userManager;
	@Autowired
	protected RoleService roleManager;
	
	@Autowired
	@Qualifier("memcachedManager")
	protected CacheService cacheManager;
	
	public void setCacheManager(CacheService cacheManager) {
		this.cacheManager = cacheManager;
	}
	public void setRoleManager(RoleService roleManager) {
		this.roleManager = roleManager;
	}
	public void setUserManager(UserService userManager) {
		this.userManager = userManager;
	}
	public void setProjectManager(ProjectService projectManager) {
		this.projectManager = projectManager;
	}
	public void setAuthorizationManager(AuthorizationService authorizationManager) {
		this.authorizationManager = authorizationManager;
	}


	public void setOrderManager(OrderService orderManager) {
		this.orderManager = orderManager;
	}
	public void setLabManager(LabService labManager) {
		this.labManager = labManager;
	}
	public void setGrantManager(GrantService grantManager) {
		this.grantManager = grantManager;
	}
	public void setInventoryManager(InventoryService inventoryManager) {
		this.inventoryManager = inventoryManager;
	}
	public abstract Class<? extends BaseObject> getModelClass();

	protected String getListAttributeName(){
		String simpleClassName = this.getModelClass().getName().substring(this.getModelClass().getName().lastIndexOf(".")+1);
		return simpleClassName.substring(0, 1).toLowerCase() + simpleClassName.substring(1) + "List";
		
	}

	
	public void setUniversalManager(UniversalService universalManager) {
		this.universalManager = universalManager;
	}

	protected void expireCachedObjects(Class<? extends BaseObject> clazz, Long id) {
		//expire cache
		if(this.cacheManager!=null&&this.cacheManager.isOn()){
			this.cacheManager.delete(clazz.getSimpleName()+"-"+id);
			this.cacheManager.delete(clazz.getSimpleName()+"-list");
		}
		
	}
	
	@SuppressWarnings("unchecked")
	protected List getListOfModels(Long parentId){
		List<? extends BaseObject> objects = null;
		if(parentId==null){
			//use cache
			if(this.cacheManager!=null&&this.cacheManager.isOn()){
				objects  = (List<? extends BaseObject>) this.cacheManager.get(this.getModelClass().getSimpleName()+"-list");
			}
			if(objects==null){
				objects = this.universalManager.getAll(this.getModelClass());
				if(this.cacheManager!=null&&this.cacheManager.isOn()){
					this.cacheManager.set(this.getModelClass().getSimpleName()+"-list", CacheService.default_ttl_in_seconds, objects);
				}
			}
		}else{
			//the controller must implement the method to get models based on the parent id
			if(this instanceof SecondLevelListable){
				objects = ((SecondLevelListable)this).getListOfChildModels(parentId);
			}
		}
		return objects;
	}



	protected List getListOfSecuredObjects(Long parentId){
		List objects = this.getListOfModels(parentId);
		if(objects!=null && (!objects.isEmpty()) && (objects.get(0) instanceof Securable)){
			log.debug("secured resources, inject the permission to display the list");
			User loginUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			this.authorizationManager.injectResourceCollectionsPermissionForUser(objects, this.getModelClass(), loginUser.getUsername());
		}
		return objects;
	}

//	
//	protected void deleteModels(Class modelClass, List<Long> toBeDeleted){
//		if(toBeDeleted!=null && (!toBeDeleted.isEmpty())){
//			User loginUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			this.checkAccess(modelClass, toBeDeleted.get(0), Securable.delete);
//			this.universalManager.deleteEntities(modelClass, toBeDeleted);
//		}
//		
//	}
	
//	protected void deleteSelectedObjects(String[] selectedIds){
//		
//		List<Long> toBeDeleted = new ArrayList<Long>();
//		for(int i=0;i<selectedIds.length;i++){
//			log.info("selected to delete " + selectedIds[i]);
//			toBeDeleted.add(new Long(selectedIds[i]));
//		}
//		//
//		this.deleteModels(this.getModelClass(), toBeDeleted);
//	}
	
	
	protected void checkAccess(Class clazz, Long id, String permission)
	throws RuntimeException {

		Class[] interfaces = clazz.getInterfaces();
		boolean secured=false;
		for(int i=0;i<interfaces.length;i++){
			if(interfaces[i].getName().equalsIgnoreCase(Securable.class.getName())){
				//secured resource
				secured=true;
				break;
			}
		}
		if(secured){
			
			if(id!=null){
				if(!this.authorizationManager.getHasPermission(clazz, new Long(id), this.getLoginUser().getUsername(), permission)){
					throw new UnauthorizedAccessException("User " + this.getLoginUser().getUsername() + " tried to unauthorizaed " + permission + " access to " + clazz + " id " + id);
				}
			}else{
				log.warn("Resource " + clazz + " form controller doesn't have ID parameter!!!!");
			}
			
		}

	}
	
    protected User getLoginUser(){
    	return (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

	public void deleteSelectedObject(Long id) {
		//expire cache
		this.expireCachedObjects(this.getModelClass(), id);
		this.universalManager.remove(this.getModelClass(), id);
		
	}
	public Lab getCurrentLab() {
		String labName = ThreadBoundContext.getValue();
    	Lab lab = null;
    	if(this.cacheManager!=null&&this.cacheManager.isOn()){
    		lab = (Lab) this.cacheManager.get("lab-"+labName);
    	}
    	if(lab==null){
    		lab = this.labManager.getLabByName(labName);
    		if(this.cacheManager!=null&&this.cacheManager.isOn()){
    			this.cacheManager.set("lab-"+labName, CacheService.default_ttl_in_seconds, lab);
    		}
    	}
    	return lab;
	}
}
