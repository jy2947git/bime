package com.focaplo.myfuse.webapp.controller.inventory;

import java.util.List;
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

import com.focaplo.myfuse.model.StorageOthers;
import com.focaplo.myfuse.model.StorageSection;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.webapp.controller.BimeFormController;
import com.focaplo.myfuse.webapp.support.UserConverter;
@Controller
public class StorageOthersFormController extends BimeFormController {

	@Autowired
	private UserConverter userConverter;


	
	@RequestMapping(value="/inventory/storageOthers/{storageOthersId}/form.html", method=RequestMethod.POST)
	public String submitForm(@ModelAttribute("storageOthers")StorageOthers storageOthers, BindingResult result, HttpServletRequest request, SessionStatus sessionStatus, Locale locale)throws Exception {
        	Integer originalVersion = storageOthers.getVersion();
        	
        	String alias = this.getText("storageOthers", locale)+ " " + storageOthers.getName() + " " + storageOthers.getType() + " " + storageOthers.getLocation();
        	storageOthers.setAlias(alias.trim());
           	if(!storageOthers.getSections().isEmpty()){
        		for(StorageSection ss:storageOthers.getSections()){
        			ss.setAlias(storageOthers.getAlias() + " " + this.getText("section",locale) + " " + ss.getName());
        		}
        	}
        	this.inventoryManager.saveStorage(storageOthers);
        	this.expireCachedObjects(StorageOthers.class, storageOthers.getId());
            saveMessage(request, getText("storageOthers.saved",storageOthers.getName(), locale));
        
            if(originalVersion==null){
            	//first time created, go back to form page to add section
                return "redirect:/inventory/storageOthers/"+storageOthers.getId()+"/form.html";
            }else{
            	return "redirect:/inventory/storageOthers/list.html";
            }

        

	}
	
	@ModelAttribute("storageOthers")
	public StorageOthers getStorageOthers(@PathVariable(value="storageOthersId") Long id,  HttpServletRequest request)
    throws Exception {
           return (StorageOthers) super.getModelObject(StorageOthers.class, id, "get".equalsIgnoreCase(request.getMethod()));
		
	}
	@RequestMapping(value="/inventory/storageOthers/{storageOthersId}/form.html", method=RequestMethod.GET)
	public String showForm(@PathVariable(value="storageOthersId") Long id, Model model){ 
            //for the section page
            model.addAttribute("storageId", id);
            return "/inventory/othersForm";
		
	}
	@ModelAttribute("userList")
	protected List<User> getUsers(){
		return super.getLabUsers();

	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		
		binder.registerCustomEditor(com.focaplo.myfuse.model.User.class, this.userConverter);
		
	}
}
