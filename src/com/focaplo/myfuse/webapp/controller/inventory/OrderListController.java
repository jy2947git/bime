package com.focaplo.myfuse.webapp.controller.inventory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.focaplo.myfuse.model.ManagedOrder;
import com.focaplo.myfuse.webapp.controller.BimeListController;
@Controller
public class OrderListController extends BimeListController {

	@Override
	public Class getModelClass() {
		return ManagedOrder.class;
	}

	@RequestMapping(value="/inventory/orders/list.html", method=RequestMethod.GET)
	public String displayList(Model model){
		model.addAttribute( this.getListAttributeName(),super.getListOfSecuredObjects(null));
		return "/inventory/orderList";
	}
	
	
	@RequestMapping(value="/inventory/order/{orderId}/delete.html", method=RequestMethod.POST)
	public String delete(@PathVariable(value="orderId") Long orderId, Model model){
		super.deleteSelectedObject(orderId);
		model.addAttribute(this.getListAttributeName(), super.getListOfSecuredObjects(null));
		return "inventory/include/include_orderListTable";

	}
}
