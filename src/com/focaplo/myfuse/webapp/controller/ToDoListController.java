package com.focaplo.myfuse.webapp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.focaplo.myfuse.model.Securable;
import com.focaplo.myfuse.model.ToDo;
import com.focaplo.myfuse.service.ProjectManager;

public class ToDoListController extends BaseListController{
	Logger log = LogManager.getLogger(this.getClass());
	@Autowired
	private ProjectManager projectManager;
	
	
	public void setProjectManager(ProjectManager projectManager) {
		this.projectManager = projectManager;
	}


	@Override
	protected List getListOfModels(HttpServletRequest req)  {
		Long id = (Long)req.getSession().getAttribute("projectId");
		this.checkAccess(this.getModelClass(), id, Securable.edit);
		return this.projectManager.getToDoOfProject(id);
	}
	@Override
	public Class getModelClass() {
		return ToDo.class;
	}

}
