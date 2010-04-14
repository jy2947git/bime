package com.focaplo.myfuse.webapp.controller;

import com.focaplo.myfuse.model.ManagedItem;


public class InventoryItemListController extends BaseListController {

	@Override
	public Class getModelClass() {
		return ManagedItem.class;
	}



}
