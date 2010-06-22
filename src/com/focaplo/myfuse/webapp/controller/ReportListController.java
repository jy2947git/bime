package com.focaplo.myfuse.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.Constants;
import com.focaplo.myfuse.model.User;
@Controller
public class ReportListController extends BimeListController {

	@RequestMapping(value="/project/reportList.html", method=RequestMethod.GET)
	public String displayList(@RequestParam(value="id", required=false) Long parentId, Model model){
		model.addAttribute( this.getListAttributeName(),super.getListOfSecuredObjects(parentId));
		return "project/reportList";
	}
	
	


	@Override
	public Class getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
