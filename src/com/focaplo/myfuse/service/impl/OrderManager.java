package com.focaplo.myfuse.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

import com.focaplo.myfuse.Constants;
import com.focaplo.myfuse.model.ItemCategory;
import com.focaplo.myfuse.model.ManagedItem;
import com.focaplo.myfuse.model.ManagedOrder;
import com.focaplo.myfuse.model.OrderItem;
import com.focaplo.myfuse.service.InventoryService;
import com.focaplo.myfuse.service.OrderService;
@Service(value="orderManager")
public class OrderManager extends UniversalManager implements
		OrderService {
	@Autowired
	private InventoryService inventoryManager;

	public void setInventoryManager(InventoryService inventoryManager) {
		this.inventoryManager = inventoryManager;
	}

	public List<ManagedOrder> getOrdersForApproval(Long userId) {
		return this.orderDao.getOrdersForMyApproval(userId);
	}

	public List<ManagedOrder> getOrdersSubmittedBy(Long userId) {
		return this.orderDao.getOrdersSubmittedBy(userId);
	}

	public void deleteOrderItem(Long id) {
		this.orderDao.deleteOrderItem(id);
	}

	public void saveOrderItem(Long orderId, OrderItem orderItem) {
		if(orderItem.getId()!=null){
			log.warn("id should be null");
			orderItem.setId(null);
		}
		if(orderItem==null){
			log.error("storage can not be null and storage id can not be null " + orderItem.getOrder());
			return;
		}
		if(orderItem.getInventoryItem()==null && orderItem.getItemCategoryId()!=null){
			orderItem.setItemCategory((ItemCategory) this.get(ItemCategory.class, orderItem.getItemCategoryId()));
		}
		ManagedOrder order = (ManagedOrder) this.orderDao.get(ManagedOrder.class, orderId);
		if(order!=null){
			log.debug("find order " + orderId);
		}else{
			log.error("failed to locate the order " + orderId);
			throw new RuntimeException("failed to locate the order " + orderId);
		}
		orderItem.setOrder(order);
		
		this.syncOrderItemCost(orderItem);
		order.getOrderItems().add(orderItem);
		this.orderDao.save(orderItem);
		//
		this.syncOrderTotalCost(order);
	}

	private void syncOrderTotalCost(ManagedOrder order) {
		BigDecimal total = new BigDecimal(0);
		for(OrderItem item:order.getOrderItems()){
			total = total.add(item.getTotalCost());
		}
		if(total.compareTo(order.getTotalPrice())!=0){
			this.updateOrderTotalCost(order.getId(), total);
		}
	}

	private void updateOrderTotalCost(Long id, BigDecimal total) {
		this.orderDao.updateOrderTotalCost(id, total);
	}

	private void syncOrderItemCost(OrderItem item) {
		item.setTotalCost(item.getUnitPrice().multiply(new BigDecimal(item.getAmount())));
	}

	public void updateOrderItem(Long id, Long itemCategoryId, String maker, BigDecimal unitPrice, Integer amount, BigDecimal totalCost, String supplier) {
		OrderItem orderItem = (OrderItem) this.orderDao.get(OrderItem.class, id);
		if(orderItem.getOrder().getStatus().compareToIgnoreCase(Constants.order_inventoried)>0){
			throw new RuntimeException(orderItem.getOrder().getStatus());
		}

		this.orderDao.updateOrderItem(id, itemCategoryId, maker, unitPrice, amount, totalCost, supplier);
	}

	public List<OrderItem> getOrderItemsOfOrder(Long orderId) {
		return this.orderDao.getOrderItemsOfOrder(orderId);
	}

	public void deleteOrder(List<Long> toBeDeleted) {
		for(Long id:toBeDeleted){
			this.deleteOrder(id);
		}
	}

	public void deleteOrder(Long id) {
		ManagedOrder order = (ManagedOrder) this.orderDao.get(ManagedOrder.class, id);
		if(order.getStatus().compareToIgnoreCase(Constants.order_inventoried)>0){
			throw new RuntimeException(order.getStatus());
		}
		order.setStatus(Constants.order_cancelled);
		this.saveOrder(order);
	}

	public void saveOrder(ManagedOrder order) {
		if(StringUtils.isBlank(order.getStatus())){
			order.setStatus(Constants.order_draft);
			order.setCreatedDate(new Date());
			order.setLastUpdateDate(new Date());
		}
		
		
		this.orderDao.save(order);
	}
	
	public void submitOrder(Long orderId, Long submitedBy, Long forApprovalBy){
		ManagedOrder order = (ManagedOrder) this.orderDao.get(ManagedOrder.class, orderId);
		order.setSubmitDate(new Date());
		order.setStatus(Constants.order_submitted);
		order.setLastUpdateDate(new Date());
		order.setSubmittedByPerson(submitedBy);
		order.setApprovedByPerson(forApprovalBy);
		//order.setTotalPrice(totalPrice);
		this.saveOrder(order);
		
	}



	public void approveOrder(Long orderId, Long approveById){
		ManagedOrder order = (ManagedOrder) this.orderDao.get(ManagedOrder.class, orderId);
		order.setStatus(Constants.order_approved);
		order.setLastUpdateDate(new Date());
		order.setApprovedByPerson(approveById);
		order.setApprovalDate(new Date());
		this.saveOrder(order);
		this.sendOrderApprovedEmail(orderId);
	}
	
	public void rejectOrder(Long orderId, Long rejectedById){
		ManagedOrder order = (ManagedOrder) this.orderDao.get(ManagedOrder.class, orderId);
		order.setStatus(Constants.order_rejected);
		order.setLastUpdateDate(new Date());
		order.setApprovedByPerson(rejectedById);
		this.saveOrder(order);
		this.sendOrderRejectedEmail(orderId);
	}
	
	public List<ManagedItem> inventorizeOrder(Long orderId){
		//change order status
		this.orderDao.changeOrderStatus(orderId, Constants.order_inventoried);
		return this.inventoryManager.addOrderToInventory(orderId);
		
	}
	private void sendOrderRejectedEmail(Long orderId) {
		log.debug("sending email to notice order rejected");
	}

	private void sendOrderApprovedEmail(Long orderId) {
		log.debug("sending email to notice order approved");
	}

	public void copyExistingOrder(ManagedOrder order) {
		ManagedOrder ord = new ManagedOrder();
		ord.setAccountNumber(order.getAccountNumber());
		ord.setApprovalDate(null);
		ord.setApprovedByPerson(order.getApprovedByPerson());
		ord.setCreatedDate(new Date());
		ord.setFundName(order.getFundName());
		ord.setLastUpdateDate(new Date());
		ord.setOrderByPerson(order.getOrderByPerson());
//		ord.setOrderItems(order.getOrderItems());
		ord.setSalesEmail(order.getSalesEmail());
		ord.setSalesFirstName(order.getSalesFirstName());
		ord.setSalesLastName(order.getSalesLastName());
		ord.setSalesPhone(order.getSalesPhone());
		ord.setStatus(Constants.order_draft);
		//TODO:automatically calculate from item totals
		ord.setTotalPrice(order.getTotalPrice());
		this.saveOrder(ord);
		Iterator<OrderItem> items = order.getOrderItems().iterator();
		while(items.hasNext()){
			OrderItem item = new OrderItem();
			OrderItem etm = items.next();
			item.setAmount(etm.getAmount());
			item.setCreatedDate(new Date());
			item.setItemCategory(etm.getItemCategory());
			item.setItemCategoryId(etm.getItemCategoryId());
			item.setLastUpdateDate(new Date());
			item.setMaker(etm.getMaker());
			item.setOrder(ord);
			item.setSupplier(etm.getSupplier());
			item.setTotalCost(etm.getTotalCost());
			item.setUnitPrice(etm.getUnitPrice());
			item.setAmount(etm.getAmount());
			this.saveOrderItem(ord.getId(), item);
		}
	}


}
