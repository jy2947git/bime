package com.focaplo.myfuse.webapp.controller;

import com.focaplo.myfuse.model.ExperimentProtocol;

public class ProtocolListController extends BaseListController {


	@Override
	public Class getModelClass() {
		return ExperimentProtocol.class;
	}

}
