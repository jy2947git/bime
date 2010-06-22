package com.focaplo.myfuse.webapp.controller.lab;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.model.MeetingFile;
import com.focaplo.myfuse.service.LabService;
import com.focaplo.myfuse.service.StorageService;
import com.focaplo.myfuse.webapp.controller.BimeFormController;
import com.focaplo.myfuse.webapp.spring.ThreadBoundContext;
@Controller
public class MeetingFileDownloadController extends BimeFormController{

	@RequestMapping(value="/lab/meeting/{meetingId}/document/{documentId}/download.html", method=RequestMethod.GET)
	public ModelAndView handleRequest(@PathVariable("meetingId") Long meetingId, @PathVariable("documentId") Long documentId, HttpServletResponse response) throws Exception {
		//get the meeting file id
		MeetingFile file = (MeetingFile) this.labManager.get(MeetingFile.class, documentId);
		if(file==null){
			//should not happen
			throw new RuntimeException("Failed to locate meeting file " + documentId);
		}
        
        if(file.isEncrypted()){
        	//pass the secret key down to storage service, the key is not saved in database.
        	file.setPassword(file.getUploadedBy().getPassword());
        }
		//get lab
    	Lab lab = super.getCurrentLab();
		InputStream is = null;

		String shortFileName = file.getFileName();
		//get file and send to response stream
		StringBuffer contentDisposition = new StringBuffer("attachment; filename=\"");
	    contentDisposition.append(shortFileName);
	    contentDisposition.append("\"");
	    //response.setHeader("Content-Type", "application/vnd.ms-excel");
	    response.setHeader("Content-Disposition", contentDisposition.toString());

	    java.io.OutputStream os = response.getOutputStream();
	    try{
	    	is = this.storageManager.downloadFile(lab, file);
	    	byte[] buffer = new byte[8024];
	    	int read = is.read(buffer);
	    	while (read >= 0) {
	    	    if (read > 0)    {
	    	        os.write(buffer, 0, read);
	    	    }
	    	    read = is.read(buffer);
	    	}

	    }finally{
	    	if(is!=null){
	    		is.close();
	    		is = null;
	    	}
	    	if(os!=null){
	    		os.flush();
	    		os.close();
	    	}
	    }
	   

		return null;
	}

}
