package com.focaplo.myfuse.webapp.controller.inventory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.focaplo.myfuse.model.ItemCategory;
import com.focaplo.myfuse.webapp.controller.BimeListController;
@Controller(value="itemCategoryListController")
public class ItemCategoryListController extends BimeListController {

	@Override
	public Class getModelClass() {
		return ItemCategory.class;
	}

	@RequestMapping(value="/inventory/itemCategories/list.html", method=RequestMethod.GET)
	public String displayList(Model model){
		model.addAttribute( this.getListAttributeName(),super.getListOfSecuredObjects(null));
		return "/inventory/itemCategoryList";
	}
	
	
	@RequestMapping(value="/inventory/itemCategory/{itemCategoryId}/delete.html", method=RequestMethod.POST)
	public String delete(@PathVariable(value="itemCategoryId") Long itemCategoryId, Model model){
		super.deleteSelectedObject(itemCategoryId);
		model.addAttribute(this.getListAttributeName(), super.getListOfSecuredObjects(null));
		return "inventory/include/include_itemCategoryListTable";

	}
}
