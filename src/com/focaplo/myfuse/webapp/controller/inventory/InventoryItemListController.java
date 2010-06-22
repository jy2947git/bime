package com.focaplo.myfuse.webapp.controller.inventory;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.BaseObject;
import com.focaplo.myfuse.model.ManagedItem;
import com.focaplo.myfuse.model.Storagible;
import com.focaplo.myfuse.service.InventoryService;
import com.focaplo.myfuse.webapp.controller.BimeListController;

@Controller(value="inventoryItemListController")
public class InventoryItemListController extends BimeListController {


	/* (non-Javadoc)
	 * @see com.focaplo.myfuse.webapp.controller.BimeListController#getListOfModels(java.lang.Long)
	 * must override to set the storagible alias
	 */
	@Override
	protected List getListOfModels(Long parentId)  {
		List<ManagedItem> items =  super.getListOfModels(parentId);
		List<Storagible> allStoragibles = this.inventoryManager.getAllStoragibles();
		//replace the item's storagible id with alias for display purpose
		for(ManagedItem item:items){
			for(Storagible s:allStoragibles){
				if(item.getStorigibleUniqueId()!=null && item.getStorigibleUniqueId().equalsIgnoreCase(s.getStorigibleUniqueId())){
					item.setStorigibleUniqueId(s.getAlias());
				}
			}
		}
		return items;
	}

	@RequestMapping(value="/inventory/items/list.html", method=RequestMethod.GET)
	public String displayList(Model model){
		model.addAttribute( this.getListAttributeName(),super.getListOfSecuredObjects(null));
		return "/inventory/itemList";
		
	}
	
	
	@RequestMapping(value="/inventory/item/{itemId}/delete.html", method=RequestMethod.POST)
	public String delete(@PathVariable(value="itemId") Long itemId, Model model){
		super.deleteSelectedObject(itemId);
		model.addAttribute( this.getListAttributeName(),super.getListOfSecuredObjects(null));

		return "inventory/include/include_itemListTable";

	}

	@Override
	public Class<? extends BaseObject> getModelClass() {
		return ManagedItem.class;
	}

}
