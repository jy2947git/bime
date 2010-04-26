package com.focaplo.myfuse.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.focaplo.myfuse.Constants;
import com.focaplo.myfuse.model.Equipment;
import com.focaplo.myfuse.model.ManagedGrant;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.GrantService;
import com.focaplo.myfuse.service.InventoryService;

public class GrantListController implements Controller {
	@Autowired
private GrantService grantManager;

Logger log = LogManager.getLogger(this.getClass());
	

	
	public void setGrantManager(GrantService grantManager) {
	this.grantManager = grantManager;
}

	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		if(req.getParameter("requestedMethod")!=null){
			this.handleMethod(req.getParameter("requestedMethod"),req);
			return new ModelAndView("redirect:grantList.html");
		}
		return new ModelAndView("grant/grantList", Constants.GRANT_LIST, this.grantManager.getAll(ManagedGrant.class));
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
			this.grantManager.deleteGrants(toBeDeleted);
		}
		//redirect to list
		
	}


}
