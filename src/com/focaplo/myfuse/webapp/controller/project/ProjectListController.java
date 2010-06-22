package com.focaplo.myfuse.webapp.controller.project;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.focaplo.myfuse.model.ManagedProject;
import com.focaplo.myfuse.webapp.controller.BimeListController;
@Controller
public class ProjectListController extends BimeListController {

	@Override
	public Class getModelClass() {
		return ManagedProject.class;
	}



	@RequestMapping(value="/projects/list.html", method=RequestMethod.GET)
	public String displayList(Model model){
		model.addAttribute( this.getListAttributeName(),super.getListOfSecuredObjects(null));
		return "/project/projectList";
	}
	
	
	@RequestMapping(value="/project/{projectId}/delete.html", method=RequestMethod.POST)
	public String delete(@PathVariable(value="projectId") Long projectId, Model model){
		super.deleteSelectedObject(projectId);
		model.addAttribute(this.getListAttributeName(), super.getListOfSecuredObjects(null));
		return "project/include/include_projectListTable";

	}


}
