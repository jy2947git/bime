package com.focaplo.myfuse.webapp.controller.lab;

import java.util.ArrayList;
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
import com.focaplo.myfuse.model.LabelValue;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.webapp.controller.BimeFormController;
import com.focaplo.myfuse.webapp.support.UserConverter;
@Controller
public class MeetingFormController extends BimeFormController {
	
	@Autowired
	private UserConverter userConverter;
	
	public void setUserConverter(UserConverter userConverter) {
		this.userConverter = userConverter;
	}

	
	@RequestMapping(value="/lab/meeting/{meetingId}/form.html", method=RequestMethod.POST)
	public String submitForm(@PathVariable("meetingId") Long meetingId, @ModelAttribute("labMeeting") LabMeeting meeting, BindingResult result, HttpServletRequest request, SessionStatus sessionStatus, Locale locale)throws Exception {

		String[] accessUserIds = request.getParameterValues("accessUserIds");
    	log.debug("there are " + accessUserIds);
    	Long originalId = meeting.getId();
    	meeting.getParticipants().clear();
    	if(accessUserIds!=null){
        	for(String userId:accessUserIds){
        		meeting.getParticipants().add(this.userManager.getUser(userId));
        	}
    	}
        this.labManager.save(meeting);
        this.expireCachedObjects(LabMeeting.class, meetingId);
        saveMessage(request, getText("meeting.saved",meeting.getSubject(), locale));
        if(originalId==null){
        	//new meeting just created, back to form
        	return "redirect:/lab/meeting/" + meeting.getId()+"/form.html";
        }else{
        	return "redirect:/lab/meetings/list.html";
        }
	}
	
	@RequestMapping(value="/lab/meeting/{meetingId}/form.html", method=RequestMethod.GET)
	public String showForm(@PathVariable("meetingId") Long meetingId, Model model){
        //for the sub-menu
        model.addAttribute("meetingId", meetingId);
		model.addAttribute("userLabelList",super.getLabelValueListOfUsers());
		model.addAttribute("hourAndMinuteLabelList", this.createHourAndMinuteLabelList());
        return "/lab/meetingForm";
		
	}
	
	@ModelAttribute("labMeeting")
	public LabMeeting getMeeting(@PathVariable("meetingId") Long meetingId,  HttpServletRequest request)
    throws Exception {
		LabMeeting meeting = null;
		meeting = (LabMeeting) super.getModelObject(LabMeeting.class, meetingId, "get".equalsIgnoreCase(request.getMethod()));
		if(meetingId!=null && meetingId.longValue()>0){
			
		} else {
           meeting.setCoordinator(this.getLoginUser());
           meeting.getParticipants().add(this.getLoginUser());
         }
        return meeting;
		
		
	}
	
	
	private List<LabelValue> createHourAndMinuteLabelList() {
		List<LabelValue> refList = new ArrayList<LabelValue>();
		for(int i=0;i<24;i++){
			for(int j=0;j<4;j++){
				refList.add(new LabelValue(StringUtils.leftPad(Integer.toString(i),2,"0")+":"+StringUtils.leftPad(Integer.toString(j*15),2,"0"), i+":"+j*15));
			}
		}
		return refList;
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
