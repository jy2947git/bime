package com.focaplo.myfuse.webapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.mail.MailException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.focaplo.myfuse.Constants;
import com.focaplo.myfuse.model.Equipment;
import com.focaplo.myfuse.model.ExperimentProtocol;
import com.focaplo.myfuse.model.LabelValue;
import com.focaplo.myfuse.model.ManagedProject;
import com.focaplo.myfuse.model.Role;
import com.focaplo.myfuse.model.Securable;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.InventoryManager;
import com.focaplo.myfuse.service.ProjectManager;
import com.focaplo.myfuse.service.UserManager;
import com.focaplo.myfuse.webapp.support.UserConverter;
import com.focaplo.myfuse.webapp.util.RequestUtil;

public class ProjectFormController extends BaseFormController {

	private ProjectManager projectManager;
	private UserManager userManager;
	private UserConverter userConverter;
	
	public void setUserConverter(UserConverter userConverter) {
		this.userConverter = userConverter;
	}



	public UserManager getUserManager() {
		return userManager;
	}



	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}



	public void setProjectManager(ProjectManager projectManager) {
		this.projectManager = projectManager;
	}



	public ProjectFormController() {
		super();
		setCommandName("managedProject");
	    setCommandClass(ManagedProject.class);
		
	}

	public ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command,
            BindException errors)throws Exception {
		log.debug("entering 'onSubmit' method...");
		
		
		ManagedProject project = (ManagedProject)command;
		if(project.getId()!=null){
			//only PI, which has authorize permission can edit existing project basic data. Although anybody can
			//create new project...
			this.checkAccess(this.getCommandClass(), project.getId(), Securable.authorize);
		}
		Locale locale = request.getLocale();
		//make sure PI is in participants
		if(!project.getParticipants().contains(project.getPrincipal())){
			project.getParticipants().add(project.getPrincipal());
		}
        this.projectManager.saveProject(project);

        saveMessage(request, getText("project.saved",project.getName(), locale));

       return new ModelAndView(getSuccessView());
            
	}
	
	protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
		
		if(!StringUtils.isBlank(request.getParameter("id"))){
			log.debug("existing project "+ request.getParameter("id"));
			request.getSession().setAttribute("projectId", new Long(request.getParameter("id")));
		}
		Long id = (Long)request.getSession().getAttribute("projectId");
		
            ManagedProject project;
            if (id != null) {
            	this.checkAccess(this.getCommandClass(), id, Securable.edit);
            	log.debug("loading existing project " + id);
            	project = (ManagedProject) this.projectManager.get(ManagedProject.class, new Long(id));
            	this.authorizationManager.injectResourcePermissionForUser(project, this.getCommandClass(), this.getLoginUser().getUsername());
            } else {
            	log.debug("creating new project...");
                project = new ManagedProject();
                project.setPrincipal(this.getLoginUser());
            }
            return project;
		
		
	}
	

	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		Map<String,List> map = new HashMap<String,List>();
		map.put("userList", this.userManager.getAllLabUsers());
		map.put("userLabelList", this.userManager.getLabelValueListOfUsers());
		map.put("projectStatusLabelList", this.getProjectStatusLabelList(request.getLocale()));
		return map;
	}
	
	private List getProjectStatusLabelList(Locale locale) {
		List<LabelValue> data = new ArrayList<LabelValue>();
		
		data.add(new LabelValue(this.getText("project.active", locale), Constants.PROJECT_STATUS_ACTIVE));
		data.add(new LabelValue(this.getText("project.pending", locale), Constants.PROJECT_STATUS_PENDING));
		data.add(new LabelValue(this.getText("project.finished", locale), Constants.PROJECT_STATUS_FINISHED));
		
		return data;
	}



	@Override
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) {
		
		super.initBinder(request, binder);
		binder.registerCustomEditor(com.focaplo.myfuse.model.User.class, this.userConverter);
		
	}
}
