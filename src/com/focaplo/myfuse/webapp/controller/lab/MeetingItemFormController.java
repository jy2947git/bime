package com.focaplo.myfuse.webapp.controller.lab;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.Equipment;
import com.focaplo.myfuse.model.LabMeeting;
import com.focaplo.myfuse.model.LabMeetingItem;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.webapp.controller.BimeFormController;
import com.focaplo.myfuse.webapp.support.UserConverter;
@Controller
public class MeetingItemFormController extends BimeFormController {
	
	@Autowired
	private UserConverter userConverter;

	public void setUserConverter(UserConverter userConverter) {
		this.userConverter = userConverter;
	}

	
	@RequestMapping(value="/lab/meeting/{meetingId}/item/{itemId}/form.html", method=RequestMethod.POST)
	public String submitForm(@PathVariable("meetingId") Long meetingId, @PathVariable("itemId") Long itemId, @ModelAttribute("labMeetingItem") LabMeetingItem meetingItem, BindingResult result, HttpServletRequest request, SessionStatus sessionStatus, Locale locale)throws Exception {

        Integer originalVersion = meetingItem.getVersion();

        this.labManager.save(meetingItem);
        this.expireCachedObjects(LabMeetingItem.class, meetingItem.getId());
       saveMessage(request, getText("meetingItem.saved", meetingItem.getId().toString(), locale));

        return "redirect:/lab/meeting/"+meetingId+"/items/list.html";
        

	}
	
	@ModelAttribute("labMeetingItem")
	public LabMeetingItem getMeetingItem(@PathVariable("meetingId") Long meetingId, @PathVariable("itemId") Long itemId,  HttpServletRequest request)
    throws Exception {

           
            LabMeetingItem meetingItem = (LabMeetingItem) super.getModelObject(LabMeetingItem.class, itemId, "get".equalsIgnoreCase(request.getMethod()));
            if (itemId!=null && itemId.longValue()>0) {
            	
            } else {
                meetingItem.setMeeting((LabMeeting)super.getModelObject(LabMeeting.class, meetingId, "get".equalsIgnoreCase(request.getMethod())));
            }
            return meetingItem;

	}
	
	@RequestMapping(value="/lab/meeting/{meetingId}/item/{itemId}/form.html", method=RequestMethod.GET)
	public String showForm(@PathVariable("meetingId") Long meetingId, @PathVariable("itemId") Long itemId, Model model){
		model.addAttribute("meetingId", meetingId);
        return "/lab/meetingItemForm";
	
}
	@ModelAttribute("userList")
	protected List<User> getUsers(){
		return super.getLabUsers();

	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		
		binder.registerCustomEditor(com.focaplo.myfuse.model.User.class, this.userConverter);
		
	}
}
