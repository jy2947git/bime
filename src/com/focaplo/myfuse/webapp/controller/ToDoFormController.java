package com.focaplo.myfuse.webapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.Constants;
import com.focaplo.myfuse.model.LabelValue;
import com.focaplo.myfuse.model.ManagedProject;
import com.focaplo.myfuse.model.Securable;
import com.focaplo.myfuse.model.ToDo;
import com.focaplo.myfuse.model.WorkLog;
import com.focaplo.myfuse.model.WorkMonthlyPlan;
import com.focaplo.myfuse.model.WorkPlan;
import com.focaplo.myfuse.model.WorkPlanItem;
import com.focaplo.myfuse.model.WorkWeeklyPlan;
import com.focaplo.myfuse.service.ProjectService;

public class ToDoFormController extends BaseFormController {
	@Autowired
	protected ProjectService projectManager;

	public void setProjectManager(ProjectService projectManager) {
		this.projectManager = projectManager;
	}

	public ToDoFormController() {
		super();
		setCommandName("toDo");
		setCommandClass(ToDo.class);
	}

	
	public ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command,
            BindException errors)throws Exception {
		log.debug("entering 'onSubmit' method...");
		ToDo toDo = (ToDo)command;
		Locale locale = request.getLocale();
		Long projectId = (Long)request.getSession().getAttribute("projectId");
		this.checkAccess(this.getCommandClass(), projectId, Securable.edit);
        this.projectManager.save(toDo);
        
        saveMessage(request, getText("toDo.saved","" + toDo.getId(), locale));
        return new ModelAndView(getSuccessView());

	}
	
	protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
		Long projectId = (Long)request.getSession().getAttribute("projectId");
		this.checkAccess(this.getCommandClass(), projectId, Securable.edit);
            String id = request.getParameter("id");
    		
            ToDo toDo;
            if (!StringUtils.isBlank(id)) {
            	toDo = (ToDo) this.projectManager.get(this.getCommandClass(), new Long(id));
            } else {
                toDo = new ToDo();
               
                toDo.setManagedProject((ManagedProject) this.projectManager.get(ManagedProject.class, projectId));
                //by default
                toDo.setStatus(Constants.WORK_STATUS_NOT_STARTED);
            }
            return toDo;

	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		Map<String,List> map = new HashMap<String,List>();
		map.put("statusLabelList", this.getPlanStatusLabelList(request.getLocale()));
		return map;
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
