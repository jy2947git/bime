package com.focaplo.myfuse.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.focaplo.myfuse.exception.UnauthorizedAccessException;
import com.focaplo.myfuse.model.BaseObject;
import com.focaplo.myfuse.model.Securable;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.AuthorizationService;
import com.focaplo.myfuse.service.UniversalService;

public abstract class BaseListController implements Controller {
	@Autowired
	@Qualifier(value="manager")
	UniversalService manager;
	Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	AuthorizationService authorizationManager;
	
	public void setAuthorizationManager(AuthorizationService authorizationManager) {
		this.authorizationManager = authorizationManager;
	}


	private String includedListView;
	private String listView;
	
	public void setIncludedListView(String includedListView) {
		this.includedListView = includedListView;
	}

	public void setListView(String listView) {
		this.listView = listView;
	}

	public abstract Class getModelClass();

	protected String getListAttributeName(){
		String simpleClassName = this.getModelClass().getName().substring(this.getModelClass().getName().lastIndexOf(".")+1);
		return simpleClassName.substring(0, 1).toLowerCase() + simpleClassName.substring(1) + "List";
		
	}
	public void setManager(UniversalService universalManager) {
		this.manager = universalManager;
	}
	
	@SuppressWarnings("unchecked")
	protected List getListOfModels(HttpServletRequest req){
		List<BaseObject> objects = this.manager.getAll(this.getModelClass());
		return objects;
	}

	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		if(req.getParameter("requestedMethod")!=null){
			return this.handleMethod(req.getParameter("requestedMethod"),req);
			
		}
		List objects = this.getListOfModels(req);
		if(objects!=null && (!objects.isEmpty()) && (objects.get(0) instanceof Securable)){
			log.debug("secured resources, inject the permission to display the list");
			User loginUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			this.authorizationManager.injectResourceCollectionsPermissionForUser(objects, this.getModelClass(), loginUser.getUsername());
		}
		return new ModelAndView(this.listView, this.getListAttributeName(),objects);

	}
	
	protected void deleteModels(Class modelClass, List<Long> toBeDeleted){
		if(toBeDeleted!=null && (!toBeDeleted.isEmpty())){
			User loginUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			this.checkAccess(modelClass, toBeDeleted.get(0), Securable.delete);
			this.manager.deleteEntities(modelClass, toBeDeleted);
		}
		
	}
	
	
	protected ModelAndView handleMethod(String methodParameter, HttpServletRequest req) {
		if(methodParameter.equalsIgnoreCase("delete")){
			
			String[] selectedIds = req.getParameterValues("selected");
			
			List<Long> toBeDeleted = new ArrayList<Long>();
			for(int i=0;i<selectedIds.length;i++){
				log.info("selected to delete " + selectedIds[i]);
				toBeDeleted.add(new Long(selectedIds[i]));
			}
			//
			this.deleteModels(this.getModelClass(), toBeDeleted);
		//redirect to list
			return new ModelAndView(this.includedListView, this.getListAttributeName(), this.getListOfModels(req));
		}else{
			throw new IllegalArgumentException("unsupported method " + methodParameter);
		}
	}
	
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
	
    public User getLoginUser(){
    	return (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
