package com.focaplo.myfuse.webapp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.ExperimentImage;
import com.focaplo.myfuse.model.ExperimentNote;
import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.model.Securable;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.LabService;
import com.focaplo.myfuse.service.ProjectService;
import com.focaplo.myfuse.service.StorageService;
import com.focaplo.myfuse.webapp.spring.ThreadBoundContext;

public class ExperimentImageListController extends BaseListController{
	@Autowired
	private ProjectService projectManager;
	@Autowired
	private LabService labManager;
	@Autowired
	private StorageService storageService;
	
	public void setProjectManager(ProjectService projectManager) {
		this.projectManager = projectManager;
	}

	public void setLabManager(LabService labManager) {
		this.labManager = labManager;
	}

	public void setStorageService(StorageService storageService) {
		this.storageService = storageService;
	}
	@Override
	public Class getModelClass() {
		return ExperimentImage.class;
	}
	@Override
	protected List getListOfModels(HttpServletRequest req) {
		String noteId = (String)req.getParameter("id");
		log.debug("trying to get file of note id " + noteId);
		return this.projectManager.getExperimentImagesOfNote(new Long(noteId));
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
    	User loginUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	
		for(Long id:toBeDeleted){

			ExperimentImage imageFile = (ExperimentImage) this.projectManager.get(ExperimentImage.class,id);
			//make sure only note-access users can delete
			log.debug("checking whether login user has edit access to experiment note " + imageFile.getExperimentNote().getId());
			this.checkAccess(ExperimentNote.class, imageFile.getExperimentNote().getId(), Securable.edit);
			this.projectManager.removeExperimentImageFile(lab, imageFile);
		}
		
	
	}
}
