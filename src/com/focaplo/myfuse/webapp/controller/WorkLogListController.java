package com.focaplo.myfuse.webapp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.focaplo.myfuse.model.WorkLog;
import com.focaplo.myfuse.service.ProjectManager;

public class WorkLogListController extends BaseListController{

	@Override
	protected List getListOfModels(HttpServletRequest req) {
		String toDoId = req.getParameter("toDoId");
		return this.projectManager.getWorkLogsOfToDo(new Long(toDoId));
	}

	@Autowired
	private ProjectManager projectManager;
	
	
	public void setProjectManager(ProjectManager projectManager) {
		this.projectManager = projectManager;
	}

	
	@Override
	public Class getModelClass() {
		return WorkLog.class;
	}



}
