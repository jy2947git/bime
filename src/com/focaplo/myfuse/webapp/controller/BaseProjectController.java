package com.focaplo.myfuse.webapp.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.focaplo.myfuse.model.ManagedProject;
import com.focaplo.myfuse.service.ProjectManager;
@Controller
public class BaseProjectController{
	Logger log = LogManager.getLogger(this.getClass());
	@Autowired
	private ProjectManager projectManager;
	
	
	public void setProjectManager(ProjectManager projectManager) {
		this.projectManager = projectManager;
	}	
	
	protected Long getProjectId(HttpServletRequest req){
		Long id = (Long)req.getSession().getAttribute("projectId");
		return id;
	}
	
	protected ManagedProject getCurrentProject(HttpServletRequest req){
		return (ManagedProject) this.projectManager.get(ManagedProject.class, this.getProjectId(req));
	}
}
