package com.focaplo.myfuse.webapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.ItemCategory;
import com.focaplo.myfuse.model.ManagedItem;
import com.focaplo.myfuse.model.Storage;
import com.focaplo.myfuse.model.StorageSection;
import com.focaplo.myfuse.model.Storagible;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.InventoryService;
import com.focaplo.myfuse.webapp.wrapper.ManagedItemWrapper;

public class InventoryItemFormController extends BaseFormController {
	@Autowired
	private InventoryService inventoryManager;


	
	public void setInventoryManager(InventoryService inventoryManager) {
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

			ManagedItem item = itemWrapper.getManagedItem();
        	Integer originalVersion = item.getVersion();
        	Long selectedSectionId = itemWrapper.getStorageSectionId();
        	if(itemWrapper.getItemCategoryId()!=null){
        		item.setItemCategory((ItemCategory) this.inventoryManager.get(ItemCategory.class, itemWrapper.getItemCategoryId()));
        	}
        	//detect amount change
        	StringBuffer notes = new StringBuffer();
        	if(item.getId()==null){
        		notes.append(this.getText("item.amount.added.by.peron", new Object[]{((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getFullName(), item.getAmount()}, locale));
        	}else{
        		
        		Integer before = itemWrapper.getInventoryAmount()==null?new Integer("0"):itemWrapper.getInventoryAmount();
        		Integer after = item.getAmount()==null?new Integer("0"):item.getAmount();
        		if(before.intValue()<after.intValue()){
        			notes.append(this.getText("item.amount.added.by.person", new Object[]{((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getFullName(), new Integer(after.intValue()-before.intValue())},locale));
        		}else if(before.intValue()>after.intValue()){
        			notes.append(this.getText("item.amount.reduced.by.person", new Object[]{((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getFullName(), new Integer(before.intValue()-after.intValue())},locale));
        		}
        	}
        	this.inventoryManager.save(item);
        	
        	if(!StringUtils.isBlank(request.getParameter("notes"))){
        		notes.append(this.getText("item.history.notes.by", ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getFullName(), locale) + ":" + request.getParameter("notes"));
        	}
        	if(!StringUtils.isBlank(notes.toString())){
        		this.inventoryManager.addNotesToItem(item, notes.toString());
        	}

                 saveMessage(request, getText("item.saved",item.getItemCategory().getName(), locale));

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
		
		if(item.getStorigibleUniqueId()!=null){
			//find the real storage type and id
			String[] segs = item.getStorigibleUniqueId().split("-");
			String storageClassSimpleName = segs[0];
			String storageOrSectionId = segs[1];
			if(storageClassSimpleName.equalsIgnoreCase(StorageSection.class.getSimpleName())){
				//it is section
				StorageSection ss = (StorageSection)this.inventoryManager.get(StorageSection.class, new Long(storageOrSectionId));
				if(ss!=null){
					wrapper.setStorigibleAlias(ss.getAlias());
				}else{
					throw new RuntimeException("Thought that is a storage section, but could not find it by id " + storageOrSectionId);
				}
			}else if(storageClassSimpleName.equalsIgnoreCase(Storage.class.getSimpleName())){
				//it is storage level
				Storage s = (Storage)this.inventoryManager.get(Storage.class, new Long(storageOrSectionId));
				if(s!=null){
					wrapper.setStorigibleAlias(s.getAlias());
				}else{
					throw new RuntimeException("Thought that is a storage, but could not find it by id " + storageOrSectionId);
				}
			}else{
				throw new UnsupportedOperationException("Unsupported storageClassSimpleName=" + storageClassSimpleName + " from " +  item.getStorigibleUniqueId());
			}
			//stored at section level
			
		}
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
		List<Storagible> allStoragibles = this.inventoryManager.getAllStoragibles();
		map.put("allStoragableList", allStoragibles);
		map.put("itemCategoryList", this.inventoryManager.getAll(ItemCategory.class));
		if(command!=null){
			ManagedItemWrapper item = (ManagedItemWrapper)command;
		
			map.put("inventoryAudits", new ArrayList(item.getManagedItem().getInventoryAudits()));
		}
		return map;
	}
    
    
}
