package com.focaplo.myfuse.webapp.controller.inventory;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.ChemicalShelves;
import com.focaplo.myfuse.model.ItemCategory;
import com.focaplo.myfuse.webapp.controller.BimeFormController;
@Controller
public class ItemCategoryFormController extends BimeFormController {

	@RequestMapping(value="/inventory/itemCategory/{itemCategoryId}/form.html", method=RequestMethod.POST)
	public String submitForm(@ModelAttribute("itemCategory")ItemCategory itemCategory, BindingResult result, HttpServletRequest request, SessionStatus sessionStatus, Locale locale)throws Exception {

        	Integer originalVersion = itemCategory.getVersion();
        	this.inventoryManager.save(itemCategory);
        	this.expireCachedObjects(ItemCategory.class, itemCategory.getId());
            saveMessage(request, getText("itemCategory.saved",itemCategory.getName(), locale));
            return "redirect:/inventory/itemCategories/list.html";
            
	}
	
	@ModelAttribute("itemCategory")
	public ItemCategory getItemCategory(@PathVariable(value="itemCategoryId") Long id,  HttpServletRequest request){
       return (ItemCategory) super.getModelObject(ItemCategory.class, id, "get".equalsIgnoreCase(request.getMethod()));
	}
	@RequestMapping(value="/inventory/itemCategory/{itemCategoryId}/form.html", method=RequestMethod.GET)
	public String showForm( Model model){
            return "/inventory/itemCategoryForm";
	}
	
}
