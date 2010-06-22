package com.focaplo.myfuse.webapp.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.focaplo.myfuse.service.CacheService;

@Controller
public class ServiceSwitchController {

	@Autowired
	CacheService cacheManager;

	public void setCacheManager(CacheService cacheManager) {
		this.cacheManager = cacheManager;
	}
	
	
	@RequestMapping(value="/support/cache/turnOn.html", method=RequestMethod.POST)
	@ResponseBody
	public String turnOnCache(){
		cacheManager.turnOn();
		return "done";
	}
	
	@RequestMapping(value="/support/cache/turnOff.html", method=RequestMethod.POST)
	@ResponseBody
	public String turnOffCache(){
		cacheManager.turnOff();
		return "done";
	}

}
