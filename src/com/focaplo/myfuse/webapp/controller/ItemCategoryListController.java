package com.focaplo.myfuse.webapp.controller;

import com.focaplo.myfuse.model.ItemCategory;

public class ItemCategoryListController extends BaseListController {

	@Override
	public Class getModelClass() {
		return ItemCategory.class;
	}


}
