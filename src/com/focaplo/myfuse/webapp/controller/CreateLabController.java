package com.focaplo.myfuse.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.service.LabService;
import com.focaplo.myfuse.webapp.spring.DynDataSourceRegister;

public class CreateLabController implements Controller {
	protected final transient Log log = LogFactory.getLog(getClass());
	@Autowired
	private DynDataSourceRegister dynDataSourceRegister;
	@Autowired
	private LabService labManager;
	
	public void setLabManager(LabService labManager) {
		this.labManager = labManager;
	}

	public void setDynDataSourceRegister(DynDataSourceRegister dynDataSourceRegister) {
		this.dynDataSourceRegister = dynDataSourceRegister;
	}

	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		String file = req.getParameter("file");
		log.info("adding new data source with:" + file);
		//FIXME password!
		this.dynDataSourceRegister.addDataSource(file);
		//create the Lab record and prepare storage for it
		Lab lab = new Lab();
		lab.setName(req.getParameter("name"));
		
		this.labManager.saveLab(lab);
		this.labManager.setupStorageForLab(lab);
		return null;
	}

}
