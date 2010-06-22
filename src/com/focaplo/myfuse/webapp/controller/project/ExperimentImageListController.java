package com.focaplo.myfuse.webapp.controller.project;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.BaseObject;
import com.focaplo.myfuse.model.ExperimentImage;
import com.focaplo.myfuse.model.ExperimentNote;
import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.model.Securable;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.LabService;
import com.focaplo.myfuse.service.ProjectService;
import com.focaplo.myfuse.service.StorageService;
import com.focaplo.myfuse.webapp.controller.BimeListController;
import com.focaplo.myfuse.webapp.controller.FileUpload;
import com.focaplo.myfuse.webapp.controller.SecondLevelListable;
import com.focaplo.myfuse.webapp.spring.ThreadBoundContext;
@Controller(value="experimentImageListController")
public class ExperimentImageListController extends BimeListController implements SecondLevelListable{

	@Autowired
	@Qualifier("storageManager")
	private StorageService storageManager;


	public void setStorageManager(StorageService storageManager) {
		this.storageManager = storageManager;
	}

	@Override
	public Class getModelClass() {
		return ExperimentImage.class;
	}
	
	@RequestMapping(value="/project/{projectId}/note/{noteId}/images/list.html", method=RequestMethod.GET)
	public String displayList(@PathVariable("projectId") Long projectId, @PathVariable(value="noteId")Long noteId, Model model){
		//a empty fileupload
		FileUpload fp = new FileUpload();
	
		model.addAttribute("fileUpload", fp);
		model.addAttribute("projectId", projectId);
		model.addAttribute("noteId", noteId);
		model.addAttribute( this.getListAttributeName(),super.getListOfSecuredObjects(noteId));
		return "project/include/include_experimentImageListTable";
	}
	
	
	@RequestMapping(value="/project/{projectId}/note/{noteId}/image/{imageId}/delete.html", method=RequestMethod.POST)
	public String delete(@PathVariable("projectId") Long projectId, @PathVariable(value="noteId")Long noteId,  @PathVariable(value="imageId")Long imageId, Model model){
		super.deleteSelectedObject(imageId);
		model.addAttribute("projectId", projectId);
		model.addAttribute("noteId", noteId);
		model.addAttribute(this.getListAttributeName(), super.getListOfSecuredObjects(noteId));
		return "project/include/include_experimentImageListTable";

	}
	

	public List<? extends BaseObject> getListOfChildModels(Long noteId) {
		log.debug("trying to get file of note id " + noteId);
		return this.projectManager.getExperimentImagesOfNote(new Long(noteId));
	}

	@Override
	public void deleteSelectedObject(Long id) {
		//get lab
    	Lab lab =super.getCurrentLab();
		//first try to delete the storage
    	User loginUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    	ExperimentImage imageFile = (ExperimentImage) this.projectManager.get(ExperimentImage.class,id);
		//make sure only note-access users can delete
		log.debug("checking whether login user has edit access to experiment note " + imageFile.getExperimentNote().getId());
		this.checkAccess(ExperimentNote.class, imageFile.getExperimentNote().getId(), Securable.edit);
		this.projectManager.removeExperimentImageFile(lab, imageFile);
		this.expireCachedObjects(ExperimentImage.class, id);
	}

	

}
