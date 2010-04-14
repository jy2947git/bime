package com.focaplo.myfuse.webapp.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.WorkMonthlyPlan;
import com.focaplo.myfuse.model.WorkPlan;
import com.focaplo.myfuse.model.WorkWeeklyPlan;

public class WorkWeeklyPlanFormController extends WorkPlanFormController {

	public WorkWeeklyPlanFormController() {
		super();
		setCommandClass(WorkWeeklyPlan.class);
		this.monthly=false;
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
                saveMessage(request, getText("workPlan.weekly.added", "" + workPlan.getId(), locale));
                return new ModelAndView("redirect:workplanWeeklyForm.html?from=list&id="+workPlan.getId());
        	}else{
        		this.projectManager.save(workPlan);
        		if (StringUtils.isBlank(request.getParameter("version"))) {
                    saveMessage(request, getText("workPlan.weekly.added", "" + workPlan.getId(), locale));
                    return new ModelAndView("redirect:workplanForm.html?from=list&id="+workPlan.getId());
                } else {
                    saveMessage(request, getText("workPlan.updated", "" + workPlan.getId(), locale));
                    return new ModelAndView(getSuccessView());
                }
        	}
        }
//        return showForm(request, response, errors);
	}
}
