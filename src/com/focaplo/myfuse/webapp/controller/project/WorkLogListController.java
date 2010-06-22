package com.focaplo.myfuse.webapp.controller.project;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.focaplo.myfuse.model.BaseObject;
import com.focaplo.myfuse.model.WorkLog;
import com.focaplo.myfuse.webapp.controller.BimeListController;
import com.focaplo.myfuse.webapp.controller.SecondLevelListable;
@Controller
public class WorkLogListController extends BimeListController implements SecondLevelListable{
	
	@Override
	public Class getModelClass() {
		return WorkLog.class;
	}


	@RequestMapping(value="/project/{projectId}/todo/{toDoId}/worklogs/list.html", method=RequestMethod.GET)
	public String displayList(@PathVariable("projectId") Long projectId, @PathVariable("toDoId") Long toDoId, Model model){
		model.addAttribute( this.getListAttributeName(),super.getListOfSecuredObjects(toDoId));
		model.addAttribute("projectId",projectId);
		model.addAttribute("toDoId",toDoId);
		return "project/include/include_workLogListTable";
	}
	
	
	@RequestMapping(value="/project/{projectId}/todo/{toDoId}/worklog/{workLogId}/delete.html", method=RequestMethod.POST)
	public String delete(@PathVariable("projectId") Long projectId, @PathVariable("toDoId") Long toDoId, @PathVariable("workLogId") Long workLogId, Model model){
		this.deleteSelectedObject(workLogId);
		model.addAttribute( this.getListAttributeName(),super.getListOfSecuredObjects(toDoId));
		
		return "project/include/include_workLogListTable";

	}


	public List<? extends BaseObject> getListOfChildModels(Long toDoId) {
		return this.projectManager.getWorkLogsOfToDo(toDoId);
	}
}
