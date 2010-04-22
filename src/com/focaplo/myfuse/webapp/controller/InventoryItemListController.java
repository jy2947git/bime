package com.focaplo.myfuse.webapp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.ManagedItem;
import com.focaplo.myfuse.model.Storagible;
import com.focaplo.myfuse.service.InventoryService;


public class InventoryItemListController extends BaseListController {
	@Autowired
	private InventoryService inventoryManager;


	
	public void setInventoryManager(InventoryService inventoryManager) {
		this.inventoryManager = inventoryManager;
	}
	@Override
	public Class getModelClass() {
		return ManagedItem.class;
	}


	@Override
	protected List getListOfModels(HttpServletRequest req) {
		// TODO Auto-generated method stub
		List<ManagedItem> items =  super.getListOfModels(req);
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



}
