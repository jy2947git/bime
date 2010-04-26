package com.focaplo.myfuse.webapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.focaplo.myfuse.Constants;
import com.focaplo.myfuse.model.Equipment;
import com.focaplo.myfuse.model.Refrigerator;
import com.focaplo.myfuse.model.Role;
import com.focaplo.myfuse.model.StorageSection;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.InventoryService;
import com.focaplo.myfuse.service.UserService;
import com.focaplo.myfuse.webapp.support.UserConverter;
import com.focaplo.myfuse.webapp.util.RequestUtil;

public class RefrigeratorFormController extends BaseFormController {
	@Autowired
	private InventoryService inventoryManager;
	@Autowired
	private UserService userManager;
	@Autowired
	private UserConverter userConverter;
	public void setUserManager(UserService userManager) {
		this.userManager = userManager;
	}


	public void setUserConverter(UserConverter userConverter) {
		this.userConverter = userConverter;
	}

	
	public void setInventoryManager(InventoryService inventoryManager) {
		this.inventoryManager = inventoryManager;
	}



	public RefrigeratorFormController() {
		super();
		setCommandName("refrigerator");
	    setCommandClass(Refrigerator.class);
		
	}

	public ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command,
            BindException errors)throws Exception {
		log.debug("entering 'onSubmit' method...");
		Refrigerator refrigerator = (Refrigerator)command;
		Locale locale = request.getLocale();

		if (request.getParameter("method") != null && request.getParameter("method").equalsIgnoreCase("deleteSection")) {
            this.inventoryManager.deleteStorageSection(new Long(request.getParameter("id")));
            saveMessage(request, getText("refrigerator.section.deleted", refrigerator.getName(), locale));

            return new ModelAndView(getSuccessView());
        } else {
        	Integer originalVersion = refrigerator.getVersion();
        	
        	String alias = this.getText("refrigerator", locale)+ " " + refrigerator.getName() + " " + refrigerator.getType() + " " + refrigerator.getLocation();
        	refrigerator.setAlias(alias.trim());
        	if(!refrigerator.getSections().isEmpty()){
        		for(StorageSection ss:refrigerator.getSections()){
        			ss.setAlias(refrigerator.getAlias() + " " + this.getText("section", locale) + " " + ss.getName());
        		}
        	}
        	this.inventoryManager.saveRefrigerator(refrigerator);
        	saveMessage(request, getText("refrigerator.saved",refrigerator.getName(), locale));
        	if(originalVersion==null){
            	//first time created, go back to form page to add section
                return new ModelAndView("redirect:refrigeratorForm.html?from=list&id="+refrigerator.getId());
            }else{
            	return new ModelAndView(getSuccessView());
            }
        }
//        return showForm(request, response, errors);
	}
	
	protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
		 Refrigerator refrigerator;
		String id = request.getParameter("id");
		if (!StringUtils.isBlank(id)) {
            	refrigerator = (Refrigerator) this.inventoryManager.get(Refrigerator.class, new Long(id));
        } else {
                refrigerator = new Refrigerator();
        }
	 return refrigerator;
	}

    
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		Map<String,List> map = new HashMap<String,List>();
		map.put("userList", this.userManager.getAllLabUsers());
		
	
		return map;
	}
	
	@Override
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) {
		
		super.initBinder(request, binder);
		binder.registerCustomEditor(com.focaplo.myfuse.model.User.class, this.userConverter);
		
	}
}
