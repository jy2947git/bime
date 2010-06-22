package com.focaplo.myfuse.webapp.controller.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.ChemicalShelves;
import com.focaplo.myfuse.model.InventoryAudit;
import com.focaplo.myfuse.model.ItemCategory;
import com.focaplo.myfuse.model.ManagedItem;
import com.focaplo.myfuse.model.Storage;
import com.focaplo.myfuse.model.StorageSection;
import com.focaplo.myfuse.model.Storagible;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.webapp.controller.BimeFormController;
import com.focaplo.myfuse.webapp.wrapper.ManagedItemWrapper;
@Controller

public class InventoryItemFormController extends BimeFormController {

	@RequestMapping(value="/inventory/item/{itemId}/form.html", method=RequestMethod.POST)
	public String submitForm(@ModelAttribute("managedItemWrapper")ManagedItemWrapper itemWrapper, BindingResult result, HttpServletRequest request, SessionStatus sessionStatus, Locale locale)throws Exception {

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
        	this.expireCachedObjects(ManagedItem.class, item.getId());
        	if(!StringUtils.isBlank(request.getParameter("notes"))){
        		notes.append(this.getText("item.history.notes.by", ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getFullName(), locale) + ":" + request.getParameter("notes"));
        	}
        	if(!StringUtils.isBlank(notes.toString())){
        		this.inventoryManager.addNotesToItem(item, notes.toString());
        	}

           saveMessage(request, getText("item.saved",item.getItemCategory().getName(), locale));

           return "redirect:/inventory/items/list.html";
             
	}
	
	@ModelAttribute("managedItemWrapper")
	public ManagedItemWrapper getManagedItemWrapper(@PathVariable(value="itemId") Long id, Model model){
		ManagedItemWrapper wrapper = new ManagedItemWrapper();
        ManagedItem item;
        if (id!=null && id.intValue()>0) {
        	item = (ManagedItem) this.inventoryManager.get(ManagedItem.class, id);
        } else {
        	item = new ManagedItem();
        }
		wrapper.setItemCategoryId(item.getItemCategory()==null?null:item.getItemCategory().getId());
		wrapper.setInventoryAmount(item.getAmount());
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
       return wrapper;
	}

	@RequestMapping(value="/inventory/item/{itemId}/form.html", method=RequestMethod.GET)
	public String showForm(){

            return "/inventory/itemForm";
		
	}
	

	@ModelAttribute("allStoragableList")
	public List<Storagible> getAllStoragibles(){
		return  this.inventoryManager.getAllStoragibles();
	}

	@ModelAttribute("itemCategoryList")
	public List<ItemCategory> getAllItemCategories(){
		return  super.getListOfModelObject(ItemCategory.class);
	}

	
    
    
}
