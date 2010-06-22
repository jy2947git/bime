package com.focaplo.myfuse.service;

import java.util.Date;

import org.junit.Test;

import com.focaplo.myfuse.model.ManagedOrder;

public class OrderManagerTest extends BaseManagerTestCase {
	@Test
	public void testSaveNewsOrder(){
		ManagedOrder order = new ManagedOrder();
		order.setAccountNumber("12345" + new Date());
		order.setFundName("fund" + new Date());
		order.setSalesFirstName("Jerry");
		order.setSalesLastName("You");
//		OrderManager om = (OrderManager)this.applicationContext.getBean("orderManager");
		orderManager.saveOrder(order);
		log.info("order id=" + order.getId());
	}
	@Test
	public void testSubmitOrder(){
//		OrderManager om = (OrderManager)this.applicationContext.getBean("orderManager");
		
		orderManager.submitOrder(new Long("3"), new Long("2"), new Long("2"));
	}
	
	@Test
	public void testCopyOrder(){
		Long newOrderId = orderManager.copyExistingOrder((ManagedOrder) orderManager.get(ManagedOrder.class, new Long("2")));
		log.info("new order:" + newOrderId);
	}
}
