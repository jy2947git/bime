package com.focaplo.myfuse.webapp.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.mail.MailException;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.focaplo.myfuse.Constants;
import com.focaplo.myfuse.model.Equipment;
import com.focaplo.myfuse.model.ManagedGrant;
import com.focaplo.myfuse.model.Role;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.GrantService;
import com.focaplo.myfuse.service.InventoryService;
import com.focaplo.myfuse.webapp.util.RequestUtil;

public class GrantFormController extends BaseFormController {

	private GrantService grantManager;




	public void setGrantManager(GrantService grantManager) {
		this.grantManager = grantManager;
	}

	public GrantFormController() {
		super();
		setCommandName("managedGrant");
	    setCommandClass(ManagedGrant.class);
		
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
		ManagedGrant grant = (ManagedGrant)command;
		Locale locale = request.getLocale();

        if (request.getParameter("delete") != null) {
            this.grantManager.remove(ManagedGrant.class, grant.getId());
            saveMessage(request, getText("grant.deleted", grant.getName(), locale));

            return new ModelAndView(getSuccessView());
        } else {
        	Integer originalVersion = grant.getVersion();
        	this.grantManager.save(grant);
        	 if (!StringUtils.equals(request.getParameter("from"), "list")) {
                 saveMessage(request, getText("grant.saved",grant.getName(), locale));

//                 // return to main Menu
//                 return new ModelAndView("redirect:grantList.html");
                 return new ModelAndView(getSuccessView());
             } else {
                 if (StringUtils.isBlank(request.getParameter("version"))) {
                     saveMessage(request, getText("grant.added", grant.getName(), locale));

                     // Send an account information e-mail
//                     message.setSubject(getText("signup.email.subject", locale));
//
//                     try {
//                         sendUserMessage(user, getText("newuser.email.message", user.getFullName(), locale),
//                                         RequestUtil.getAppURL(request));
//                     } catch (MailException me) {
//                         saveError(request, me.getCause().getLocalizedMessage());
//                     }

                     return new ModelAndView(getSuccessView());
                 } else {
                     saveMessage(request, getText("grant.updated", grant.getName(), locale));
//                     return new ModelAndView("redirect:grantList.html");
                     return new ModelAndView(getSuccessView());
                 }
             }
        }
//        return showForm(request, response, errors);
	}
	
	protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
		if (!isFormSubmission(request)) {
            String id = request.getParameter("id");
            ManagedGrant grant;
            if (id == null && !isAdd(request)) {
            	grant = (ManagedGrant) this.grantManager.get(ManagedGrant.class, new Long(id));
            } else if (!StringUtils.isBlank(id) && !"".equals(request.getParameter("version"))) {
            	grant = (ManagedGrant) this.grantManager.get(ManagedGrant.class, new Long(id));
            } else {
                grant = new ManagedGrant();
//                user.addRole(new Role(Constants.USER_ROLE));
            }
            return grant;
		}else if (request.getParameter("id") != null && !"".equals(request.getParameter("id"))
                && request.getParameter("cancel") == null) {
            // populate user object from database, so all fields don't need to be hidden fields in form
            return this.grantManager.get(ManagedGrant.class, new Long(request.getParameter("id")));
        }
		return super.formBackingObject(request);
	}
	
    protected boolean isAdd(HttpServletRequest request) {
        String method = request.getParameter("method");
        return (method != null && method.equalsIgnoreCase("add"));
    }
}
