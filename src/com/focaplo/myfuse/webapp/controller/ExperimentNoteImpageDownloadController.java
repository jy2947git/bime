package com.focaplo.myfuse.webapp.controller;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.ExperimentImage;
import com.focaplo.myfuse.model.ExperimentNote;
import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.model.Securable;
import com.focaplo.myfuse.service.LabService;
import com.focaplo.myfuse.service.ProjectService;
import com.focaplo.myfuse.service.StorageService;
import com.focaplo.myfuse.webapp.spring.ThreadBoundContext;

public class ExperimentNoteImpageDownloadController extends BaseFormController {
	protected transient final Log log = LogFactory.getLog(getClass());
	@Autowired
	private ProjectService projectManager;
	@Autowired
	private StorageService storageService;
	@Autowired
	private LabService labManager;

	public void setStorageService(StorageService storageService) {
		this.storageService = storageService;
	}



	public void setLabManager(LabService labManager) {
		this.labManager = labManager;
	}



	public void setProjectManager(ProjectService projectManager) {
		this.projectManager = projectManager;
	}



	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse response) throws Exception {
		//get the meeting file id
		String id = req.getParameter("id");
		this.checkAccess(ExperimentNote.class, new Long(id), Securable.edit);
		
		ExperimentImage file = (ExperimentImage) this.projectManager.get(ExperimentImage.class, new Long(id));
		if(file==null){
			//should not happen
			throw new RuntimeException("Failed to locate image file " + id);
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
