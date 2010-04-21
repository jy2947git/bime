package com.focaplo.myfuse.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.focaplo.myfuse.model.LabMeeting;
import com.focaplo.myfuse.service.LabService;

public class MeetingPrintController implements Controller {
	@Autowired
	private LabService labManager;
	
	public void setLabManager(LabService labManager) {
		this.labManager = labManager;
	}

	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		//get meeting id
		Long id = (Long)req.getSession().getAttribute("meetingId");
		LabMeeting meeting = (LabMeeting) this.labManager.get(LabMeeting.class, id);
		return new ModelAndView("lab/include/include_meetingPrint", "labMeeting", meeting);
	}
}