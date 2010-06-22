package com.focaplo.myfuse.webapp.controller.project;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

import com.focaplo.myfuse.model.ExperimentImage;
import com.focaplo.myfuse.model.ExperimentNote;
import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.webapp.controller.BimeFormController;
import com.focaplo.myfuse.webapp.controller.FileUpload;
import com.focaplo.myfuse.webapp.spring.ThreadBoundContext;

/**
 *
 *
 */
@Controller
public class ExperimentImageUploadController extends BimeFormController {

	private boolean encryptFile=true;
	
	public void setEncryptFile(boolean encryptFile) {
		this.encryptFile = encryptFile;
	}
	
	@ModelAttribute("fileUpload")
	public FileUpload getFileUpload(){
		return new FileUpload();
	}
	@RequestMapping(value="/project/{projectId}/note/{noteId}/noteImageUpload.html", method=RequestMethod.GET)
	public String showForm(Model model){
		
		return "/project/noteImageUpload";
	}
	@RequestMapping(value="/project/{projectId}/note/{noteId}/noteImageUpload.html", method=RequestMethod.POST)
   public String submitForm(@PathVariable("projectId") Long projectId, @PathVariable("noteId")Long noteId, @ModelAttribute("fileUpload") FileUpload fileUpload, BindingResult result,  HttpServletRequest request, SessionStatus sessionStatus, Locale locale)throws Exception {
	
        // validate a file was entered
        if (fileUpload.getFile().length == 0) {
            Object[] args = 
                new Object[] { getText("uploadForm.file", request.getLocale()) };
//       FIXME     errors.rejectValue("file", "errors.required", args, "File");
//       FIXME     
//            return showForm(request, response, errors);
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        CommonsMultipartFile multipartFile = (CommonsMultipartFile) multipartRequest.getFile("file");

        byte[] file = fileUpload.getFile();
        ExperimentNote note = (ExperimentNote) this.projectManager.get(ExperimentNote.class, noteId);
        ExperimentImage imageFile = new ExperimentImage();
        imageFile.setUploadedBy(this.getLoginUser());
        imageFile.setCreatedDate(new Date());
        imageFile.setFileName(multipartFile.getOriginalFilename());
        imageFile.setLastUpdateDate(new Date());
        imageFile.setExperimentNote(note);
        imageFile.setEncrypted(this.encryptFile);
        if(this.encryptFile){
        	//pass the secret key down to storage service, the key is not saved in database.
//        	meetingFile.setPassword(request.getParameter("secretKey"));
        	imageFile.setPassword(imageFile.getUploadedBy().getPassword());
        }
        //retrieve the file data
        ByteArrayInputStream stream = null;
        try{
      		//not support mark, then make it byte-array-input-stream
        	stream = new ByteArrayInputStream(file);
        	
        	//get lab
        	String labName = ThreadBoundContext.getValue();
        	Lab lab =super.getCurrentLab();
        	this.projectManager.uploadExperimentImageFile(lab, imageFile, stream);
        }catch(Exception e){
        	log.error("error",e);
        	throw new ServletException(e);
        }finally{
        	if(stream!=null){
        		stream.close();
        		
        	}
        }
        return "redirect:/project/"+projectId+"/note/"+noteId+"/form.html";
    }
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
	}
}
