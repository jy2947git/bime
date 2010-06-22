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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.focaplo.myfuse.model.Equipment;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.webapp.controller.BimeFormController;
import com.focaplo.myfuse.webapp.support.UserConverter;
@Controller

public class EquipmentFormController extends BimeFormController {

	@Autowired
	private UserConverter userConverter;



	public void setUserConverter(UserConverter userConverter) {
		this.userConverter = userConverter;
	}


	
	@RequestMapping(value="/inventory/equipment/{equipmentId}/form.html", method=RequestMethod.POST)
	public String submitForm(@PathVariable("equipmentId") Long equipmentId, @ModelAttribute("equipment") Equipment equipment, BindingResult result, @RequestParam(value="newVersion", required=false, defaultValue="false") Boolean newVersion, HttpServletRequest request, SessionStatus sessionStatus, Locale locale)throws Exception {

        	Integer originalVersion = equipment.getVersion();
        	this.expireCachedObjects(Equipment.class, equipmentId);
        	this.inventoryManager.save(equipment);
            saveMessage(request, getText("equipment.saved",equipment.getName(), locale));

            return "redirect:/inventory/equipments/list.html";
             
	}
	
	@ModelAttribute("equipment")
	public Equipment getEquipment(@PathVariable("equipmentId") Long equipmentId,  HttpServletRequest request){
		Equipment equipment = (Equipment) this.getModelObject(Equipment.class, equipmentId, "get".equalsIgnoreCase(request.getMethod()));
		equipment.getContactPerson();
		equipment.getLastUser();
		return equipment;
	}
	
	@RequestMapping(value="/inventory/equipment/{equipmentId}/form.html", method=RequestMethod.GET)
	public String showForm(@PathVariable("equipmentId") Long equipmentId, Model model){
           
            return "/inventory/equipmentForm";
		
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
