package com.focaplo.myfuse.webapp.controller;

import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.webapp.controller.inventory.OrderItemController;

public class OrderItemControllerTest extends BaseControllerTest{
	@Test
	public void testSaveOrderItem() throws Exception{
		OrderItemController controller = applicationContext.getBean(OrderItemController.class);
		request.setRequestURI("/inventory/order/4/orderItem/0/form.json");
		request.setMethod("POST");
		request.addParameter("orderItemCategoryId", "2");
		request.addParameter("orderItemMaker","11");
		request.addParameter("orderItemAmount","1");
		request.addParameter("orderItemUnitPrice","2");
		request.addParameter("orderItemTotalCost","2");
		request.addParameter("orderItemSupplier","sss");
	
		//request.setQueryString("orderItemCategoryId=2&orderItemMaker=4&orderItemAmount=44&orderItemUnitPrice=4444&orderItemTotalCost=195536&orderItemSupplier=444444");
		ModelAndView mav = handlerAdapter.handle(request, response, controller);
		System.out.println(response.getContentAsString());
	}
	
	@Test
	public void testSaveBadOrderItem() throws Exception{
		OrderItemController controller = applicationContext.getBean(OrderItemController.class);
		request.setRequestURI("/inventory/order/4/orderItem/0/form.json");
		request.setMethod("POST");
		request.addParameter("orderItemCategoryId", "");
		request.addParameter("orderItemMaker","");
		request.addParameter("orderItemAmount","");
		request.addParameter("orderItemUnitPrice","");
		request.addParameter("orderItemTotalCost","");
		request.addParameter("orderItemSupplier","");
	
		//request.setQueryString("orderItemCategoryId=2&orderItemMaker=4&orderItemAmount=44&orderItemUnitPrice=4444&orderItemTotalCost=195536&orderItemSupplier=444444");
		ModelAndView mav = handlerAdapter.handle(request, response, controller);
		System.out.println(response.getContentAsString());
	}
	
	@Test
	public void testUpdate() throws Exception{
		OrderItemController controller = applicationContext.getBean(OrderItemController.class);
		request.setRequestURI("/inventory/order/4/orderItem/8/form.json");
		request.setMethod("POST");
		request.addParameter("orderItemCategoryId", "2");
		request.addParameter("orderItemMaker","aaa");
		request.addParameter("orderItemAmount","1");
		request.addParameter("orderItemUnitPrice","23");
		request.addParameter("orderItemTotalCost","23");
		request.addParameter("orderItemSupplier","aaaa");
		ModelAndView mav = handlerAdapter.handle(request, response, controller);
		System.out.println(response.getContentAsString());
	}
}
