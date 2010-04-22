package com.focaplo.myfuse.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.focaplo.myfuse.Constants;
@Entity
@Table(name="item_category", uniqueConstraints={@UniqueConstraint(columnNames={"name","type"})})
public class ItemCategory extends BaseObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1794263501848309774L;
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
	private Long id;
	@Column(nullable=false,length=50,unique=false)
	private String name;
	@Column(nullable=false,length=50)
	private String type;
	@Column(nullable=false)
	private Integer totalAmount=0;
	@OneToMany(mappedBy="itemCategory", fetch=FetchType.LAZY)
	private Set<ManagedItem> managedItems = new HashSet<ManagedItem>();
	
	@OneToMany(mappedBy="itemCategory", fetch=FetchType.LAZY)
	private Set<OrderItem> orderItems = new HashSet<OrderItem>();
	
	@Column
	private Integer inventoryThreshold = new Integer(Constants.default_inventory_threshold);

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<ManagedItem> getManagedItems() {
		return managedItems;
	}

	public void setManagedItems(Set<ManagedItem> managedItems) {
		this.managedItems = managedItems;
	}

	public Set<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(Set<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getInventoryThreshold() {
		return inventoryThreshold;
	}

	public void setInventoryThreshold(Integer inventoryThreshold) {
		this.inventoryThreshold = inventoryThreshold;
	}

}
