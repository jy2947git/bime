package com.focaplo.myfuse.webapp.controller.project;

import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.ExperimentImage;
import com.focaplo.myfuse.model.ExperimentNote;
import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.model.Securable;
import com.focaplo.myfuse.webapp.controller.BimeFormController;
import com.focaplo.myfuse.webapp.spring.ThreadBoundContext;
@Controller(value="experimentNoteImageDownloadController")
public class ExperimentNoteImpageDownloadController extends BimeFormController {

	@RequestMapping(value="/project/{projectId}/note/{noteId}/image/{imageId}/noteImageDownload.html", method=RequestMethod.GET)
	public ModelAndView handleRequest(@PathVariable("projectId") Long projectId, @PathVariable("noteId") Long noteId, @PathVariable("imageId") Long imageId, HttpServletResponse response) throws Exception {
		//
		this.checkAccess(ExperimentNote.class, new Long(noteId), Securable.edit);
		
		ExperimentImage file = (ExperimentImage) this.projectManager.get(ExperimentImage.class, new Long(imageId));
		if(file==null){
			//should not happen
			throw new RuntimeException("Failed to locate image file " + imageId);
		}
        if(file.isEncrypted()){
        	//pass the secret key down to storage service, the key is not saved in database.
        	file.setPassword(file.getUploadedBy().getPassword());
        }
		//get lab
  
    	Lab lab =super.getCurrentLab();
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
