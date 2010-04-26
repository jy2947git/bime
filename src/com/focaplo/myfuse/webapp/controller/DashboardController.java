package com.focaplo.myfuse.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.GrantService;
import com.focaplo.myfuse.service.InventoryService;
import com.focaplo.myfuse.service.LabService;
import com.focaplo.myfuse.service.OrderService;
import com.focaplo.myfuse.service.ProjectService;
import com.focaplo.myfuse.service.UserService;

public class DashboardController implements Controller {
	Logger log = LogManager.getLogger(this.getClass());
	@Autowired
	private OrderService orderManager;
	@Autowired
	private UserService userManager;
	@Autowired
	private LabService labManager;
	@Autowired
	private InventoryService inventoryManager;
	@Autowired
	private GrantService grantManager;
	@Autowired
	private ProjectService projectManager;
	public void setGrantManager(GrantService grantManager) {
		this.grantManager = grantManager;
	}
	
	public void setProjectManager(ProjectService projectManager) {
		this.projectManager = projectManager;
	}
	public void setOrderManager(OrderService orderManager) {
		this.orderManager = orderManager;
	}
	
	public void setUserManager(UserService userManager) {
		this.userManager = userManager;
	}

	public void setLabManager(LabService labManager) {
		this.labManager = labManager;
	}

	public void setInventoryManager(InventoryService inventoryManager) {
		this.inventoryManager = inventoryManager;
	}
	public ModelAndView handleRequest(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		ModelAndView mv = new ModelAndView();
		String userName = arg0.getRemoteUser();
		User user = this.userManager.getUserByUsername(userName);
		mv.setViewName("dashboard/my");
		mv.addObject("myMeetingList", this.labManager.getMeetingsInviting(user.getId()));
		mv.addObject("myGrantList", this.grantManager.getGrantsAccessableTo(user.getId()));
		mv.addObject("myProjectList",this.projectManager.getProjectAccessibleTo(user.getId()));
		mv.addObject("mySubmittedOrderList",this.orderManager.getOrdersSubmittedBy(user.getId()));
		mv.addObject("myForApprovalOrderList",this.orderManager.getOrdersForApproval(user.getId()));
		return mv;
	}

}
