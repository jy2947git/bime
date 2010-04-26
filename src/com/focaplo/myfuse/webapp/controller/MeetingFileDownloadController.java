package com.focaplo.myfuse.webapp.controller;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.model.MeetingFile;
import com.focaplo.myfuse.service.LabService;
import com.focaplo.myfuse.service.StorageService;
import com.focaplo.myfuse.webapp.spring.ThreadBoundContext;

public class MeetingFileDownloadController implements Controller {
	protected transient final Log log = LogFactory.getLog(getClass());
	@Autowired
	private LabService labManager;
	@Autowired
	@Qualifier("localDriveStorageManager")
	private StorageService storageService;
	
	public void setLabManager(LabService labManager) {
		this.labManager = labManager;
	}

	public void setStorageService(StorageService storageService) {
		this.storageService = storageService;
	}

	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse response) throws Exception {
		//get the meeting file id
		String meetingFileId = req.getParameter("id");
		MeetingFile file = (MeetingFile) this.labManager.get(MeetingFile.class, new Long(meetingFileId));
		if(file==null){
			//should not happen
			throw new RuntimeException("Failed to locate meeting file " + meetingFileId);
		}
        
        if(file.isEncrypted()){
        	//pass the secret key down to storage service, the key is not saved in database.
        	file.setPassword(file.getUploadedBy().getPassword());
        }
		//get lab
    	String labName = ThreadBoundContext.getValue();
    	Lab lab = this.labManager.getLabByName(labName);
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
	    	is = this.storageService.downloadFile(lab, file);
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
