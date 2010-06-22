package com.focaplo.myfuse.webapp.controller.lab;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.BaseObject;
import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.model.MeetingFile;
import com.focaplo.myfuse.service.LabService;
import com.focaplo.myfuse.service.StorageService;
import com.focaplo.myfuse.webapp.controller.BimeListController;
import com.focaplo.myfuse.webapp.controller.FileUpload;
import com.focaplo.myfuse.webapp.controller.SecondLevelListable;
import com.focaplo.myfuse.webapp.spring.ThreadBoundContext;
@Controller(value="meetingFileListController")
public class MeetingFileListController extends BimeListController implements SecondLevelListable{
	@Autowired
	private LabService labManager;
	@Autowired
	@Qualifier("storageManager")
	private StorageService storageManager;
	
	public void setLabManager(LabService labManager) {
		this.labManager = labManager;
	}



	public void setStorageManager(StorageService storageManager) {
		this.storageManager = storageManager;
	}



	@Override
	public Class getModelClass() {
		return MeetingFile.class;
	}
	




	@Override
	public void deleteSelectedObject(Long id) {
		//get lab
		Lab lab = super.getCurrentLab();
    	MeetingFile meetingFile = (MeetingFile) this.labManager.get(MeetingFile.class,id);
		this.labManager.removeMeetingFile(lab, meetingFile);
		this.expireCachedObjects(MeetingFile.class, id);
	}

	
	@RequestMapping(value="/lab/meeting/{meetingId}/documents/list.html", method=RequestMethod.GET)
	public String displayList(@PathVariable("meetingId") Long meetingId, Model model){
		model.addAttribute( this.getListAttributeName(),super.getListOfSecuredObjects(meetingId));
		//an empty fileupload
		FileUpload fp = new FileUpload();
		model.addAttribute("fileUpload", fp);
		model.addAttribute("meetingId", meetingId);
		return "/lab/meetingFileList";
	}
	
	
	@RequestMapping(value="/lab/meeting/{meetingId}/document/{documentId}/delete.html", method=RequestMethod.DELETE)
	public String delete(@PathVariable("meetingId") Long meetingId,@PathVariable("documentId") Long documentId, Model model){
		super.deleteSelectedObject(documentId);
		model.addAttribute(this.getListAttributeName(), super.getListOfSecuredObjects(meetingId));
		//a empty fileupload
		FileUpload fp = new FileUpload();
		model.addAttribute("fileUpload", fp);
		model.addAttribute("meetingId", meetingId);
		return "lab/include/include_meetingFileListTable";

	}



	public List<? extends BaseObject> getListOfChildModels(Long meetingId) {
		return this.labManager.getMeetingFilesOfMeeting(meetingId);
	}
	
}
