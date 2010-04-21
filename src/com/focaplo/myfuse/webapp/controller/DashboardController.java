package com.focaplo.myfuse.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.GrantService;
import com.focaplo.myfuse.service.InventoryService;
import com.focaplo.myfuse.service.OrderService;
import com.focaplo.myfuse.service.ProjectService;
import com.focaplo.myfuse.service.UserService;

public class DashboardController implements Controller {
	Logger log = LogManager.getLogger(this.getClass());
	private OrderService orderManager;
	private UserService userManager;
	
	public void setOrderManager(OrderService orderManager) {
		this.orderManager = orderManager;
	}
	
	public void setUserManager(UserService userManager) {
		this.userManager = userManager;
	}

	private InventoryService inventoryManager;
	public void setInventoryManager(InventoryService inventoryManager) {
		this.inventoryManager = inventoryManager;
	}
	private GrantService grantManager;
	public void setGrantManager(GrantService grantManager) {
		this.grantManager = grantManager;
	}
	private ProjectService projectManager;
	public void setProjectManager(ProjectService projectManager) {
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
