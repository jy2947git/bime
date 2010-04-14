package com.focaplo.myfuse.webapp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.ExperimentNote;
import com.focaplo.myfuse.service.ProjectManager;

public class ExperimentNoteListController extends BaseListController {

	@Autowired
	private ProjectManager projectManager;
	
	
	public void setProjectManager(ProjectManager projectManager) {
		this.projectManager = projectManager;
	}


	@Override
	protected List getListOfModels(HttpServletRequest req) {
		
		Long projectId = (Long)req.getSession().getAttribute("projectId");
		return this.projectManager.getNotesOfProject(projectId);
	}


	@Override
	public Class getModelClass() {
		return ExperimentNote.class;
	}

}
