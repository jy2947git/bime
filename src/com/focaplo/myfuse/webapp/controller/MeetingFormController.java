package com.focaplo.myfuse.webapp.controller;

import java.util.ArrayList;
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
import com.focaplo.myfuse.model.LabelValue;
import com.focaplo.myfuse.model.ManagedProject;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.LabManager;
import com.focaplo.myfuse.service.UserManager;
import com.focaplo.myfuse.webapp.support.UserConverter;

public class MeetingFormController extends BaseFormController {
	@Autowired
	private LabManager labManager;
	@Autowired
	private UserManager userManager;
	@Autowired
	private UserConverter userConverter;
	
	public void setUserConverter(UserConverter userConverter) {
		this.userConverter = userConverter;
	}

	public LabManager getLabManager() {
		return labManager;
	}

	public void setLabManager(LabManager labManager) {
		this.labManager = labManager;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public MeetingFormController() {
		super();
		this.setCommandName("labMeeting");
		this.setCommandClass(LabMeeting.class);
	}
	
	public ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command,
            BindException errors)throws Exception {
		log.debug("entering 'onSubmit' method...");
		LabMeeting meeting = (LabMeeting)command;
		Locale locale = request.getLocale();
		String[] accessUserIds = request.getParameterValues("accessUserIds");
    	log.debug("there are " + accessUserIds);
    	meeting.getParticipants().clear();
    	if(accessUserIds!=null){
        	for(String userId:accessUserIds){
        		meeting.getParticipants().add(this.userManager.getUser(userId));
        	}
    	}
        this.labManager.save(meeting);

        saveMessage(request, getText("meeting.saved",meeting.getSubject(), locale));

       return new ModelAndView(getSuccessView());
            
	}
	
	protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
		
		if(!StringUtils.isBlank(request.getParameter("id"))){
			log.debug("existing meeting "+ request.getParameter("id"));
			request.getSession().setAttribute("meetingId", new Long(request.getParameter("id")));
		}
		Long id = (Long)request.getSession().getAttribute("meetingId");
		
            LabMeeting meeting;
            if (id!=null) {
            	log.debug("loading existing meeting " + id);
            	meeting = (LabMeeting) this.labManager.get(LabMeeting.class, id);
            } else {
            	log.debug("creating new meeting...");
                meeting = new LabMeeting();
                meeting.setCoordinator(this.getLoginUser());
                meeting.getParticipants().add(this.getLoginUser());
            }
            return meeting;
		
		
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		Map<String,List> map = new HashMap<String,List>();
		map.put("userList", this.userManager.getAllLabUsers());
		map.put("userLabelList", this.userManager.getLabelValueListOfUsers());
		map.put("hourAndMinuteLabelList", this.createHourAndMinuteLabelList());
		return map;
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

	@Override
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) {
		
		super.initBinder(request, binder);
		binder.registerCustomEditor(com.focaplo.myfuse.model.User.class, this.userConverter);
		
	}
}
