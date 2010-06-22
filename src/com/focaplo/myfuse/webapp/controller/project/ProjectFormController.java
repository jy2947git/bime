package com.focaplo.myfuse.webapp.controller.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
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
import com.focaplo.myfuse.service.InventoryService;
import com.focaplo.myfuse.service.ProjectService;
import com.focaplo.myfuse.service.UserService;
import com.focaplo.myfuse.webapp.controller.BimeFormController;
import com.focaplo.myfuse.webapp.support.UserConverter;
import com.focaplo.myfuse.webapp.util.RequestUtil;
@Controller
public class ProjectFormController extends BimeFormController {
	@Autowired
	private UserConverter userConverter;
	
	public void setUserConverter(UserConverter userConverter) {
		this.userConverter = userConverter;
	}


	@RequestMapping(value="/project/{projectId}/form.html", method=RequestMethod.POST)
	public String submitForm(@PathVariable("projectId") Long originalId, @ModelAttribute("managedProject") ManagedProject project, BindingResult result, @RequestParam(value="newVersion", required=false, defaultValue="false") Boolean newVersion, HttpServletRequest request, SessionStatus sessionStatus, Locale locale)throws Exception {

	
		if(project.getId()!=null){
			//only PI, which has authorize permission can edit existing project basic data. Although anybody can
			//create new project...
			this.checkAccess(ManagedProject.class, project.getId(), Securable.authorize);
		}
		
		//make sure PI is in participants
		if(!project.getParticipants().contains(project.getPrincipal())){
			project.getParticipants().add(project.getPrincipal());
		}
		//add the login user to the project by default, otherwise, it may be locked out of project if it is not PI
		if(originalId==null){
			//new project
			project.getParticipants().add(this.getLoginUser());
		}
        this.projectManager.saveProject(project);
        this.expireCachedObjects(ManagedProject.class, project.getId());
        saveMessage(request, getText("project.saved",project.getName(), locale));
        if(originalId==0){
        	//back to project form page to display the project detail, sub-menu
        	return "redirect:/project/" + project.getId()+"/form.html";
        }else{
        	return "redirect:/projects/list.html";
        }
	}
	@RequestMapping(value="/project/{projectId}/form.html", method=RequestMethod.GET)
	public String showForm(@PathVariable("projectId") Long projectId, Model model, Locale locale){
		model.addAttribute("userLabelList", super.getLabelValueListOfUsers());
		model.addAttribute("projectStatusLabelList", this.getProjectStatusLabelList(locale));
		model.addAttribute("projectId", projectId);
        return "/project/projectForm";
		
	}
	
	@ModelAttribute("managedProject")
	public ManagedProject getManagedProject(@PathVariable("projectId") Long projectId,  HttpServletRequest request)
    throws Exception {
		if (projectId != null && projectId.intValue()>0) {
        	this.checkAccess(ManagedProject.class, projectId, Securable.edit);
		}
        ManagedProject project = (ManagedProject) super.getModelObject(ManagedProject.class, projectId, "get".equalsIgnoreCase(request.getMethod()));
            if (projectId != null && projectId.intValue()>0) {
            
            	this.authorizationManager.injectResourcePermissionForUser(project, ManagedProject.class, this.getLoginUser().getUsername());
            } else {
                project.setPrincipal(this.getLoginUser());
            }
            return project;
		
		
	}
	
	@ModelAttribute("userList")
	protected List<User> getUsers(){
		return super.getLabUsers();

	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		
		binder.registerCustomEditor(com.focaplo.myfuse.model.User.class, this.userConverter);
		
	}
	

	
	private List getProjectStatusLabelList(Locale locale) {
		List<LabelValue> data = new ArrayList<LabelValue>();
		
		data.add(new LabelValue(this.getText("project.active", locale), Constants.PROJECT_STATUS_ACTIVE));
		data.add(new LabelValue(this.getText("project.pending", locale), Constants.PROJECT_STATUS_PENDING));
		data.add(new LabelValue(this.getText("project.finished", locale), Constants.PROJECT_STATUS_FINISHED));
		
		return data;
	}


}
