package com.focaplo.myfuse.webapp.controller.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.focaplo.myfuse.model.BaseObject;
import com.focaplo.myfuse.model.ExperimentNote;
import com.focaplo.myfuse.service.ProjectService;
import com.focaplo.myfuse.webapp.controller.BimeListController;
import com.focaplo.myfuse.webapp.controller.SecondLevelListable;
@Controller(value="experimentNoteListController")
public class ExperimentNoteListController extends BimeListController  implements SecondLevelListable{




	public List<? extends BaseObject> getListOfChildModels(Long projectId) {
		return this.projectManager.getNotesOfProject(projectId);
	}


	@Override
	public Class getModelClass() {
		return ExperimentNote.class;
	}

	@RequestMapping(value="/project/{projectId}/notes/list.html",method=RequestMethod.GET)
	public String displayList(@PathVariable(value="projectId") Long projectId, Model model){
		model.addAttribute( this.getListAttributeName(),super.getListOfSecuredObjects(projectId));
		model.addAttribute("projectId", projectId);
		return "/project/noteList";
	}
	
	
	@RequestMapping(value="/project/{projectId}/note/{noteId}/delete.html",method=RequestMethod.POST)
	public String delete(@PathVariable(value="projectId") Long projectId,@PathVariable("noteId") Long noteId, Model model){
		super.deleteSelectedObject(noteId);
		model.addAttribute(this.getListAttributeName(), super.getListOfSecuredObjects(projectId));
		model.addAttribute("projectId", projectId);
		return "/project/include/include_noteListTable";

	}
}
