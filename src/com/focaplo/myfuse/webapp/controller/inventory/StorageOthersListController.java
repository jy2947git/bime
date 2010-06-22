package com.focaplo.myfuse.webapp.controller.inventory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.focaplo.myfuse.model.StorageOthers;
import com.focaplo.myfuse.webapp.controller.BimeListController;
@Controller
public class StorageOthersListController extends BimeListController {

	@Override
	public Class getModelClass() {
		return StorageOthers.class;
	}

	@RequestMapping(value="/inventory/storageOthers/list.html", method=RequestMethod.GET)
	public String displayList(Model model){
		model.addAttribute( this.getListAttributeName(),super.getListOfSecuredObjects(null));
		return "/inventory/othersList";
	}
	
	
	@RequestMapping(value="/inventory/storageOthers/{storageOthersId}/delete.html", method=RequestMethod.POST)
	public String delete(@PathVariable(value="storageOthersId") Long storageOthersId, Model model){
		super.deleteSelectedObject(storageOthersId);
		model.addAttribute(this.getListAttributeName(), super.getListOfSecuredObjects(null));
		return "inventory/include/include_storageOthersListTable";

	}
}
