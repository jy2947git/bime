package com.focaplo.myfuse.webapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.ManagedProject;
import com.focaplo.myfuse.model.Securable;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.ProjectManager;
import com.focaplo.myfuse.service.UserManager;

public class ProjectAccessFormController extends BaseFormController {
	private ProjectManager projectManager;
	private UserManager userManager;
	
	public void setProjectManager(ProjectManager projectManager) {
		this.projectManager = projectManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public ProjectAccessFormController() {
		super();
		setCommandName("managedProject");
	    setCommandClass(ManagedProject.class);
	}
	
	public ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command,
            BindException errors)throws Exception {
		
		log.debug("entering 'onSubmit' method...");
		ManagedProject project = (ManagedProject)command;
		this.checkAccess(ManagedProject.class, project.getId(), Securable.authorize);
		Locale locale = request.getLocale();
		String[] accessUserIds = request.getParameterValues("accessUserIds");
    	log.debug("there are " + accessUserIds);
    	project.getParticipants().clear();
    	if(accessUserIds!=null){
        	for(String userId:accessUserIds){
        		project.getParticipants().add(this.userManager.getUser(userId));
        	}
    	}
    	this.projectManager.saveProjectParticipants(project);
    	saveMessage(request, getText("project.access.saved", project.getName(), locale));

      return new ModelAndView(getSuccessView());
	}
	
	protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
		

            Long id = (Long)request.getSession().getAttribute("projectId");
            this.checkAccess(this.getCommandClass(), id, Securable.edit);
            ManagedProject project = (ManagedProject) this.projectManager.get(ManagedProject.class, new Long(id));
            this.authorizationManager.injectResourcePermissionForUser(project, ManagedProject.class, this.getLoginUser().getUsername());
            return project;
		
	}
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		Map<String,List> map = new HashMap<String,List>();
		
		map.put("userLabelList", this.userManager.getLabelValueListOfUsers());
	
		return map;
	}
}
