package com.focaplo.myfuse.dao;

import java.math.BigDecimal;
import java.util.List;

import com.focaplo.myfuse.model.ManagedOrder;
import com.focaplo.myfuse.model.OrderItem;

public interface IOrderDao extends IUniversalDao {

	List<ManagedOrder> getOrdersForMyApproval(Long userId);

	List<ManagedOrder> getOrdersSubmittedBy(Long userId);

	void deleteOrderItem(Long id);

	void updateOrderItem(Long id, Long itemCategoryId, String maker, BigDecimal unitPrice, Integer amount, BigDecimal totalCost, String supplier);

	List<OrderItem> getOrderItemsOfOrder(Long orderId);

	void changeOrderStatus(Long orderId, String status);

	void updateOrderTotalCost(Long id, BigDecimal total);

}
