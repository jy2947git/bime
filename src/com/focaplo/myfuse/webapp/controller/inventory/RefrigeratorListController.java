package com.focaplo.myfuse.webapp.controller.inventory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.focaplo.myfuse.model.Refrigerator;
import com.focaplo.myfuse.webapp.controller.BimeListController;
@Controller
public class RefrigeratorListController extends BimeListController {

	@Override
	public Class getModelClass() {
		return Refrigerator.class;
	}

	@RequestMapping(value="/inventory/refrigerators/list.html", method=RequestMethod.GET)
	public String displayList(Model model){
		model.addAttribute( this.getListAttributeName(),super.getListOfSecuredObjects(null));
		return "/inventory/refrigeratorList";
	}
	
	
	@RequestMapping(value="/inventory/refrigerator/{refrigeratorId}/delete.html", method=RequestMethod.POST)
	public String delete(@PathVariable(value="refrigeratorId") Long refrigeratorId, Model model){
		super.deleteSelectedObject(refrigeratorId);
		model.addAttribute(this.getListAttributeName(), super.getListOfSecuredObjects(null));
		return "inventory/include/include_refrigeratorListTable";

	}



}
