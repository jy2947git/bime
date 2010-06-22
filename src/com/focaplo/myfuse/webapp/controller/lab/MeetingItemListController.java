package com.focaplo.myfuse.webapp.controller.lab;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.focaplo.myfuse.model.BaseObject;
import com.focaplo.myfuse.model.LabMeetingItem;
import com.focaplo.myfuse.webapp.controller.BimeListController;
import com.focaplo.myfuse.webapp.controller.SecondLevelListable;
@Controller
public class MeetingItemListController extends BimeListController  implements SecondLevelListable{
	
	@Override
	public Class getModelClass() {
		return LabMeetingItem.class;
	}



	public List<? extends BaseObject> getListOfChildModels(Long meetingId) {
		return this.labManager.getMeetingItemsOfMeeting(meetingId);
	}

	@RequestMapping(value="/lab/meeting/{meetingId}/items/list.html", method=RequestMethod.GET)
	public String displayList(@PathVariable("meetingId") Long meetingId, Model model){
		model.addAttribute( this.getListAttributeName(),super.getListOfSecuredObjects(meetingId));
		model.addAttribute("meetingId", meetingId);
		return "/lab/meetingItemList";
	}
	
	
	@RequestMapping(value="/lab/meeting/{meetingId}/item/{itemId}/delete.html", method=RequestMethod.POST)
	public String delete(@PathVariable("meetingId") Long meetingId,@PathVariable("itemId") Long itemId, Model model){
		super.deleteSelectedObject(itemId);
		model.addAttribute(this.getListAttributeName(), super.getListOfSecuredObjects(meetingId));
		model.addAttribute("meetingId", meetingId);
		return "lab/include/include_meetingItemListTable";

	}
}
