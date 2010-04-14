package com.focaplo.myfuse.webapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.mail.MailException;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.focaplo.myfuse.Constants;
import com.focaplo.myfuse.model.ItemCategory;
import com.focaplo.myfuse.model.ManagedItem;
import com.focaplo.myfuse.model.Refrigerator;
import com.focaplo.myfuse.model.Role;
import com.focaplo.myfuse.model.Storage;
import com.focaplo.myfuse.model.StorageSection;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.InventoryManager;
import com.focaplo.myfuse.webapp.util.RequestUtil;
import com.focaplo.myfuse.webapp.wrapper.ManagedItemWrapper;

public class InventoryItemFormController extends BaseFormController {

	private InventoryManager inventoryManager;


	
	public void setInventoryManager(InventoryManager inventoryManager) {
		this.inventoryManager = inventoryManager;
	}



	public InventoryItemFormController() {
		super();
		setCommandName("managedItemWrapper");
	    setCommandClass(ManagedItemWrapper.class);
		
	}


	public ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command,
            BindException errors)throws Exception {
		log.debug("entering 'onSubmit' method...");
		ManagedItemWrapper itemWrapper = (ManagedItemWrapper)command;
		Locale locale = request.getLocale();


        	Integer originalVersion = itemWrapper.getManagedItem().getVersion();
        	Long selectedSectionId = itemWrapper.getStorageSectionId();
        	if(itemWrapper.getItemCategoryId()!=null){
        		itemWrapper.getManagedItem().setItemCategory((ItemCategory) this.inventoryManager.get(ItemCategory.class, itemWrapper.getItemCategoryId()));
        	}
        	//detect amount change
        	StringBuffer notes = new StringBuffer();
        	if(itemWrapper.getManagedItem().getId()==null){
        		notes.append(this.getText("item.amount.added.by.peron", new Object[]{((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getFullName(), itemWrapper.getManagedItem().getAmount()}, locale));
        	}else{
        		
        		Integer before = itemWrapper.getInventoryAmount()==null?new Integer("0"):itemWrapper.getInventoryAmount();
        		Integer after = itemWrapper.getManagedItem().getAmount()==null?new Integer("0"):itemWrapper.getManagedItem().getAmount();
        		if(before.intValue()<after.intValue()){
        			notes.append(this.getText("item.amount.added.by.person", new Object[]{((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getFullName(), new Integer(after.intValue()-before.intValue())},locale));
        		}else if(before.intValue()>after.intValue()){
        			notes.append(this.getText("item.amount.reduced.by.person", new Object[]{((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getFullName(), new Integer(before.intValue()-after.intValue())},locale));
        		}
        	}
        	ManagedItem item = this.inventoryManager.saveManagedItemToStorageSection(itemWrapper.getManagedItem(),selectedSectionId);
        	
        	if(!StringUtils.isBlank(request.getParameter("notes"))){
        		notes.append(this.getText("item.history.notes.by", ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getFullName(), locale) + ":" + request.getParameter("notes"));
        	}
        	if(!StringUtils.isBlank(notes.toString())){
        		this.inventoryManager.addNotesToItem(item, notes.toString());
        	}

                 saveMessage(request, getText("item.saved",itemWrapper.getManagedItem().getItemCategory().getName(), locale));

                 return new ModelAndView(getSuccessView());
             
	}
	
	protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
		ManagedItemWrapper wrapper = new ManagedItemWrapper();
		ManagedItem item = null;
		if(request.getParameter("cancel") != null){
			log.debug("it is cancel");
			wrapper.setManagedItem(new ManagedItem());
			return wrapper;
		}
		
		String id = StringUtils.trimToEmpty(request.getParameter("managedItem.id")) + StringUtils.trimToEmpty(request.getParameter("id"));
		if (id!=null && !StringUtils.isBlank(id)) {
            	item = (ManagedItem) this.inventoryManager.get(ManagedItem.class, new Long(id));
         } else {
                item = new ManagedItem();
         }

		wrapper.setManagedItem(item);
		wrapper.setStorageId(item.getStorageSection()==null?null:item.getStorageSection().getStorage().getId());
		wrapper.setStorageSectionId(item.getStorageSection()==null?null:item.getStorageSection().getId());
		wrapper.setItemCategoryId(item.getItemCategory()==null?null:item.getItemCategory().getId());
		wrapper.setInventoryAmount(item.getAmount());
		return wrapper;
	}
	
    protected boolean isAdd(HttpServletRequest request) {
        String method = request.getParameter("method");
        return (method != null && method.equalsIgnoreCase("add"));
    }



	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		Map<String,List> map = new HashMap<String,List>();
		map.put("storageList", this.inventoryManager.getAll(Storage.class));
		map.put("storageSectionList", this.inventoryManager.getAll(StorageSection.class));
		map.put("itemCategoryList", this.inventoryManager.getAll(ItemCategory.class));
		if(command!=null){
			ManagedItemWrapper item = (ManagedItemWrapper)command;
		
			map.put("inventoryAudits", new ArrayList(item.getManagedItem().getInventoryAudits()));
		}
		return map;
	}
    
    
}
