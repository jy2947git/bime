package com.focaplo.myfuse.webapp.controller.lab;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.focaplo.myfuse.model.LabMeeting;
import com.focaplo.myfuse.webapp.controller.BimeListController;
@Controller
public class MeetingListController extends BimeListController {

	@Override
	public Class getModelClass() {
		return LabMeeting.class;
	}


	@RequestMapping(value="/lab/meetings/list.html", method=RequestMethod.GET)
	public String displayList(Model model){

		model.addAttribute( this.getListAttributeName(),super.getListOfSecuredObjects(null));
		return "/lab/meetingList";
	}
	
	
	@RequestMapping(value="/lab/meeting/{meetingId}/delete.html", method=RequestMethod.POST)
	public String delete(@PathVariable("meetingId") Long meetingId, Model model){
		super.deleteSelectedObject(meetingId);
		model.addAttribute(this.getListAttributeName(), super.getListOfSecuredObjects(null));
		return "lab/include/include_meetingListTable";

	}
}
