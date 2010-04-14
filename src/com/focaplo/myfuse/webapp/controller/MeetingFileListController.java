package com.focaplo.myfuse.webapp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.model.MeetingFile;
import com.focaplo.myfuse.service.LabManager;
import com.focaplo.myfuse.service.StorageManager;
import com.focaplo.myfuse.webapp.spring.ThreadBoundContext;

public class MeetingFileListController extends BaseListController {
	@Autowired
	private LabManager labManager;
	@Autowired
	private StorageManager storageService;
	
	public void setLabManager(LabManager labManager) {
		this.labManager = labManager;
	}

	public void setStorageService(StorageManager storageService) {
		this.storageService = storageService;
	}

	@Override
	public Class getModelClass() {
		return MeetingFile.class;
	}
	@Override
	protected List getListOfModels(HttpServletRequest req) {
		Long meetingId = (Long)req.getSession().getAttribute("meetingId");
		return this.labManager.getMeetingFilesOfMeeting(meetingId);
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		//a empty fileupload
		FileUpload fp = new FileUpload();
	
		req.setAttribute("fileUpload", fp);
		return super.handleRequest(req, res);
	}

	@Override
	protected void deleteModels(Class modelClass, List<Long> toBeDeleted) {
		
		//get lab
		String labName = ThreadBoundContext.getValue();
    	Lab lab = this.labManager.getLabByName(labName);
		//first try to delete the storage
		for(Long id:toBeDeleted){
			MeetingFile meetingFile = (MeetingFile) this.labManager.get(MeetingFile.class,id);
			this.labManager.removeMeetingFile(lab, meetingFile);
		}
		
	
	}
	
}
