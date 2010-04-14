package com.focaplo.myfuse.webapp.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.ItemCategory;
import com.focaplo.myfuse.service.InventoryManager;

public class ItemCategoryFormController extends BaseFormController {

	private InventoryManager inventoryManager;


	
	public void setInventoryManager(InventoryManager inventoryManager) {
		this.inventoryManager = inventoryManager;
	}



	public ItemCategoryFormController() {
		super();
		setCommandName("itemCategory");
	    setCommandClass(ItemCategory.class);
		
	}

	
	public ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command,
            BindException errors)throws Exception {
		log.debug("entering 'onSubmit' method...");
		ItemCategory itemCategory = (ItemCategory)command;
		Locale locale = request.getLocale();

        	Integer originalVersion = itemCategory.getVersion();
        	this.inventoryManager.save(itemCategory);
        	
                 saveMessage(request, getText("itemCategory.saved",itemCategory.getName(), locale));
                 return new ModelAndView(getSuccessView());
            
	}
	
	protected Object formBackingObject(HttpServletRequest request)
    throws Exception {

            String id = request.getParameter("id");
            ItemCategory itemCategory;
            if(!StringUtils.isBlank(id)) {
            	itemCategory = (ItemCategory) this.inventoryManager.get(ItemCategory.class, new Long(id));
            } else {
                itemCategory = new ItemCategory();
            }
            return itemCategory;
		
	}
	
}
