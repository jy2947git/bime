package com.focaplo.myfuse.webapp.controller.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.service.LabService;
import com.focaplo.myfuse.service.impl.DynDataSourceRegister;
@Controller(value="createLabController")
@Deprecated
public class CreateLabController  {
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

	@RequestMapping("/addLab.html")
	@ResponseBody
	public String handleRequest(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		String file = req.getParameter("file");
		log.info("adding new data source with:" + file);
		//FIXME password!
		this.dynDataSourceRegister.addDataSourceFromPropertyFile(file);
		//create the Lab record and prepare storage for it
		Lab lab = new Lab();
		lab.setName(req.getParameter("name"));
		
		this.labManager.saveLab(lab);
		this.labManager.setupStorageForLab(lab);
		//JSON
		ObjectMapper mapper = new ObjectMapper();
		String json =  mapper.writeValueAsString(lab);
		return json;
	}

}
