package com.focaplo.myfuse.webapp.controller.lab;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.focaplo.myfuse.model.ExperimentProtocol;
import com.focaplo.myfuse.webapp.controller.BimeFormController;

@Controller("protocolFormController")
public class ProtocolFormController extends BimeFormController{



	@RequestMapping(value="/project/protocol/{protocolId}/form.html", method=RequestMethod.GET)
	public String showForm(){
         return "/project/protocolForm";
	}
	
	@RequestMapping(value="/project/protocol/{protocolId}/form.html", method=RequestMethod.POST)
	public String submitForm(@ModelAttribute("experimentProtocol") ExperimentProtocol protocol, BindingResult result, @RequestParam(value="newVersion", required=false, defaultValue="false") Boolean newVersion, HttpServletRequest request, SessionStatus sessionStatus, Locale locale)throws Exception {
        if(protocol.getId()==null){
        	protocol.setCreatedByName(this.getLoginUser().getFullName());
        }
        this.projectManager.saveProtocol(protocol, newVersion, this.getLoginUser().getUsername());
        this.expireCachedObjects(ExperimentProtocol.class, protocol.getId());
       saveMessage(request, getText("protocol.saved", protocol.getName(), locale));
       return "redirect:/project/protocols/list.html";

	}
	

	@ModelAttribute("experimentProtocol")
	public ExperimentProtocol ExperimentProtocol(@PathVariable("protocolId") Long id,  HttpServletRequest request){
		return (ExperimentProtocol) super.getModelObject(ExperimentProtocol.class, id, "get".equalsIgnoreCase(request.getMethod()));
	}

}
