package com.focaplo.myfuse.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.focaplo.myfuse.Constants;
import com.focaplo.myfuse.model.ExperimentNote;
import com.focaplo.myfuse.model.WorkMonthlyPlan;
import com.focaplo.myfuse.model.WorkPlan;
import com.focaplo.myfuse.service.ProjectManager;

public class WorkPlanListController implements Controller {

	Logger log = LogManager.getLogger(this.getClass());
	private ProjectManager projectManager;
	
	
	public void setProjectManager(ProjectManager projectManager) {
		this.projectManager = projectManager;
	}

	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		if(req.getParameter("requestedMethod")!=null){
			this.handleMethod(req.getParameter("requestedMethod"),req);
			return new ModelAndView("redirect:monthlyWorkPlanList.html");
		}
		Long id = (Long)req.getSession().getAttribute("projectId");
		return new ModelAndView("project/monthlyWorkplanList", Constants.WORKPLAN_LIST, this.projectManager.getWorkPlanOfProject(id));
	}

	private void handleMethod(String methodParameter, HttpServletRequest req) {
		if(methodParameter.equalsIgnoreCase("delete")){
			String[] selectedIds = req.getParameterValues("selected");
			List<Long> toBeDeleted = new ArrayList<Long>();
			for(int i=0;i<selectedIds.length;i++){
				log.info("selected to delete " + selectedIds[i]);
				toBeDeleted.add(new Long(selectedIds[i]));
			}
			//
			this.projectManager.deleteWorkPlans(toBeDeleted);
		}
		//redirect to list
		
	}

}
