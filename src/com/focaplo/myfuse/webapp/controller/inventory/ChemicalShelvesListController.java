package com.focaplo.myfuse.webapp.controller.inventory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.focaplo.myfuse.model.BaseObject;
import com.focaplo.myfuse.model.ChemicalShelves;
import com.focaplo.myfuse.webapp.controller.BimeListController;
@Controller(value="chemicalShelvesListController")
public class ChemicalShelvesListController extends BimeListController {

	@Override
	public Class<? extends BaseObject> getModelClass() {
		return ChemicalShelves.class;
	}
	@RequestMapping(value="/inventory/chemicalShelves/list.html", method=RequestMethod.GET)
	public String displayList(Model model){
		model.addAttribute(this.getListAttributeName(),super.getListOfSecuredObjects(null));
		return "/inventory/chemicalShelvesList";
	}
	
	
	@RequestMapping(value="/inventory/chemicalShelve/{chemicalShelveId}/delete.html", method=RequestMethod.POST)
	public String delete(@PathVariable(value="chemicalShelveId") Long chemicalShelveId, Model model){
		super.deleteSelectedObject(chemicalShelveId);
		model.addAttribute(this.getListAttributeName(), super.getListOfSecuredObjects(null));
		return "inventory/include/include_chemicalShelvesListTable";

	}

}
