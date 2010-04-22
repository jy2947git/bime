package com.focaplo.myfuse.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="managed_item")
public class ManagedItem extends BaseObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8898274422544382002L;
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
	private Long id;
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="item_category_id")
	private ItemCategory itemCategory;
	@Column(nullable=true,length=50)
	private String maker;
	@Column(nullable=false)
	private Integer amount=0;
	@Column(nullable=true,length=50)
	private String storePersonId;
	@Column(nullable=true)
	private Date expirationDate;
	@Column(nullable=true, length=50)
	private String lastUserName;
	
	@ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.MERGE)
	@JoinColumn(name="storage_section_fk")
	private StorageSection storageSection;
	@Column(length=50)
	private String storageNotes;
	@OneToMany(mappedBy="managedItem",cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
	private Set<InventoryAudit> inventoryAudits=new HashSet<InventoryAudit>();
	
	@Transient
	public String getStorageDetail(){
		return this.getStorageSection()==null?this.getStorageNotes():this.getStorageSection().getStorage().getName() + "-" + this.getStorageSection().getStorage().getName() + "(" + this.getStorageNotes()+")";
	}
//	
//	@ManyToOne
//	@JoinColumn(name="order_item_fk")
//	private ManagedOrder order;
	@OneToOne
	@JoinColumn(name="order_item_id")
	private OrderItem orderItem;
	


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getStorePersonId() {
		return storePersonId;
	}

	public void setStorePersonId(String storePersonId) {
		this.storePersonId = storePersonId;
	}

	public StorageSection getStorageSection() {
		return storageSection;
	}

	public void setStorageSection(StorageSection storageSection) {
		this.storageSection = storageSection;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getLastUserName() {
		return lastUserName;
	}

	public void setLastUserName(String lastUserName) {
		this.lastUserName = lastUserName;
	}
//
//	public ManagedOrder getOrder() {
//		return order;
//	}
//
//	public void setOrder(ManagedOrder order) {
//		this.order = order;
//	}

	public OrderItem getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
	}

	public ItemCategory getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(ItemCategory itemCategory) {
		this.itemCategory = itemCategory;
	}

	public String getStorageNotes() {
		return storageNotes;
	}

	public void setStorageNotes(String storageNotes) {
		this.storageNotes = storageNotes;
	}

	public Set<InventoryAudit> getInventoryAudits() {
		return inventoryAudits;
	}

	public void setInventoryAudits(Set<InventoryAudit> inventoryAudits) {
		this.inventoryAudits = inventoryAudits;
	}

}
