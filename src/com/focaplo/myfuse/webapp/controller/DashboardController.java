package com.focaplo.myfuse.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.GrantManager;
import com.focaplo.myfuse.service.InventoryManager;
import com.focaplo.myfuse.service.OrderManager;
import com.focaplo.myfuse.service.ProjectManager;
import com.focaplo.myfuse.service.UserManager;

public class DashboardController implements Controller {
	Logger log = LogManager.getLogger(this.getClass());
	private OrderManager orderManager;
	private UserManager userManager;
	
	public void setOrderManager(OrderManager orderManager) {
		this.orderManager = orderManager;
	}
	
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	private InventoryManager inventoryManager;
	public void setInventoryManager(InventoryManager inventoryManager) {
		this.inventoryManager = inventoryManager;
	}
	private GrantManager grantManager;
	public void setGrantManager(GrantManager grantManager) {
		this.grantManager = grantManager;
	}
	private ProjectManager projectManager;
	public void setProjectManager(ProjectManager projectManager) {
		this.projectManager = projectManager;
	}
	public ModelAndView handleRequest(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		ModelAndView mv = new ModelAndView();
		String userName = arg0.getRemoteUser();
		User user = this.userManager.getUserByUsername(userName);
		mv.setViewName("dashboard/my");
		mv.addObject("myGrantList", this.grantManager.getGrantsAccessableTo(user.getId()));
		mv.addObject("myProjectList",this.projectManager.getProjectAccessibleTo(user.getId()));
		mv.addObject("mySubmittedOrderList",this.orderManager.getOrdersSubmittedBy(user.getId()));
		mv.addObject("myForApprovalOrderList",this.orderManager.getOrdersForApproval(user.getId()));
		return mv;
	}

}
