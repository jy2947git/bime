package com.focaplo.myfuse.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.LabMeeting;

public class MeetingListController extends BaseListController {
	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		req.getSession().removeAttribute("meetingId");
		return super.handleRequest(req, res);
	}
	@Override
	public Class getModelClass() {
		return LabMeeting.class;
	}


}
