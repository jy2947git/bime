package com.focaplo.myfuse.webapp.controller;

import com.focaplo.myfuse.model.Equipment;

public class EquipmentListController extends BaseListController {

	@Override
	public Class getModelClass() {
		return Equipment.class;
	}
	

}
