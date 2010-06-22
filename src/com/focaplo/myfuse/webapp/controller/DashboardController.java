package com.focaplo.myfuse.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.focaplo.myfuse.model.User;
@Controller(value="dashboardController")
public class DashboardController extends BimeFormController{

	@RequestMapping("/dashboard/my.html")
	public String handleRequest(Model model) throws Exception {

		User user = this.getLoginUser();

		model.addAttribute("myMeetingList", this.labManager.getMeetingsInviting(user.getId()));
		model.addAttribute("myGrantList", this.grantManager.getGrantsAccessableTo(user.getId()));
		model.addAttribute("myProjectList",this.projectManager.getProjectAccessibleTo(user.getId()));
		model.addAttribute("mySubmittedOrderList",this.orderManager.getOrdersSubmittedBy(user.getId()));
		model.addAttribute("myForApprovalOrderList",this.orderManager.getOrdersForApproval(user.getId()));
		return "dashboard/my";
	}

}
