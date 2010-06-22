package com.focaplo.myfuse.webapp.controller.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.service.DataSourceService;
import com.focaplo.myfuse.service.LabService;

@Controller(value="removeLabController")
@Deprecated
public class RemoveLabController {
	protected final transient Log log = LogFactory.getLog(getClass());
	@Autowired
	private DataSourceService dynDataSourceRegister;
	@Autowired
	private LabService labManager;
	
	public void setLabManager(LabService labManager) {
		this.labManager = labManager;
	}

	public void setDynDataSourceRegister(DataSourceService dynDataSourceRegister) {
		this.dynDataSourceRegister = dynDataSourceRegister;
	}

	@RequestMapping("/removeLab.html")
	public ModelAndView handleRequest(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		//FIXME refactor to lab controller
		String labName = req.getParameter("name");
		//remove from the runtime data source routing
		this.dynDataSourceRegister.removeDataSource(labName);
		//remove lab and its storage
		this.labManager.removeLabByName(labName);
		return null;
	}

}
