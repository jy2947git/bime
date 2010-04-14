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
import com.focaplo.myfuse.model.Role;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.InventoryManager;
import com.focaplo.myfuse.webapp.util.RequestUtil;

public class EquipmentFormController extends BaseFormController {

	private InventoryManager inventoryManager;


	
	public void setInventoryManager(InventoryManager inventoryManager) {
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
	

}
