package com.focaplo.myfuse.webapp.controller;

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
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.ChemicalShelves;
import com.focaplo.myfuse.model.StorageOthers;
import com.focaplo.myfuse.model.StorageSection;
import com.focaplo.myfuse.service.InventoryService;
import com.focaplo.myfuse.service.UserService;
import com.focaplo.myfuse.webapp.support.UserConverter;

public class ChemicalShelvesFormController extends BaseFormController {
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



	public ChemicalShelvesFormController() {
		super();
		setCommandName("chemicalShelves");
	    setCommandClass(ChemicalShelves.class);
		
	}

	
	public ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command,
            BindException errors)throws Exception {
		log.debug("entering 'onSubmit' method...");
		ChemicalShelves chemicalShelves = (ChemicalShelves)command;
		Locale locale = request.getLocale();

		if (request.getParameter("method") != null && request.getParameter("method").equalsIgnoreCase("deleteSection")) {
            this.inventoryManager.deleteStorageSection(new Long(request.getParameter("id")));
            saveMessage(request, getText("refrigerator.section.deleted", chemicalShelves.getName(), locale));

            return new ModelAndView(getSuccessView());
        } else {
        	Integer originalVersion = chemicalShelves.getVersion();
        	
        	String alias = this.getText("chemicalShelve", locale)+ " " + chemicalShelves.getName() + " " + chemicalShelves.getType() + " " + chemicalShelves.getLocation();
        	chemicalShelves.setAlias(alias.trim());
        	if(!chemicalShelves.getSections().isEmpty()){
        		for(StorageSection ss:chemicalShelves.getSections()){
        			ss.setAlias(chemicalShelves.getAlias() + " " + this.getText("section", locale) + " " + ss.getName());
        		}
        	}
        	this.inventoryManager.saveStorage(chemicalShelves);
            saveMessage(request, getText("chemicalShelves.saved",chemicalShelves.getName(), locale));
            if(originalVersion==null){
            	//first time created, go back to form page to add section
                return new ModelAndView("redirect:chemicalShelvesForm.html?from=list&id="+chemicalShelves.getId());
            }else{
            	return new ModelAndView(getSuccessView());
            }

        }

	}
	
	protected Object formBackingObject(HttpServletRequest request)
    throws Exception {

            String id = request.getParameter("id");
            ChemicalShelves chemicalShelves;
            if (!StringUtils.isBlank(id)) {
            	chemicalShelves = (ChemicalShelves) this.inventoryManager.get(ChemicalShelves.class, new Long(id));
            } else {
                chemicalShelves = new ChemicalShelves();
            }
            return chemicalShelves;
		
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
