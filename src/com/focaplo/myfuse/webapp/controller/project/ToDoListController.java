package com.focaplo.myfuse.webapp.controller.project;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.focaplo.myfuse.model.BaseObject;
import com.focaplo.myfuse.model.Securable;
import com.focaplo.myfuse.model.ToDo;
import com.focaplo.myfuse.webapp.controller.BimeListController;
import com.focaplo.myfuse.webapp.controller.SecondLevelListable;
@Controller
public class ToDoListController extends BimeListController implements SecondLevelListable{

	@Override
	public Class getModelClass() {
		return ToDo.class;
	}

	@RequestMapping(value="/project/{projectId}/todos/list.html", method=RequestMethod.GET)
	public String displayList(@PathVariable("projectId") Long projectId, Model model){
		this.checkAccess(this.getModelClass(), projectId, Securable.edit);
		model.addAttribute( this.getListAttributeName(),super.getListOfSecuredObjects(projectId));
		model.addAttribute("projectId",projectId);
		return "project/toDoList";
	}
	
	
	@RequestMapping(value="/project/{projectId}/todo/{toDoId}/delete.html", method=RequestMethod.POST)
	public String delete(@PathVariable(value="projectId") Long projectId, @PathVariable("toDoId") Long toDoId, Model model){
		this.deleteSelectedObject(toDoId);
		model.addAttribute( this.getListAttributeName(),super.getListOfSecuredObjects(projectId));		
		return "project/include/include_toDoListTable";

	}



	public List<? extends BaseObject> getListOfChildModels(Long projectId) {
		return this.projectManager.getToDoOfProject(projectId);
	}
}
