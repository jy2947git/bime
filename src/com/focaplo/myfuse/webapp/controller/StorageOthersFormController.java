package com.focaplo.myfuse.webapp.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.StorageOthers;
import com.focaplo.myfuse.service.InventoryManager;

public class StorageOthersFormController extends BaseFormController {

	private InventoryManager inventoryManager;


	
	public void setInventoryManager(InventoryManager inventoryManager) {
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
        	this.inventoryManager.saveStorage(storageOthers);
            saveMessage(request, getText("storageOthers.saved",storageOthers.getName(), locale));
            return new ModelAndView(getSuccessView());

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

}
