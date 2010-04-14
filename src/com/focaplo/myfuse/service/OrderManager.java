package com.focaplo.myfuse.service;

import java.math.BigDecimal;
import java.util.List;

import com.focaplo.myfuse.model.ManagedItem;
import com.focaplo.myfuse.model.ManagedOrder;
import com.focaplo.myfuse.model.OrderItem;
import com.focaplo.myfuse.model.StorageSection;

public interface OrderManager extends UniversalManager {

	List<ManagedOrder> getOrdersSubmittedBy(Long userId);

	List<ManagedOrder>  getOrdersForApproval(Long userId);
	public void updateOrderItem(Long id, Long itemCategoryId, String maker, BigDecimal unitPrice, Integer amount, BigDecimal totalCost, String supplier);
	public void deleteOrderItem(Long id);
	public void saveOrderItem(Long orderId, OrderItem orderItem);
	public List<OrderItem> getOrderItemsOfOrder(Long orderId);
	public void deleteOrder(Long id) ;
	void deleteOrder(List<Long> toBeDeleted);

	void saveOrder(ManagedOrder order);
	
	public void submitOrder(Long orderId, Long submitedBy, Long forApprovalBy);
	public void approveOrder(Long orderId, Long approveById);
	public void rejectOrder(Long orderId, Long rejectedById);
	public List<ManagedItem> inventorizeOrder(Long orderId);

	public void copyExistingOrder(ManagedOrder order);
}
