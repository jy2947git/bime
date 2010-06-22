package com.focaplo.myfuse.webapp.controller.inventory;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.focaplo.myfuse.model.Equipment;
import com.focaplo.myfuse.webapp.controller.BimeListController;
@Controller(value="equipmentListController")
public class EquipmentListController extends BimeListController {

	@Override
	public Class getModelClass() {
		return Equipment.class;
	}
	
	@RequestMapping(value="/inventory/equipments/list.html", method=RequestMethod.GET)
	public String displayList(Model model){
		List<Equipment> objects = super.getListOfSecuredObjects(null);
		//when we are caching the objects, we have to retrieve the child data now, otherwise
		//there will be session issue since the session data in cached object not existing any more
		for(Equipment equipment:objects){
			equipment.getLastUser();
		}
		model.addAttribute( this.getListAttributeName(),objects);
		return "/inventory/equipmentList";
	}
	
	
	@RequestMapping(value="/inventory/equipment/{equipmentId}/delete.html", method=RequestMethod.POST)
	public String delete(@PathVariable(value="equipmentId") Long equipmentId, Model model){
		this.deleteSelectedObject(equipmentId);
		model.addAttribute( this.getListAttributeName(),super.getListOfSecuredObjects(null));
		return "inventory/include/include_equipmentListTable";

	}
}
