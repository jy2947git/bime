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

import com.focaplo.myfuse.model.ChemicalShelves;
import com.focaplo.myfuse.model.StorageSection;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.webapp.controller.BimeFormController;
import com.focaplo.myfuse.webapp.support.UserConverter;

@Controller
public class ChemicalShelvesFormController extends BimeFormController {
		@Autowired
	private UserConverter userConverter;

	
	@RequestMapping(value="/inventory/chemicalShelve/{chemicalShelveId}/form.html", method=RequestMethod.POST)
	public String submitForm(@ModelAttribute("chemicalShelves")ChemicalShelves chemicalShelves, BindingResult result, HttpServletRequest request, SessionStatus sessionStatus, Locale locale)throws Exception {
        	Integer originalVersion = chemicalShelves.getVersion();
        	
        	String alias = this.getText("chemicalShelve", locale)+ " " + chemicalShelves.getName() + " " + chemicalShelves.getType() + " " + chemicalShelves.getLocation();
        	chemicalShelves.setAlias(alias.trim());
           	if(!chemicalShelves.getSections().isEmpty()){
        		for(StorageSection ss:chemicalShelves.getSections()){
        			ss.setAlias(chemicalShelves.getAlias() + " " + this.getText("section",locale) + " " + ss.getName());
        		}
        	}
        	this.inventoryManager.saveStorage(chemicalShelves);
        	this.expireCachedObjects(ChemicalShelves.class, chemicalShelves.getId());
            saveMessage(request, getText("chemicalShelves.saved",chemicalShelves.getName(), locale));
        
            if(originalVersion==null){
            	//first time created, go back to form page to add section
                return "redirect:/inventory/chemicalShelve/"+chemicalShelves.getId()+"/form.html";
            }else{
            	return "redirect:/inventory/chemicalShelves/list.html";
            }

        

	}


	@ModelAttribute()
	public ChemicalShelves getChemicalShelve(@PathVariable(value="chemicalShelveId") Long id, HttpServletRequest request){
		return (ChemicalShelves) this.getModelObject(ChemicalShelves.class, id, "get".equalsIgnoreCase(request.getMethod()));
	}
	@RequestMapping(value="/inventory/chemicalShelve/{chemicalShelveId}/form.html", method=RequestMethod.GET)
	public String showForm(@PathVariable(value="chemicalShelveId") Long id, Model model){

            
            //for the section page
            model.addAttribute("storageId", id);
            return "/inventory/chemicalShelvesForm";
		
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
