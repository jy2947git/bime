package com.focaplo.myfuse.webapp.controller;

import java.beans.PropertyEditorSupport;
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
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.Constants;
import com.focaplo.myfuse.model.LabelValue;
import com.focaplo.myfuse.model.ManagedProject;
import com.focaplo.myfuse.model.WorkMonthlyPlan;
import com.focaplo.myfuse.model.WorkPlan;
import com.focaplo.myfuse.model.WorkWeeklyPlan;
import com.focaplo.myfuse.service.ProjectManager;

public class WorkPlanFormController extends BaseFormController {
	protected ProjectManager projectManager;
	protected PropertyEditorSupport projectConverter;
	protected boolean monthly=true;
	
	public void setProjectConverter(PropertyEditorSupport projectConverter) {
		this.projectConverter = projectConverter;
	}



	public void setProjectManager(ProjectManager projectManager) {
		this.projectManager = projectManager;
	}



	public WorkPlanFormController() {
		super();
		setCommandName("workPlan");
	}

	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors, Map controlModel)
			throws Exception {
		if("deleteWeeklyPlan".equalsIgnoreCase(request.getParameter("requestMethod"))){
			log.debug("deleting weekly plan " + request.getParameter("weeklyPlanId") + " of monthly " + request.getParameter("monthlyPlanId"));
			WorkWeeklyPlan weeklyPlan = (WorkWeeklyPlan)this.projectManager.get(WorkWeeklyPlan.class, new Long(request.getParameter("weeklyPlanId")));
			Long monthlyId = weeklyPlan.getWorkMonthlyPlan().getId();
			this.projectManager.remove(WorkWeeklyPlan.class, new Long(request.getParameter("weeklyPlanId")));
			//redirect
			return new ModelAndView("redirect:workplanForm.html?from=list&id="+monthlyId);
		}
		return super.showForm(request, response, errors, controlModel);
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
		WorkPlan workPlan = (WorkPlan)command;
		Locale locale = request.getLocale();

        if (request.getParameter("delete") != null) {
            this.projectManager.remove(WorkMonthlyPlan.class, workPlan.getId());
            saveMessage(request, getText("workPlan.deleted", "" + workPlan.getId(), locale));

            return new ModelAndView(getSuccessView());
        } else {
        	Integer originalVersion = workPlan.getVersion();

        	if(workPlan instanceof WorkWeeklyPlan){
        		String monthlyPlanId = request.getParameter("monthlyPlanId");
                
                log.debug("saving new weekly plan for month plan " + monthlyPlanId);
                ((WorkWeeklyPlan)workPlan).setWorkMonthlyPlan((WorkMonthlyPlan) this.projectManager.get(WorkMonthlyPlan.class, new Long(monthlyPlanId)));
                
                this.projectManager.save(workPlan);
                saveMessage(request, getText("workPlan.added", "" + workPlan.getId(), locale));
                return new ModelAndView("redirect:workplanWeeklyForm.html?from=list&id="+workPlan.getId());
        	}else{
        		this.projectManager.save(workPlan);
        		if (StringUtils.isBlank(request.getParameter("version"))) {
                    saveMessage(request, getText("workPlan.added", "" + workPlan.getId(), locale));
                    return new ModelAndView("redirect:workplanForm.html?from=list&id="+workPlan.getId());
                } else {
                    saveMessage(request, getText("workPlan.updated", "" + workPlan.getId(), locale));
                    return new ModelAndView(getSuccessView());
                }
        	}
        }
//        return showForm(request, response, errors);
	}
	
	protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
		request.setAttribute("monthly", new Boolean(monthly));
		log.debug("it is " + (monthly?"month plan":"week plan"));
		if (!isFormSubmission(request)) {
            String id = request.getParameter("id");
    		
            WorkPlan workPlan;
            if (!StringUtils.isBlank(id) && !"".equals(request.getParameter("version"))) {
            	workPlan = (WorkPlan) this.projectManager.get(this.getCommandClass(), new Long(id));
            } else {
                workPlan = (WorkPlan) this.getCommandClass().newInstance();
                String monthlyPlanId = request.getParameter("monthlyPlanId");
                if(!StringUtils.isBlank(monthlyPlanId)){
                	log.debug("creating new weekly plan for month plan " + monthlyPlanId);
                	request.setAttribute("monthlyPlanId", monthlyPlanId);
                }
                //by default
                workPlan.setStatus(Constants.WORK_STATUS_NOT_STARTED);
                Long projectId = (Long)request.getSession().getAttribute("projectId");
                workPlan.setManagedProject((ManagedProject) this.projectManager.get(ManagedProject.class, projectId));
            }
            return workPlan;
		}else if (request.getParameter("id") != null && !"".equals(request.getParameter("id"))
                && request.getParameter("cancel") == null) {
            // populate user object from database, so all fields don't need to be hidden fields in form
            return this.projectManager.get(this.getCommandClass(), new Long(request.getParameter("id")));
        }

		return super.formBackingObject(request);
	}
	
    protected boolean isAdd(HttpServletRequest request) {
        String method = request.getParameter("method");
        return (method != null && method.equalsIgnoreCase("add"));
    }
    
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		Map<String,List> map = new HashMap<String,List>();
		map.put("projectList", this.projectManager.getAll(ManagedProject.class));
		
		if(monthly){
			map.put("monthLabelList", this.getMonthLabelList(request.getLocale()));
		}else{
			map.put("weekOfMonthLabelList", this.getWeekOfMonthLabelList(request.getLocale()));
		}
		map.put("planTypeLabelList", this.getPlanTypeLabelList(request.getLocale()));
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



	private List<LabelValue> getPlanTypeLabelList(Locale locale) {
		List<LabelValue> data = new ArrayList<LabelValue>();
		if(monthly){
			data.add(new LabelValue(this.getText("workplan.monthly", locale), Constants.WORKPLAN_MONTHLY));
		}else{
			data.add(new LabelValue(this.getText("workplan.weekly", locale), Constants.WORKPLAN_WEEKLY));
		}
		return data;
	}



	private List<LabelValue> getWeekOfMonthLabelList(Locale locale) {
		List<LabelValue> data = new ArrayList<LabelValue>();
		for(int i=0;i<4;i++){
			data.add(new LabelValue(this.getText("week_of_month_" + i, locale), "week_of_month_" + i));
		}
		return data;
	}



	private List<LabelValue> getMonthLabelList(Locale locale) {
		List<LabelValue> data = new ArrayList<LabelValue>();
		for(int i=0;i<12;i++){
			data.add(new LabelValue(this.getText("month_" + i, locale), "month_" + i));
		}
		return data;
	}



	@Override
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) {
		
		super.initBinder(request, binder);
		binder.registerCustomEditor(com.focaplo.myfuse.model.ManagedProject.class, this.projectConverter);
	}
	
	
}
