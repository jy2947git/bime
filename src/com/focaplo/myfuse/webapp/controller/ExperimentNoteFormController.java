package com.focaplo.myfuse.webapp.controller;

import java.beans.PropertyEditorSupport;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.ExperimentImage;
import com.focaplo.myfuse.model.ExperimentNote;
import com.focaplo.myfuse.model.ExperimentProtocol;
import com.focaplo.myfuse.model.ManagedProject;
import com.focaplo.myfuse.model.Securable;
import com.focaplo.myfuse.service.ProjectManager;
import com.focaplo.myfuse.service.UserManager;

public class ExperimentNoteFormController extends BaseFormController {
	private ProjectManager projectManager;
	private UserManager userManager;
	PropertyEditorSupport protocolConverter;
	PropertyEditorSupport projectConverter;
	
	public void setProtocolConverter(PropertyEditorSupport protocolConverter) {
		this.protocolConverter = protocolConverter;
	}



	public void setProjectConverter(PropertyEditorSupport projectConverter) {
		this.projectConverter = projectConverter;
	}



	public void setUserManager(UserManager userManager) {
	this.userManager = userManager;
}



	public void setProjectManager(ProjectManager projectManager) {
		this.projectManager = projectManager;
	}



	public ExperimentNoteFormController() {
		super();
		setCommandName("experimentNote");
	    setCommandClass(ExperimentNote.class);
		
	}

	public ModelAndView processFormSubmission(HttpServletRequest request,
            HttpServletResponse response,
            Object command,
            BindException errors)throws Exception {
		if (request.getParameter("cancel") != null) {
			if (!StringUtils.equals(request.getParameter("from"), "list")) {
				return new ModelAndView(getCancelView());
			} else {
				return new ModelAndView(getSuccessView());
			}
		}
		
		return super.processFormSubmission(request, response, command, errors);
	}
	
	public ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command,
            BindException errors)throws Exception {
		log.debug("entering 'onSubmit' method...");
		ExperimentNote note = (ExperimentNote)command;
		Locale locale = request.getLocale();
		//only user with edit permission can edit
		if(note.getId()!=null){
			this.checkAccess(this.getCommandClass(), note.getId(), Securable.edit);
		}
        if (request.getParameter("delete") != null) {
            this.projectManager.remove(ExperimentNote.class, note.getId());
            saveMessage(request, getText("note.deleted", "" + note.getId(), locale));

            return new ModelAndView(getSuccessView());
        } else {
        	Integer originalVersion = note.getVersion();
        	Enumeration names = request.getParameterNames();
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
        	 if (!StringUtils.equals(request.getParameter("from"), "list")) {
                 saveMessage(request, getText("note.saved","" + note.getId(), locale));
                 return new ModelAndView(getSuccessView());
             } else {
                 if (StringUtils.isBlank(request.getParameter("version"))) {
                     saveMessage(request, getText("note.added", "" + note.getId(), locale));
                     return new ModelAndView(getSuccessView());
                 } else {
                     saveMessage(request, getText("note.updated", "" + note.getId(), locale));
//                     return new ModelAndView("redirect:noteList.html");
                     return new ModelAndView(getSuccessView());
                 }
             }
        }
//        return showForm(request, response, errors);
	}
	
	protected Object formBackingObject(HttpServletRequest request)
    throws Exception {

            String id = request.getParameter("id");
    		//a empty fileupload
    		FileUpload fp = new FileUpload();
    		fp.setId((id==null||id.equalsIgnoreCase(""))?null:new Long(id));
    		request.setAttribute("fileUpload", fp);
            ExperimentNote note;
            if (id != null && !StringUtils.isBlank(id)) {
            	log.debug("loading note id " + id);
            	this.checkAccess(this.getCommandClass(), new Long(id), Securable.edit);
            	note = (ExperimentNote) this.projectManager.get(ExperimentNote.class, new Long(id));
            } else {
            	log.debug("creating new note");
                note = new ExperimentNote();
                //by default
                note.getAccessedBy().add(this.getLoginUser());
                Long projectId = (Long)request.getSession().getAttribute("projectId");
                note.setManagedProject((ManagedProject) this.projectManager.get(ManagedProject.class, projectId));
            }
            log.debug("note has images:" + note.getExperimentImages()==null?"empty":note.getExperimentImages().size());
            request.setAttribute("experimentImageList", note.getExperimentImages());
		return note;
	}
	
    protected boolean isAdd(HttpServletRequest request) {
        String method = request.getParameter("method");
        return (method != null && method.equalsIgnoreCase("add"));
    }
    
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		Map<String,List> map = new HashMap<String,List>();
		map.put("protocolList", this.projectManager.getAll(ExperimentProtocol.class));
		map.put("projectList", this.projectManager.getAll(ManagedProject.class));
		map.put("userList", this.userManager.getLabelValueListOfUsers());
		return map;
	}



	@Override
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) {
		
		super.initBinder(request, binder);
		binder.registerCustomEditor(com.focaplo.myfuse.model.ExperimentProtocol.class, this.protocolConverter);
		binder.registerCustomEditor(com.focaplo.myfuse.model.ManagedProject.class, this.projectConverter);
	}
	
	
}
