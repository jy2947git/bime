package com.focaplo.myfuse.webapp.controller.project;

import java.beans.PropertyEditorSupport;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.focaplo.myfuse.model.BaseObject;
import com.focaplo.myfuse.model.ExperimentNote;
import com.focaplo.myfuse.model.ExperimentProtocol;
import com.focaplo.myfuse.model.LabelValue;
import com.focaplo.myfuse.model.ManagedProject;
import com.focaplo.myfuse.model.Securable;
import com.focaplo.myfuse.webapp.controller.BimeFormController;
import com.focaplo.myfuse.webapp.controller.FileUpload;
@Controller
public class ExperimentNoteFormController extends BimeFormController {
	@Autowired
	PropertyEditorSupport protocolConverter;
	@Autowired
	PropertyEditorSupport projectConverter;
	@Autowired
	PropertyEditorSupport userConverter;
	
	public void setUserConverter(PropertyEditorSupport userConverter) {
		this.userConverter = userConverter;
	}



	public void setProtocolConverter(PropertyEditorSupport protocolConverter) {
		this.protocolConverter = protocolConverter;
	}



	public void setProjectConverter(PropertyEditorSupport projectConverter) {
		this.projectConverter = projectConverter;
	}


	@ModelAttribute("experimentNote")
	public ExperimentNote formBackingObject(@PathVariable(value="noteId")Long noteId, @PathVariable(value="projectId") Long projectId, HttpServletRequest request) throws Exception{
		if(noteId!=null && noteId.longValue()>0){
			this.checkAccess(ExperimentNote.class, noteId, Securable.edit);
		}
		
		ExperimentNote note = (ExperimentNote) super.getModelObject(ExperimentNote.class, noteId, "get".equalsIgnoreCase(request.getMethod()));
		if(noteId==null || noteId.intValue()==0){
	        //by default
	        note.getAccessedBy().add(this.getLoginUser());
	        note.setManagedProject((ManagedProject) this.projectManager.get(ManagedProject.class, projectId));
		}
		return note;
		
	}
	@RequestMapping(value="/project/{projectId}/note/{noteId}/form.html", method=RequestMethod.POST)
	public String submitForm(@PathVariable("projectId") Long projectId, @ModelAttribute("experimentNote")ExperimentNote note, BindingResult result,  HttpServletRequest request, SessionStatus sessionStatus, Locale locale)throws Exception {

		//only user with edit permission can edit
		if(note.getId()!=null){
			this.checkAccess(ExperimentNote.class, note.getId(), Securable.edit);
		}

        	Integer originalVersion = note.getVersion();
        	String[] accessUserIds = request.getParameterValues("accessUserIds");
        	log.debug("there are " + accessUserIds);
        	if(accessUserIds!=null){
	        	for(String userId:accessUserIds){
	        		note.getAccessedBy().add(this.userManager.getUser(userId));
	        	}
        	}
        	if(note.getId()==null){
        		note.setResearcher(this.getLoginUser());
        	}
        
        	this.projectManager.saveExperimentNote(note);
        	this.expireCachedObjects(ExperimentNote.class, note.getId());
        	 saveMessage(request, getText("note.saved","" + note.getId(), locale));
     
        	if(originalVersion==null){
        		//back to the form
        		return "/project/{"+projectId+"}/note/{"+note.getId()+"}/form.html";
        	}else{
        		return "redirect:/project/" + projectId + "/notes/list.html";
        	}
	}
	
	
	@RequestMapping(value="/project/{projectId}/note/{noteId}/form.html", method=RequestMethod.GET)
	public String showForm(@PathVariable(value="projectId") Long projectId, @PathVariable(value="noteId") Long noteId, Model model) throws Exception{
    		//a empty fileupload
    		FileUpload fp = new FileUpload();
    		model.addAttribute("fileUpload", fp);
            model.addAttribute("projectId", projectId);
            model.addAttribute("noteId", noteId);
            return "/project/noteForm";
	}
	
    


	@ModelAttribute("userList")
	protected List<LabelValue> getUsers(){
		return super.getLabelValueListOfUsers();

	}
	@ModelAttribute("projectList")
	protected List<ManagedProject> getProjects(){
		return super.getListOfModelObject(ManagedProject.class);

	}
	@ModelAttribute("protocolList")
	protected List<ExperimentProtocol> getProtocols(){
		return super.getListOfModelObject(ExperimentProtocol.class);

	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(com.focaplo.myfuse.model.ExperimentProtocol.class, this.protocolConverter);
		binder.registerCustomEditor(com.focaplo.myfuse.model.ManagedProject.class, this.projectConverter);
		binder.registerCustomEditor(com.focaplo.myfuse.model.User.class, this.userConverter);
		
	}
}
