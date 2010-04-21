package com.focaplo.myfuse.webapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.LabMeeting;
import com.focaplo.myfuse.model.LabMeetingItem;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.LabService;
import com.focaplo.myfuse.service.UserService;
import com.focaplo.myfuse.webapp.support.UserConverter;

public class MeetingItemFormController extends BaseFormController {
	@Autowired
	private LabService labManager;
	@Autowired
	private UserService userManager;
	@Autowired
	private UserConverter userConverter;
	public void setUserManager(UserService userManager) {
		this.userManager = userManager;
	}


	public void setUserConverter(UserConverter userConverter) {
		this.userConverter = userConverter;
	}


	public void setLabManager(LabService labManager) {
		this.labManager = labManager;
	}


	public MeetingItemFormController() {
		super();
		setCommandName("labMeetingItem");
	    setCommandClass(LabMeetingItem.class);
		
	}

	
	public ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command,
            BindException errors)throws Exception {
		log.debug("entering 'onSubmit' method...");
		LabMeetingItem meetingItem = (LabMeetingItem)command;
		Locale locale = request.getLocale();


        Integer originalVersion = meetingItem.getVersion();

        this.labManager.save(meetingItem);
       saveMessage(request, getText("meetingItem.saved", meetingItem.getId().toString(), locale));

       return new ModelAndView(getSuccessView());

	}
	
	protected Object formBackingObject(HttpServletRequest request)
    throws Exception {

            String id = request.getParameter("id");
            LabMeetingItem meetingItem;
            if (!StringUtils.isBlank(id)) {
            	meetingItem = (LabMeetingItem) this.labManager.get(LabMeetingItem.class, new Long(id));
            } else {
                meetingItem = new LabMeetingItem();
                Long meetingId = (Long) request.getSession().getAttribute("meetingId");
                meetingItem.setMeeting((LabMeeting) this.labManager.get(LabMeeting.class, meetingId));
            }
            return meetingItem;

	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		Map<String,List> map = new HashMap<String,List>();
		map.put("userList", this.userManager.getAllLabUsers());
		
	
		return map;
	}
	
	@Override
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) {
		
		super.initBinder(request, binder);
		binder.registerCustomEditor(com.focaplo.myfuse.model.User.class, this.userConverter);
		
	}
}
