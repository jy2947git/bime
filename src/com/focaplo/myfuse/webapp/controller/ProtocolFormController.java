package com.focaplo.myfuse.webapp.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.ExperimentProtocol;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.ProjectService;

public class ProtocolFormController extends BaseFormController {

	private ProjectService projectManager;


	
	public void setProjectManager(ProjectService projectManager) {
		this.projectManager = projectManager;
	}



	public ProtocolFormController() {
		super();
		setCommandName("experimentProtocol");
	    setCommandClass(ExperimentProtocol.class);
		
	}

	
	public ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command,
            BindException errors)throws Exception {
		log.debug("entering 'onSubmit' method...");
		ExperimentProtocol protocol = (ExperimentProtocol)command;
		Locale locale = request.getLocale();


        Integer originalVersion = protocol.getVersion();
        	if(protocol.getId()==null){
        		protocol.setCreatedByName(((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        	}
        	boolean saveAsNewVersion=false;
        	if(!StringUtils.isBlank(request.getParameter("newVersion"))){
        		saveAsNewVersion=true;
        	}
        	this.projectManager.saveProtocol(protocol, saveAsNewVersion, ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
       saveMessage(request, getText("protocol.saved", protocol.getName(), locale));

       return new ModelAndView(getSuccessView());

	}
	
	protected Object formBackingObject(HttpServletRequest request)
    throws Exception {

            String id = request.getParameter("id");
            ExperimentProtocol protocol;
            if (!StringUtils.isBlank(id)) {
            	protocol = (ExperimentProtocol) this.projectManager.get(ExperimentProtocol.class, new Long(id));
            } else {
                protocol = new ExperimentProtocol();
//                user.addRole(new Role(Constants.USER_ROLE));
            }
            return protocol;

	}

}
