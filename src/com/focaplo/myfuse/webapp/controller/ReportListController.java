package com.focaplo.myfuse.webapp.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.focaplo.myfuse.Constants;
import com.focaplo.myfuse.model.User;

public class ReportListController extends UserController implements Controller {

	public ModelAndView handleRequest(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		return new ModelAndView("inventory/refrigerator", Constants.USER_LIST, this.userManager.getUsers(new User()));
	}

}
