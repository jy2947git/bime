package com.focaplo.myfuse.webapp.controller.lab;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.model.ExperimentProtocol;
import com.focaplo.myfuse.webapp.controller.BimeListController;

@Controller(value="protocolListController")
public class ProtocolListController extends BimeListController {


	/* (non-Javadoc)
	 * @see com.focaplo.myfuse.webapp.controller.BimeListController#getModelClass()
	 */
	public Class getModelClass() {
		return ExperimentProtocol.class;
	}
	
	@RequestMapping(value="/project/protocols/list.html", method=RequestMethod.GET)
	public String displayList(Model model){
		model.addAttribute( this.getListAttributeName(),super.getListOfSecuredObjects(null));
		return "/project/protocolList";
	}
	
	
	@RequestMapping(value="/project/protocol/{protocolId}/delete.html", method=RequestMethod.POST)
	public String delete(@PathVariable(value="protocolId") Long protocolId, Model model){
		super.deleteSelectedObject(protocolId);
		model.addAttribute(this.getListAttributeName(), super.getListOfSecuredObjects(null));
		return "project/include/include_protocolListTable";

	}
	
}
