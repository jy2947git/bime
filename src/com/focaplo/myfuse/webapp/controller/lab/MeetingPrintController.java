package com.focaplo.myfuse.webapp.controller.lab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.focaplo.myfuse.model.LabMeeting;
import com.focaplo.myfuse.service.LabService;
@Controller
public class MeetingPrintController{
	@Autowired
	private LabService labManager;
	
	public void setLabManager(LabService labManager) {
		this.labManager = labManager;
	}

	@RequestMapping(value="/lab/meeting/{meetingId}/print.html", method=RequestMethod.GET)
	public String printMeeting(@PathVariable("meetingId") Long meetingId, Model model) throws Exception {
		//get meeting id
		
		LabMeeting meeting = (LabMeeting) this.labManager.get(LabMeeting.class, meetingId);
		model.addAttribute("labMeeting", meeting);
		return "lab/include/include_meetingPrint";
	}
}