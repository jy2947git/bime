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
import com.focaplo.myfuse.model.Role;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.InventoryService;
import com.focaplo.myfuse.service.UserService;
import com.focaplo.myfuse.webapp.support.UserConverter;
import com.focaplo.myfuse.webapp.util.RequestUtil;

public class EquipmentFormController extends BaseFormController {
	@Autowired
	private InventoryService inventoryManager;

	@Autowired
	private UserService userManager;
	@Autowired
	private UserConverter userConverter;
	public void setUserManager(UserService userManager) {
		this.userManager = userManager;
	}
	
	public void setInventoryManager(InventoryService inventoryManager) {
		this.inventoryManager = inventoryManager;
	}



	public EquipmentFormController() {
		super();
		setCommandName("equipment");
	    setCommandClass(Equipment.class);
		
	}

	
	public ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command,
            BindException errors)throws Exception {
		log.debug("entering 'onSubmit' method...");
		Equipment equipment = (Equipment)command;
		Locale locale = request.getLocale();

        	Integer originalVersion = equipment.getVersion();
        	this.inventoryManager.save(equipment);
        	 
                 saveMessage(request, getText("equipment.saved",equipment.getName(), locale));

                 return new ModelAndView(getSuccessView());
             
	}
	
	protected Object formBackingObject(HttpServletRequest request)
    throws Exception {

            String id = request.getParameter("id");
            Equipment equipment;
            if (!StringUtils.isBlank(id)) {
            	equipment = (Equipment) this.inventoryManager.get(Equipment.class, new Long(id));
            } else {
                equipment = new Equipment();

            }
            return equipment;
		
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
