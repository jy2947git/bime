package com.focaplo.myfuse.webapp.controller.inventory;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.focaplo.myfuse.model.Refrigerator;
import com.focaplo.myfuse.model.StorageSection;
import com.focaplo.myfuse.webapp.controller.BimeFormController;
import com.focaplo.myfuse.webapp.support.UserConverter;
@Controller
public class RefrigeratorFormController extends BimeFormController {
		@Autowired
	private UserConverter userConverter;
	

	public void setUserConverter(UserConverter userConverter) {
		this.userConverter = userConverter;
	}

	

	@RequestMapping(value="/inventory/refrigerator/{refrigeratorId}/form.html", method=RequestMethod.POST)
	public String submitForm(@ModelAttribute("refrigerator")Refrigerator refrigerator, BindingResult result,  HttpServletRequest request, SessionStatus sessionStatus, Locale locale)throws Exception {
		Integer originalVersion = refrigerator.getVersion();
		String alias = this.getText("refrigerator", locale)+ " " + refrigerator.getName() + " " + refrigerator.getType() + " " + refrigerator.getLocation();
    	refrigerator.setAlias(alias.trim());
       	if(!refrigerator.getSections().isEmpty()){
    		for(StorageSection ss:refrigerator.getSections()){
    			ss.setAlias(refrigerator.getAlias() + " " + this.getText("section",locale) + " " + ss.getName());
    		}
    	}
    	this.inventoryManager.saveStorage(refrigerator);
    	this.expireCachedObjects(Refrigerator.class, refrigerator.getId());
        saveMessage(request, getText("refrigerator.saved",refrigerator.getName(), locale));
    
        if(originalVersion==null){
        	//first time created, go back to form page to add section
            return "redirect:/inventory/refrigerator/"+refrigerator.getId()+"/form.html";
        }else{
        	return "redirect:/inventory/refrigerators/list.html";
        }
	}
	@ModelAttribute("refrigerator")
	public Refrigerator getRefrigerator(@PathVariable("refrigeratorId") Long id,  HttpServletRequest request)
    throws Exception {
		 return (Refrigerator) super.getModelObject(Refrigerator.class, new Long(id), "get".equalsIgnoreCase(request.getMethod()));

	}

	@RequestMapping(value="/inventory/refrigerator/{refrigeratorId}/form.html", method=RequestMethod.GET)
	public String showForm(Model model){
		model.addAttribute("userList", super.getLabUsers());
         return "/inventory/refrigeratorForm";
	}
    
	@InitBinder
	protected void initBinder(WebDataBinder binder){
	
		binder.registerCustomEditor(com.focaplo.myfuse.model.User.class, this.userConverter);
		
	}
}
