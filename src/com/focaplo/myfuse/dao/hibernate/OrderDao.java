package com.focaplo.myfuse.dao.hibernate;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.focaplo.myfuse.Constants;
import com.focaplo.myfuse.dao.IOrderDao;
import com.focaplo.myfuse.model.ManagedOrder;
import com.focaplo.myfuse.model.OrderItem;
@Repository(value="orderDao")
public class OrderDao extends UniversalDao implements
		IOrderDao {

	@SuppressWarnings("unchecked")
	public List<ManagedOrder> getOrdersForMyApproval(Long userId) {
		return (List<ManagedOrder>) this.getSessionFactory().getCurrentSession().createQuery("from ManagedOrder where approvedByPerson=? and status='" + Constants.order_submitted+"'").setLong(0,userId).list();
	}

	public List<ManagedOrder> getOrdersSubmittedBy(Long userId) {
		return (List<ManagedOrder>) this.getSessionFactory().getCurrentSession().createQuery("from ManagedOrder where submittedByPerson=? and status='" + Constants.order_submitted+"'").setLong(0,userId).list();
	}

	public void deleteOrderItem(Long id) {
		this.remove(OrderItem.class, id);
	}

	public void updateOrderItem(Long id, Long itemCategoryId, String maker, BigDecimal unitPrice, Integer amount, BigDecimal totalCost, String supplier) {
		this.getSessionFactory().getCurrentSession().createQuery("update OrderItem set itemCategory=?, maker=?, unitPrice=?, amount=?, totalCost=?, supplier=?  where id=?")
		.setLong(0, itemCategoryId)
		.setString(1, maker)
		.setBigDecimal(2, unitPrice)
		.setInteger(3, amount)
		.setBigDecimal(4, totalCost)
		.setString(5, supplier)
		.setLong(6,id)
		.executeUpdate();
	}

	
	public List<OrderItem> getOrderItemsOfOrder(Long orderId) {
		return this.getSessionFactory().getCurrentSession().createQuery("from OrderItem where order.id=?").setLong(0, orderId).list();
	}

	public void changeOrderStatus(Long orderId, String status) {
		this.getSessionFactory().getCurrentSession().createQuery("update ManagedOrder set status=? where id=?").setString(0, status).setLong(1, orderId).executeUpdate();
	}

	public void updateOrderTotalCost(Long id, BigDecimal total) {
		this.getSessionFactory().getCurrentSession().createQuery("update ManagedOrder set totalPrice=? where id=?").setBigDecimal(0, total).setLong(1, id).executeUpdate();
	}

}
