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

import com.focaplo.myfuse.model.StorageOthers;
import com.focaplo.myfuse.model.StorageSection;
import com.focaplo.myfuse.service.InventoryService;
import com.focaplo.myfuse.service.UserService;
import com.focaplo.myfuse.webapp.support.UserConverter;

public class StorageOthersFormController extends BaseFormController {
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



	public StorageOthersFormController() {
		super();
		setCommandName("storageOthers");
	    setCommandClass(StorageOthers.class);
		
	}

	
	public ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command,
            BindException errors)throws Exception {
		log.debug("entering 'onSubmit' method...");
		StorageOthers storageOthers = (StorageOthers)command;
		Locale locale = request.getLocale();

		if (request.getParameter("method") != null && request.getParameter("method").equalsIgnoreCase("deleteSection")) {
            this.inventoryManager.deleteStorageSection(new Long(request.getParameter("id")));
            saveMessage(request, getText("refrigerator.section.deleted", storageOthers.getName(), locale));

            return new ModelAndView(getSuccessView());
        } else {
        	Integer originalVersion = storageOthers.getVersion();
        	
        	String alias = this.getText("storage", locale)+ " " + storageOthers.getName() + " " + storageOthers.getType() + " " + storageOthers.getLocation();
        	storageOthers.setAlias(alias.trim());
        	if(!storageOthers.getSections().isEmpty()){
        		for(StorageSection ss:storageOthers.getSections()){
        			ss.setAlias(storageOthers.getAlias() + " " + this.getText("section", locale) + " " + ss.getName());
        		}
        	}
        	this.inventoryManager.saveStorage(storageOthers);
        	saveMessage(request, getText("storageOthers.saved",storageOthers.getName(), locale));
        	if(originalVersion==null){
            	//first time created, go back to form page to add section
                return new ModelAndView("redirect:othersForm.html?from=list&id="+storageOthers.getId());
            }else{
            	return new ModelAndView(getSuccessView());
            }

        }

	}
	
	protected Object formBackingObject(HttpServletRequest request)
    throws Exception {

            String id = request.getParameter("id");
            StorageOthers storageOthers;
            if (!StringUtils.isBlank(id)) {
            	storageOthers = (StorageOthers) this.inventoryManager.get(StorageOthers.class, new Long(id));
            } else {
                storageOthers = new StorageOthers();
            }
            return storageOthers;
		
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
