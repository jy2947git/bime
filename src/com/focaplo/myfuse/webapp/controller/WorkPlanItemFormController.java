package com.focaplo.myfuse.webapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.Constants;
import com.focaplo.myfuse.model.LabelValue;
import com.focaplo.myfuse.model.ManagedProject;
import com.focaplo.myfuse.model.WorkMonthlyPlan;
import com.focaplo.myfuse.model.WorkPlan;
import com.focaplo.myfuse.model.WorkPlanItem;
import com.focaplo.myfuse.model.WorkWeeklyPlan;
import com.focaplo.myfuse.service.ProjectManager;

public class WorkPlanItemFormController extends BaseFormController {
	protected ProjectManager projectManager;

	public void setProjectManager(ProjectManager projectManager) {
		this.projectManager = projectManager;
	}

	public WorkPlanItemFormController() {
		super();
		setCommandName("workPlanItem");
		setCommandClass(WorkPlanItem.class);
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
		WorkPlanItem workPlanItem = (WorkPlanItem)command;
		Locale locale = request.getLocale();

        if (request.getParameter("delete") != null) {
            this.projectManager.remove(WorkPlanItem.class, workPlanItem.getId());
            saveMessage(request, getText("workPlanItem.deleted", "" + workPlanItem.getId(), locale));

            return new ModelAndView(getSuccessView());
        } else {
        	Integer originalVersion = workPlanItem.getVersion();
        	
        	String planId = request.getParameter("planId");
            if(!StringUtils.isBlank(planId)){
                	log.debug("saving new plan item for " + planId);
                	workPlanItem.setWorkPlan((WorkPlan) this.projectManager.get(WorkPlan.class, new Long(planId)));
                }
        		
        	
        	this.projectManager.save(workPlanItem);
        	 if (!StringUtils.equals(request.getParameter("from"), "list")) {
                 saveMessage(request, getText("workPlanItem.saved","" + workPlanItem.getId(), locale));
                 return new ModelAndView("redirect:project/include/include_workPlanItemForm.html?from=list&id="+workPlanItem.getId());
             } else {
                 if (StringUtils.isBlank(request.getParameter("version"))) {
                     saveMessage(request, getText("workPlanItem.added", "" + workPlanItem.getId(), locale));

                     return new ModelAndView("redirect:include_workPlanItemForm.html?from=list&id="+workPlanItem.getId());
                 } else {
                     saveMessage(request, getText("workPlanItem.updated", "" + workPlanItem.getId(), locale));
                     return new ModelAndView("redirect:include_workPlanItemForm.html?from=list&id="+workPlanItem.getId());

                 }
             }
        }
//        return showForm(request, response, errors);
	}
	
	protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
		
		if (!isFormSubmission(request)) {
            String id = request.getParameter("id");
    		
            WorkPlanItem workPlanItem;
            if (!StringUtils.isBlank(id) && !"".equals(request.getParameter("version"))) {
            	workPlanItem = (WorkPlanItem) this.projectManager.get(this.getCommandClass(), new Long(id));
            } else {
                workPlanItem = new WorkPlanItem();
                String planId = request.getParameter("planId");
                if(!StringUtils.isBlank(planId)){
                	log.debug("creating new plan item for pplan " + planId);
                	request.setAttribute("planId", planId);
                }
                //by default
                workPlanItem.setStatus(Constants.WORK_STATUS_NOT_STARTED);
            }
            return workPlanItem;
		}else if (request.getParameter("id") != null && !"".equals(request.getParameter("id"))
                && request.getParameter("cancel") == null) {
            // populate user object from database, so all fields don't need to be hidden fields in form
            return this.projectManager.get(this.getCommandClass(), new Long(request.getParameter("id")));
        }

		return super.formBackingObject(request);
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
