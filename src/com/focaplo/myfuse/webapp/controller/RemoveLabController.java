package com.focaplo.myfuse.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.service.LabManager;
import com.focaplo.myfuse.webapp.spring.DynDataSourceRegister;

public class RemoveLabController implements Controller {
	protected final transient Log log = LogFactory.getLog(getClass());
	private DynDataSourceRegister dynDataSourceRegister;
	@Autowired
	private LabManager labManager;
	
	public void setLabManager(LabManager labManager) {
		this.labManager = labManager;
	}

	public void setDynDataSourceRegister(DynDataSourceRegister dynDataSourceRegister) {
		this.dynDataSourceRegister = dynDataSourceRegister;
	}

	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		String labName = req.getParameter("name");
		//remove from the runtime data source routing
		this.dynDataSourceRegister.removeDataSource(labName);
		//remove lab and its storage
		this.labManager.removeLabByName(labName);
		return null;
	}

}
