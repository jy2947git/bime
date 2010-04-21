package com.focaplo.myfuse.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.focaplo.myfuse.Constants;
import com.focaplo.myfuse.model.Equipment;
import com.focaplo.myfuse.model.Storage;
import com.focaplo.myfuse.model.StorageOthers;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.InventoryService;

public class StorageOthersListController extends BaseListController {

	@Override
	public Class getModelClass() {
		return StorageOthers.class;
	}


}
