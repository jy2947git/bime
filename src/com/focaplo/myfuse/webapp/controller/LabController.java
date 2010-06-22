package com.focaplo.myfuse.webapp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.service.LabService;
import com.focaplo.myfuse.service.UserService;
import com.focaplo.myfuse.webapp.support.UserConverter;

@Controller
@RequestMapping("/lab")
public class LabController {
	@Autowired
	private LabService labManager;
	@Autowired
	private UserService userManager;
	@Autowired
	private UserConverter userConverter;
	
	@RequestMapping(value="/form", method=RequestMethod.GET)
	public String showForm(Map<String, Object> model){
		model.put("lab", new Lab());
		return "form";
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public String showForm(@PathVariable Long id, Map<String, Object> model){
		Lab lab = (Lab) this.labManager.get(Lab.class, id);
		model.put("lab", lab);
		return "form";
	}
	
	@RequestMapping(value="/filter", method=RequestMethod.GET)
	public String search(Map<String, Object> model){
		List<Lab> labs = (List<Lab>) this.labManager.getAll(Lab.class);
		model.put("labs", labs);
		return "list";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String submitForm(Lab lab){
		this.labManager.saveLab(lab);
		return "redirect:/" + lab.getId();
	}

	
	
}
