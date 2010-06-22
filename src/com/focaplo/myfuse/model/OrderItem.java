package com.focaplo.myfuse.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@Table(name="order_item")
public class OrderItem extends BaseObject implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
	private Long id;
	@ManyToOne
	@JoinColumn(name="item_category_id")
	private ItemCategory itemCategory;
	@Transient
	private Long itemCategoryId;
	@Column(nullable=true,length=50)
	private String maker;
	@Column(nullable=false)
	private Integer amount=0;
	@Column(nullable=true)
	private BigDecimal unitPrice=BigDecimal.ZERO;
	@Column(nullable=false)
	private BigDecimal totalCost=BigDecimal.ZERO;
	@Column(nullable=true)
	private String supplier;
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="order_id")
	private ManagedOrder order;
	@OneToOne(mappedBy="orderItem", fetch=FetchType.LAZY)
	private ManagedItem inventoryItem;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public ItemCategory getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(ItemCategory itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getMaker() {
		return maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public BigDecimal getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public ManagedOrder getOrder() {
		return order;
	}

	public void setOrder(ManagedOrder order) {
		this.order = order;
	}

	public ManagedItem getInventoryItem() {
		return inventoryItem;
	}

	public void setInventoryItem(ManagedItem inventoryItem) {
		this.inventoryItem = inventoryItem;
	}

	public Long getItemCategoryId() {
		return this.getItemCategory()==null?this.itemCategoryId:this.getItemCategory().getId();
	}

	public void setItemCategoryId(Long itemCategoryId) {
		this.itemCategoryId = itemCategoryId;
	}

}
