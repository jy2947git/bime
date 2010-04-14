package com.focaplo.myfuse.webapp.controller;

import com.focaplo.myfuse.model.ManagedOrder;

public class OrderListController extends BaseListController {

	@Override
	public Class getModelClass() {
		return ManagedOrder.class;
	}

}
