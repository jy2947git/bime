package com.focaplo.myfuse.webapp.controller.project;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.Equipment;
import com.focaplo.myfuse.model.ToDo;
import com.focaplo.myfuse.model.WorkLog;
import com.focaplo.myfuse.webapp.controller.BimeFormController;
@Controller
public class WorkLogFormController extends BimeFormController {
	
	
	@RequestMapping(value="/project/{projectId}/todo/{toDoId}/worklog/{workLogId}/form.html", method=RequestMethod.POST)
	public String submitForm(@PathVariable("projectId") Long projectId, @PathVariable("toDoId") Long toDoId, @ModelAttribute("workLog") WorkLog workLog, BindingResult result, @RequestParam(value="newVersion", required=false, defaultValue="false") Boolean newVersion, HttpServletRequest request, SessionStatus sessionStatus, Locale locale)throws Exception {


		workLog.setUpdatedByUser(this.getLoginUser().getUsername());
        this.projectManager.save(workLog);
        this.expireCachedObjects(WorkLog.class, workLog.getId());
        saveMessage(request, getText("workLog.saved","" + workLog.getId(), locale));
        return"redirect:/project/"+projectId+"/todo/"+toDoId+"/worklog/" + workLog.getId()+"/form.html?ajax=true";
	}
	
	@ModelAttribute("workLog")
	public WorkLog getWorkLog( @PathVariable("toDoId") Long toDoId, @PathVariable("workLogId") Long workLogId,  HttpServletRequest request) throws Exception {
		

            WorkLog workLog = (WorkLog) super.getModelObject(WorkLog.class, workLogId, "get".equalsIgnoreCase(request.getMethod()));
            if (workLogId!=null && workLogId.longValue()>0) {
            	
            } else {
               
                workLog.setToDo((ToDo) super.getModelObject(ToDo.class, new Long(toDoId), "get".equalsIgnoreCase(request.getMethod())));
              
            }
            return workLog;

	}
	
	@RequestMapping(value="/project/{projectId}/todo/{toDoId}/worklog/{workLogId}/form.html", method=RequestMethod.GET)
	public String showForm(@PathVariable("workLogId") Long workLogId, Model model){
           
            return "/project/include/include_workLogForm";
		
	}
}
