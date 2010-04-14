package com.focaplo.myfuse.webapp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.focaplo.myfuse.model.LabMeetingItem;
import com.focaplo.myfuse.service.LabManager;

public class MeetingItemListController extends BaseListController {
	@Autowired
	private LabManager labManager;
	
	public void setLabManager(LabManager labManager) {
		this.labManager = labManager;
	}

	@Override
	public Class getModelClass() {
		return LabMeetingItem.class;
	}

	@Override
	protected List getListOfModels(HttpServletRequest req) {
		Long meetingId = (Long)req.getSession().getAttribute("meetingId");
		return this.labManager.getMeetingItemsOfMeeting(meetingId);
	}

}
