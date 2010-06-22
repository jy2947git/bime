package com.focaplo.myfuse.webapp.controller.project;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.Equipment;
import com.focaplo.myfuse.model.ManagedProject;
import com.focaplo.myfuse.model.Securable;
import com.focaplo.myfuse.webapp.controller.BimeFormController;
@Controller
public class ProjectAccessFormController extends BimeFormController {

	
	@RequestMapping(value="/project/{projectId}/access/form.html", method=RequestMethod.POST)
	public String submitForm(@PathVariable("projectId") Long projectId, @ModelAttribute("managedProject") ManagedProject managedProject, BindingResult result, @RequestParam(value="newVersion", required=false, defaultValue="false") Boolean newVersion, HttpServletRequest request, SessionStatus sessionStatus, Locale locale)throws Exception {

		this.checkAccess(ManagedProject.class, managedProject.getId(), Securable.authorize);
		
		String[] accessUserIds = request.getParameterValues("accessUserIds");
    	log.debug("there are " + accessUserIds);
    	managedProject.getParticipants().clear();
    	if(accessUserIds!=null){
        	for(String userId:accessUserIds){
        		managedProject.getParticipants().add(this.userManager.getUser(userId));
        	}
    	}
    	this.projectManager.saveProjectParticipants(managedProject);
    	this.expireCachedObjects(ManagedProject.class, managedProject.getId());
    	saveMessage(request, getText("project.access.saved", managedProject.getName(), locale));

      return "redirect:/project/" + managedProject.getId()+"/access/form.html";
	}
	
	@RequestMapping(value="/project/{projectId}/access/form.html", method=RequestMethod.GET)
	public String showForm(@PathVariable("projectId") Long projectId, Model model){
		model.addAttribute("userLabelList", super.getLabelValueListOfUsers());
		model.addAttribute("projectId", projectId);
		return "/project/projectAccessForm";
	}
	
	@ModelAttribute("managedProject")
	public ManagedProject getManagedProject(@PathVariable("projectId") Long projectId,  HttpServletRequest request)throws Exception {
            this.checkAccess(ManagedProject.class, projectId, Securable.edit);
            ManagedProject project = (ManagedProject)super.getModelObject(ManagedProject.class, new Long(projectId), "get".equalsIgnoreCase(request.getMethod()));
            this.authorizationManager.injectResourcePermissionForUser(project, ManagedProject.class, this.getLoginUser().getUsername());
            return project;
		
	}
	
}
