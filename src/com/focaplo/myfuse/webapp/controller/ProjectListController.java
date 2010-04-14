package com.focaplo.myfuse.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.ManagedProject;

public class ProjectListController extends BaseListController {

	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		req.getSession().removeAttribute("projectId");
		return super.handleRequest(req, res);
	}

	@Override
	public Class getModelClass() {
		return ManagedProject.class;
	}





}
