package com.focaplo.myfuse.webapp.controller.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.Constants;
import com.focaplo.myfuse.model.Equipment;
import com.focaplo.myfuse.model.LabelValue;
import com.focaplo.myfuse.model.ManagedProject;
import com.focaplo.myfuse.model.Securable;
import com.focaplo.myfuse.model.ToDo;
import com.focaplo.myfuse.webapp.controller.BimeFormController;
@Controller
public class ToDoFormController extends BimeFormController {

	
	@RequestMapping(value="/project/{projectId}/todo/{toDoId}/form.html", method=RequestMethod.POST)
	public String submitForm(@PathVariable("projectId") Long projectId, @ModelAttribute("toDo") ToDo toDo, BindingResult result, @RequestParam(value="newVersion", required=false, defaultValue="false") Boolean newVersion, HttpServletRequest request, SessionStatus sessionStatus, Locale locale)throws Exception {

		this.checkAccess(ManagedProject.class, projectId, Securable.edit);
        this.projectManager.save(toDo);
        this.expireCachedObjects(ToDo.class, toDo.getId());
        saveMessage(request, getText("toDo.saved","" + toDo.getId(), locale));
        return "redirect:/project/"+projectId+"/todos/list.html";

	}
	
	@RequestMapping(value="/project/{projectId}/todo/{toDoId}/form.html", method=RequestMethod.GET)
	public String showForm(@PathVariable("projectId") Long projectId,@PathVariable("toDoId") Long toDoId, Model model, Locale locale){
		model.addAttribute("statusLabelList",this.getPlanStatusLabelList(locale));
		model.addAttribute("projectId", projectId);
		model.addAttribute("toDoId", toDoId);
        return "/project/toDoForm";
		
	}
	
	@ModelAttribute("toDo")
	public ToDo getToDo(@PathVariable("projectId") Long projectId, @PathVariable("toDoId") Long toDoId,  HttpServletRequest request) throws Exception{
		this.checkAccess(ManagedProject.class, projectId, Securable.edit);
       
        ToDo toDo = (ToDo) super.getModelObject(ToDo.class, toDoId, "get".equalsIgnoreCase(request.getMethod()));
        if (toDoId!=null && toDoId.longValue()>0) {
        	
        } else {

            toDo.setManagedProject((ManagedProject) super.getModelObject(ManagedProject.class, projectId, "get".equalsIgnoreCase(request.getMethod())));
            //by default
            toDo.setStatus(Constants.WORK_STATUS_NOT_STARTED);
        }
        return toDo;
	}
	
	
	
	private List getPlanStatusLabelList(Locale locale) {
		List<LabelValue> data = new ArrayList<LabelValue>();
		
		data.add(new LabelValue(this.getText("work.completed", locale), Constants.WORK_STATUS_COMPLETED));
		data.add(new LabelValue(this.getText("work.in_progress", locale), Constants.WORK_STATUS_IN_PROGRESS));
		data.add(new LabelValue(this.getText("work.unstarted", locale), Constants.WORK_STATUS_NOT_STARTED));
		data.add(new LabelValue(this.getText("work.pause", locale), Constants.WORK_STATUS_PAUSE));
		data.add(new LabelValue(this.getText("work.uncompleted", locale), Constants.WORK_STATUS_UNCOMPLETED));
	
		
		return data;
	}
}
