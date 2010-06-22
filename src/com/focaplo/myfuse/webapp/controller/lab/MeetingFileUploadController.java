package com.focaplo.myfuse.webapp.controller.lab;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.model.LabMeeting;
import com.focaplo.myfuse.model.MeetingFile;
import com.focaplo.myfuse.service.LabService;
import com.focaplo.myfuse.webapp.controller.BimeFormController;
import com.focaplo.myfuse.webapp.controller.FileUpload;
import com.focaplo.myfuse.webapp.spring.ThreadBoundContext;

@Controller
public class MeetingFileUploadController extends BimeFormController {
	
	private boolean encryptFile=true;
	
	public void setEncryptFile(boolean encryptFile) {
		this.encryptFile = encryptFile;
	}
	
	@ModelAttribute("fileUpload")
	public FileUpload getFileUpload(){
		return new FileUpload();
	}
	
	@RequestMapping(value="/lab/meeting/{meetingId}/documents/upload.html", method=RequestMethod.GET)
	public String showForm(Model model){
		
		return "/lab/include/include_image_upload";
	}
	
	@RequestMapping(value="/lab/meeting/{meetingId}/documents/upload.html", method=RequestMethod.POST)
	public String submitForm(@PathVariable("meetingId") Long meetingId, @ModelAttribute("fileUpload") FileUpload fileUpload, BindingResult result,  HttpServletRequest request, SessionStatus sessionStatus, Locale locale)throws Exception {

        // validate a file was entered
        if (fileUpload.getFile().length == 0) {
            Object[] args = 
                new Object[] { getText("uploadForm.file", request.getLocale()) };
//            FIXME errors.rejectValue("file", "errors.required", args, "File");
            
//            FIXME return showForm(request, response, errors);
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");

   
        LabMeeting meeting = (LabMeeting) this.labManager.get(LabMeeting.class, meetingId);
        MeetingFile meetingFile = new MeetingFile();
        meetingFile.setUploadedBy(this.getLoginUser());
        meetingFile.setCreatedDate(new Date());
        meetingFile.setFileName(file.getOriginalFilename());
        meetingFile.setLastUpdateDate(new Date());
        meetingFile.setMeeting(meeting);
        meetingFile.setEncrypted(this.encryptFile);
        if(this.encryptFile){
        	//pass the secret key down to storage service, the key is not saved in database.
//        	meetingFile.setPassword(request.getParameter("secretKey"));
        	meetingFile.setPassword(meetingFile.getUploadedBy().getPassword());
        }
        //retrieve the file data
        ByteArrayInputStream stream = null;
        try{
      		//not support mark, then make it byte-array-input-stream
        	stream = new ByteArrayInputStream(file.getBytes());
        	
        	//get lab
        	Lab lab = super.getCurrentLab();

        	this.labManager.uploadMeetingFile(lab, meetingFile, stream);
        }catch(Exception e){
        	log.error("error",e);
        	throw new ServletException(e);
        }finally{
        	if(stream!=null){
        		stream.close();
        		
        	}
        }
        return "redirect:/lab/meeting/"+meetingId+"/documents/list.html";
    }
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
	}
}
