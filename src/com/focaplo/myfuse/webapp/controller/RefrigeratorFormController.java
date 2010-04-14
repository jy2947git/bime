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
import com.focaplo.myfuse.model.Refrigerator;
import com.focaplo.myfuse.model.Role;
import com.focaplo.myfuse.model.StorageSection;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.InventoryManager;
import com.focaplo.myfuse.webapp.util.RequestUtil;

public class RefrigeratorFormController extends BaseFormController {

	private InventoryManager inventoryManager;


	
	public void setInventoryManager(InventoryManager inventoryManager) {
		this.inventoryManager = inventoryManager;
	}



	public RefrigeratorFormController() {
		super();
		setCommandName("refrigerator");
	    setCommandClass(Refrigerator.class);
		
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
		Refrigerator refrigerator = (Refrigerator)command;
		Locale locale = request.getLocale();

        if (request.getParameter("delete") != null) {
            this.inventoryManager.deleteStorage(refrigerator.getId());
            saveMessage(request, getText("refrigerator.deleted", refrigerator.getName(), locale));

            return new ModelAndView(getSuccessView());
        } else if (request.getParameter("method") != null && request.getParameter("method").equalsIgnoreCase("deleteSection")) {
            this.inventoryManager.deleteStorageSection(new Long(request.getParameter("id")));
            saveMessage(request, getText("refrigerator.section.deleted", refrigerator.getName(), locale));

            return new ModelAndView(getSuccessView());
        } else {
        	Integer originalVersion = refrigerator.getVersion();
        	this.inventoryManager.saveRefrigerator(refrigerator);
        	 if (!StringUtils.equals(request.getParameter("from"), "list")) {
                 saveMessage(request, getText("refrigerator.saved",refrigerator.getName(), locale));

//                 // return to main Menu
//                 return new ModelAndView("redirect:refrigeratorList.html");
                 return new ModelAndView(getSuccessView());
             } else {
                 if (StringUtils.isBlank(request.getParameter("version"))) {
                     saveMessage(request, getText("refrigerator.added", refrigerator.getName(), locale));

                     // Send an account information e-mail
//                     message.setSubject(getText("signup.email.subject", locale));
//
//                     try {
//                         sendUserMessage(user, getText("newuser.email.message", user.getFullName(), locale),
//                                         RequestUtil.getAppURL(request));
//                     } catch (MailException me) {
//                         saveError(request, me.getCause().getLocalizedMessage());
//                     }
                     return new ModelAndView("redirect:refrigeratorForm.html?from=list&id="+refrigerator.getId());
//                     return new ModelAndView(getSuccessView());
                 } else {
                     saveMessage(request, getText("refrigerator.updated", refrigerator.getName(), locale));
//                     return new ModelAndView("redirect:refrigeratorList.html");
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
            Refrigerator refrigerator;
            if (id == null && !isAdd(request)) {
            	refrigerator = (Refrigerator) this.inventoryManager.get(Refrigerator.class, new Long(id));
            } else if (!StringUtils.isBlank(id) && !"".equals(request.getParameter("version"))) {
            	refrigerator = (Refrigerator) this.inventoryManager.get(Refrigerator.class, new Long(id));
            } else {
                refrigerator = new Refrigerator();
//                user.addRole(new Role(Constants.USER_ROLE));
            }
            return refrigerator;
		}else if (request.getParameter("id") != null && !"".equals(request.getParameter("id"))
                && request.getParameter("cancel") == null) {
            // populate user object from database, so all fields don't need to be hidden fields in form
            return this.inventoryManager.get(Refrigerator.class, new Long(request.getParameter("id")));
        }
		return super.formBackingObject(request);
	}
	
    protected boolean isAdd(HttpServletRequest request) {
        String method = request.getParameter("method");
        return (method != null && method.equalsIgnoreCase("add"));
    }
}
