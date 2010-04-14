package com.focaplo.myfuse.webapp.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.ToDo;
import com.focaplo.myfuse.model.WorkLog;
import com.focaplo.myfuse.service.ProjectManager;

public class WorkLogFormController extends BaseFormController {
	protected ProjectManager projectManager;

	public void setProjectManager(ProjectManager projectManager) {
		this.projectManager = projectManager;
	}

	public WorkLogFormController() {
		super();
		setCommandName("workLog");
		setCommandClass(WorkLog.class);
	}

	
	public ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command,
            BindException errors)throws Exception {
		log.debug("entering 'onSubmit' method...");
		WorkLog workLog = (WorkLog)command;
		Locale locale = request.getLocale();
		workLog.setUpdatedByUser(this.getLoginUser().getUsername());
        this.projectManager.save(workLog);
        saveMessage(request, getText("workLog.saved","" + workLog.getId(), locale));
        return new ModelAndView("redirect:/project/include/include_workLogForm.html?id=" + workLog.getId());
	}
	
	protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
		
            String id = request.getParameter("id");
    		
            WorkLog workLog;
            if (!StringUtils.isBlank(id)) {
            	workLog = (WorkLog) this.projectManager.get(this.getCommandClass(), new Long(id));
            } else {
                workLog = new WorkLog();
                String toDoId = request.getParameter("toDoId");
                workLog.setToDo((ToDo) this.projectManager.get(ToDo.class, new Long(toDoId)));
              
            }
            return workLog;

	}
	

}
