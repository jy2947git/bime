package com.focaplo.myfuse.webapp.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.model.LabMeeting;
import com.focaplo.myfuse.model.MeetingFile;
import com.focaplo.myfuse.service.LabService;
import com.focaplo.myfuse.webapp.spring.ThreadBoundContext;

/**
 * Controller class to upload Files.
 *
 * <p>
 * <a href="FileUploadFormController.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class MeetingFileUploadController extends BaseFormController {
	@Autowired
	private LabService labManager;
	private boolean encryptFile=true;
	
	public void setLabManager(LabService labManager) {
		this.labManager = labManager;
	}
	

	public void setEncryptFile(boolean encryptFile) {
		this.encryptFile = encryptFile;
	}


	public MeetingFileUploadController() {
        setCommandName("fileUpload");
        setCommandClass(FileUpload.class);
    }
    
    public ModelAndView processFormSubmission(HttpServletRequest request,
                                              HttpServletResponse response,
                                              Object command,
                                              BindException errors)
    throws Exception {
        if (request.getParameter("cancel") != null) {
            return new ModelAndView(getCancelView());
        }

        return super.processFormSubmission(request, response, command, errors);
    }

	
    public ModelAndView onSubmit(HttpServletRequest request,
                                 HttpServletResponse response, Object command,
                                 BindException errors)
    throws Exception {
        FileUpload fileUpload = (FileUpload) command;

        // validate a file was entered
        if (fileUpload.getFile().length == 0) {
            Object[] args = 
                new Object[] { getText("uploadForm.file", request.getLocale()) };
            errors.rejectValue("file", "errors.required", args, "File");
            
            return showForm(request, response, errors);
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");

        Long meetingId = (Long)request.getSession().getAttribute("meetingId");
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
        	String labName = ThreadBoundContext.getValue();
        	Lab lab = this.labManager.getLabByName(labName);

        	this.labManager.uploadMeetingFile(lab, meetingFile, stream);
        }catch(Exception e){
        	log.error("error",e);
        	throw new ServletException(e);
        }finally{
        	if(stream!=null){
        		stream.close();
        		
        	}
        }
        return new ModelAndView("redirect:meetingFileList.html");
    }
}
